package org.tendons.registry.loadbalance.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.tendons.common.RequestWrapper;
import org.tendons.common.util.AtomicNativeInteger;
import org.tendons.registry.loadbalance.AbstractLoadBalancer;
import org.tendons.registry.loadbalance.ServiceProvider;

/**
 * <pre>
 * 轮询负载均衡:
 * 由于服务器的地址列表是动态的，随时可能有机器上线、下线或者宕机，
 * 轮询法的优点在于：试图做到请求转移的绝对均衡。
 * 轮询法的缺点在于：存在并发问题，需要全局保存其轮询的状态
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午4:17:57
 */
public class RoundRobinLoadBalancer extends AbstractLoadBalancer {

  private final ConcurrentHashMap<String, AtomicNativeInteger> roundRobinService =
      new ConcurrentHashMap<>();

  @Override
  protected <T> ServiceProvider<T> doSelected(List<ServiceProvider<T>> serviceProviders,
      RequestWrapper request) {
    final String serviceName = serviceProviders.get(0).getServiceName();
    final int maxSize = serviceProviders.size();
    AtomicNativeInteger sequence = roundRobinService.get(serviceName);
    if (sequence == null) {
      sequence = roundRobinService.putIfAbsent(serviceName, new AtomicNativeInteger());
    }
    int currentSequence = sequence.getAndIncrement();
    return serviceProviders.get(currentSequence % maxSize);
  }

}
