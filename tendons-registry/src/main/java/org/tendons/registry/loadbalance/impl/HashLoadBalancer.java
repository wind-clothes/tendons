package org.tendons.registry.loadbalance.impl;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.tendons.common.RequestWrapper;
import org.tendons.common.util.DigestHashUtil;
import org.tendons.registry.loadbalance.AbstractLoadBalancer;
import org.tendons.registry.loadbalance.ServiceProvider;

/**
 * <pre>
 * 源地址哈希均衡-一致性hash算法
 * Hash：源地址哈希的思想是根据获取客户端的IP地址，通过哈希函数计算得到的一个数值，
 * 用该数值对服务器列表的大小进行取模运算，得到的结果便是客户端要访问的服务器的序号。
 * 采用源地址哈希法进行负载均衡，同一IP地址的客户端，当后端服务器列表不变时，
 * 它每次都会映射到同一台后端服务器进行访问
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午4:17:57
 */
public class HashLoadBalancer extends AbstractLoadBalancer {

  private final ConcurrentMap<String, ConsistentHashSelector<?>> selectors =
      new ConcurrentHashMap<String, ConsistentHashSelector<?>>();

  @SuppressWarnings("unchecked")
  @Override
  protected <T> ServiceProvider<T> doSelected(List<ServiceProvider<T>> serviceProviders,
      RequestWrapper request) {
    String key = serviceProviders.get(0).getServiceName();
    int identityHashCode = System.identityHashCode(serviceProviders);
    ConsistentHashSelector<T> selector = (ConsistentHashSelector<T>) selectors.get(key);
    if (selector == null || selector.getIdentityHashCode() != identityHashCode) {
      selectors.put(key, new ConsistentHashSelector<T>(serviceProviders, null, identityHashCode));
      selector = (ConsistentHashSelector<T>) selectors.get(key);
    }
    return selector.select(null);
  }

  private static final class ConsistentHashSelector<T> {

    private final TreeMap<Long, ServiceProvider<T>> virtualInvokers;

    private final int replicaNumber;

    private final int identityHashCode;

    private final int[] argumentIndex;

    public ConsistentHashSelector(List<ServiceProvider<T>> invokers, String methodName,
        int identityHashCode) {
      this.virtualInvokers = new TreeMap<Long, ServiceProvider<T>>();
      this.identityHashCode = System.identityHashCode(invokers);
      URL url = invokers.get(0).getUrl();
      this.replicaNumber = url.getMethodParameter(methodName, "hash.nodes", 160);
      String[] index = Constants.COMMA_SPLIT_PATTERN
          .split(url.getMethodParameter(methodName, "hash.arguments", "0"));
      argumentIndex = new int[index.length];
      for (int i = 0; i < index.length; i++) {
        argumentIndex[i] = Integer.parseInt(index[i]);
      }
      for (ServiceProvider<T> invoker : invokers) {
        for (int i = 0; i < replicaNumber / 4; i++) {
          byte[] digest = md5(invoker.getUrl().toFullString() + i);
          for (int h = 0; h < 4; h++) {
            long m = hash(digest, h);
            virtualInvokers.put(m, invoker);
          }
        }
      }
    }

    public int getIdentityHashCode() {
      return identityHashCode;
    }

    public ServiceProvider<T> select(RequestWrapper requestWrapper) {
      String key = toKey(requestWrapper.getArguments());
      byte[] digest = DigestHashUtil.md5(key);
      ServiceProvider<T> invoker = sekectForKey(hash(digest, 0));
      return invoker;
    }

    private String toKey(Object[] args) {
      StringBuilder buf = new StringBuilder();
      for (int i : argumentIndex) {
        if (i >= 0 && i < args.length) {
          buf.append(args[i]);
        }
      }
      return buf.toString();
    }

    private ServiceProvider<T> sekectForKey(long hash) {
      ServiceProvider<T> invoker;
      Long key = hash;
      if (!virtualInvokers.containsKey(key)) {
        SortedMap<Long, ServiceProvider<T>> tailMap = virtualInvokers.tailMap(key);
        if (tailMap.isEmpty()) {
          key = virtualInvokers.firstKey();
        } else {
          key = tailMap.firstKey();
        }
      }
      invoker = virtualInvokers.get(key);
      return invoker;
    }

    private long hash(byte[] digest, int number) {
      return (((long) (digest[3 + number * 4] & 0xFF) << 24)
          | ((long) (digest[2 + number * 4] & 0xFF) << 16)
          | ((long) (digest[1 + number * 4] & 0xFF) << 8) | (digest[0 + number * 4] & 0xFF))
          & 0xFFFFFFFFL;
    }

  }

}
