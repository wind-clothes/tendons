package org.tendons.registry.loadbalance.impl;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.tendons.common.request.RequestWrapper;
import org.tendons.common.util.DigestHashUtil;
import org.tendons.registry.loadbalance.AbstractLoadBalancer;
import org.tendons.registry.loadbalance.ServiceProvider;
import org.tendons.registry.loadbalance.ServiceProviderEntity;

/**
 * <pre>
 * 源地址哈希均衡-一致性hash算法
 * Hash：源地址哈希的思想是根据获取客户端的IP地址，通过哈希函数计算得到的一个数值，
 * 用该数值对服务器列表的大小进行取模运算，得到的结果便是客户端要访问的服务器的序号。
 * 采用源地址哈希法进行负载均衡，同一IP地址的客户端，当后端服务器列表不变时，
 * 它每次都会映射到同一台后端服务器进行访问
 * TODO
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午4:17:57
 */
public class HashLoadBalancer extends AbstractLoadBalancer {

  // 通过 serviceName作为key值得一致性hash环
  private final ConcurrentMap<String, ConsistentHashSelector<?>> selectors =
      new ConcurrentHashMap<String, ConsistentHashSelector<?>>();

  @SuppressWarnings("unchecked")
  @Override
  protected <T> ServiceProvider<T> doSelected(List<ServiceProvider<T>> serviceProviders, RequestWrapper request) {
    final String key = serviceProviders.get(0).getServiceName();
    final int identityHashCode = System.identityHashCode(serviceProviders);

    ConsistentHashSelector<T> selector = (ConsistentHashSelector<T>) selectors.get(key);
    if (selector == null || selector.getIdentityHashCode() != identityHashCode) {
      selectors.put(key, new ConsistentHashSelector<T>(serviceProviders, identityHashCode));
      selector = (ConsistentHashSelector<T>) selectors.get(key);
    }
    return selector.select(request);
  }

  /**
   * 一致性Hash算法，hash环
   * 
   * @author: xcw
   * @date:2017年5月22日 上午11:09:01
   */
  private static final class ConsistentHashSelector<T> {

    /**
     * 虚拟节点
     */
    private final TreeMap<Long, ServiceProvider<T>> virtualServiceProviders;

    /**
     * 設置的虛擬節點的個數
     */
    private final int replicaNumber;
 
    /**
     * 自定义的hashCode
     */
    private final int identityHashCode;

    /**
     * 参数索引值
     */
    //private final int[] argumentIndex;

    public ConsistentHashSelector(List<ServiceProvider<T>> serviceProviders, int identityHashCode) {
      this.virtualServiceProviders = new TreeMap<Long, ServiceProvider<T>>();
      this.identityHashCode = System.identityHashCode(serviceProviders);

      ServiceProviderEntity entity = serviceProviders.get(0).getEntity();
      this.replicaNumber = entity.getReplicaNumber();

      // 将hash环分为四块，然后每个区域内，都再分为4块，然后每一块都有一个实例
      // 一個實例有replicaNumber节点，作为其虚拟节点。
      for (ServiceProvider<T> provider : serviceProviders) {
        for (int i = 0; i < replicaNumber / 4; i++) {
          final byte[] digest = DigestHashUtil.md5(provider.getEntity().toString() + i);
          for (int h = 0; h < 4; h++) {
            final long m = hash(digest, h);
            virtualServiceProviders.put(m, provider);
          }
        }
      }
    }

    public int getIdentityHashCode() {
      return identityHashCode;
    }

    public ServiceProvider<T> select(RequestWrapper requestWrapper) {
      final String key = toKey(requestWrapper.getArguments());
      final byte[] digest = DigestHashUtil.md5(key);
      final ServiceProvider<T> invoker = sekectForKey(hash(digest, 0));
      return invoker;
    }

    private String toKey(Object[] args) {
      final StringBuilder buf = new StringBuilder();
      for (Object object : args) {
        buf.append(object);
      }
      return buf.toString();
    }

    private ServiceProvider<T> sekectForKey(long hash) {
      ServiceProvider<T> serviceProvider;
      Long key = hash;
      if (!virtualServiceProviders.containsKey(key)) {
        final SortedMap<Long, ServiceProvider<T>> tailMap = virtualServiceProviders.tailMap(key);
        if (tailMap.isEmpty()) {
          key = virtualServiceProviders.firstKey();
        } else {
          key = tailMap.firstKey();
        }
      }
      serviceProvider = virtualServiceProviders.get(key);
      return serviceProvider;
    }

    private long hash(byte[] digest, int number) {
      return (((long) (digest[3 + number * 4] & 0xFF) << 24) | ((long) (digest[2 + number * 4] & 0xFF) << 16)
          | ((long) (digest[1 + number * 4] & 0xFF) << 8) | (digest[0 + number * 4] & 0xFF)) & 0xFFFFFFFFL;
    }
  }

}
