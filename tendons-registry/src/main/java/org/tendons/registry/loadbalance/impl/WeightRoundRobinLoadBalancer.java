package org.tendons.registry.loadbalance.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.tendons.common.RequestWrapper;
import org.tendons.common.util.AtomicNativeInteger;
import org.tendons.registry.loadbalance.AbstractLoadBalancer;
import org.tendons.registry.loadbalance.ServiceProvider;
import org.tendons.registry.loadbalance.WeightWrapper;

/**
 * <pre>
 * 加权轮询负载均衡.
 *
 * 当前实现不会先去计算最大公约数再轮询, 通常最大权重和最小权重值不会相差过于悬殊,
 * 因此我觉得没有必要先去求最大公约数, 很可能产生没有必要的开销.
 *
 * 每个服务应有各自独立的实例(index不共享)
 * </pre>
 *
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午4:02:33
 */
public class WeightRoundRobinLoadBalancer extends AbstractLoadBalancer {

  private final ConcurrentHashMap<String, AtomicNativeInteger> roundRobinService =
      new ConcurrentHashMap<>();

  @Override
  protected <T> ServiceProvider<T> doSelected(List<ServiceProvider<T>> serviceProviders,
      RequestWrapper request) {
    final String serviceName = serviceProviders.get(0).getServiceName();

    final int maxSize = serviceProviders.size();

    final WeightWrapper weightWrapper = sameWeight(serviceProviders, request);

    AtomicNativeInteger sequence = roundRobinService.get(serviceName);
    if (sequence == null) {
      sequence = roundRobinService.putIfAbsent(serviceName, new AtomicNativeInteger());
    }
    int currentSequence = sequence.getAndIncrement();

    // 计算最大公约数
    // 当前实现不会先去计算最大公约数再轮询, 通常最大权重和最小权重值不会相差过于悬殊,
    // 因此我觉得没有必要先去求最大公约数, 很可能产生没有必要的开销.
    return serviceProviders.get(currentSequence % maxSize);
  }

}
