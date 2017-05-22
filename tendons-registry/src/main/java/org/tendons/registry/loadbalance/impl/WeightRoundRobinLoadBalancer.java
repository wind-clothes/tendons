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
 * 如果相同的话就按等同的轮训算法访问，
 * 如果不相同的话：按最计算上次轮训时的权重比例，然后选择比其大的权重比例的机器然后进行轮训
 * 每个服务应有各自独立的实例(index不共享)
 * </pre>
 *
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午4:02:33
 */
public class WeightRoundRobinLoadBalancer extends AbstractLoadBalancer {

  private final ConcurrentHashMap<String, AtomicNativeInteger> roundRobinService = new ConcurrentHashMap<>();

  @Override
  protected <T> ServiceProvider<T> doSelected(List<ServiceProvider<T>> serviceProviders, RequestWrapper request) {
    final String serviceName = buildKey(serviceProviders);

    final int maxSize = serviceProviders.size();

    int maxWeight = 0;
    int minWeight = Integer.MAX_VALUE;
    for (int i = 0; i < maxSize; i++) {
      final int weight = getWeight(serviceProviders.get(i), request);
      minWeight = Math.min(weight, minWeight);
      maxWeight = Math.max(weight, maxWeight);
    }

    AtomicNativeInteger sequence = roundRobinService.get(serviceName);
    if (sequence == null) {
      sequence = roundRobinService.putIfAbsent(serviceName, new AtomicNativeInteger());
    }
    int currentSequence = sequence.getAndIncrement();
    // 权重不一样，选择权重比较大的的机器进行访问
    if (maxWeight > 0 && minWeight < maxWeight) {

    }
    // 计算最大公约数
    // 当前实现不会先去计算最大公约数再轮询, 通常最大权重和最小权重值不会相差过于悬殊,
    // 因此我觉得没有必要先去求最大公约数, 很可能产生没有必要的开销.
    return serviceProviders.get(currentSequence % maxSize);
  }

}
