package org.tendons.registry.loadbalance.impl;

import java.util.List;

import org.tendons.common.request.RequestWrapper;
import org.tendons.common.util.RandomUtil;
import org.tendons.registry.loadbalance.AbstractLoadBalancer;
import org.tendons.registry.loadbalance.ServiceProvider;
import org.tendons.registry.loadbalance.WeightWrapper;

/**
 * <pre>
 * 加权随机负载均衡.
 * 
 * 加权随机法也是根据后端服务器不同的配置和负载情况来配置不同的权重；
 * 区别在于，它是按照权重来随机选择服务器的，而不是顺序。
 * </pre>
 *
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午4:01:20
 */
public class WeightRandomLoadBalancer extends AbstractLoadBalancer {

  @Override
  protected <T> ServiceProvider<T> doSelected(List<ServiceProvider<T>> serviceProviders,
      RequestWrapper request) {
    final int maxSize = serviceProviders.size();
    final WeightWrapper weightWrapper = sameWeight(serviceProviders, request);

    // 如果总的全总不为0 并且权重有不一样的，则按总的权重来计算 
    if (weightWrapper.getSumWeight() > 0 && !weightWrapper.getSameWight()) {
      int offset = RandomUtil.random(weightWrapper.getSumWeight());
      for (int i = 0; i < maxSize; i++) {
        offset -= getWeight(serviceProviders.get(i), request);
        if (offset < 0) {
          return serviceProviders.get(i);
        }
      }
    }
    return serviceProviders.get(RandomUtil.random(maxSize));
  }

  /**
   * 计算权重是否是相同的
   * 
   * @return boolean
   */
  private <E> WeightWrapper sameWeight(List<ServiceProvider<E>> serviceProviders, RequestWrapper request) {
    final WeightWrapper weightWrapper = new WeightWrapper();
    if (serviceProviders == null || serviceProviders.isEmpty()) {
      return weightWrapper;
    }

    final int size = serviceProviders.size();
    boolean sameWeight = true;
    int sumWeight = 0;
    for (int i = 0; i < size; i++) {
      final int weight = getWeight(serviceProviders.get(i), request);
      sumWeight += weight;
      if (sameWeight && i > 0 && weight != getWeight(serviceProviders.get(i - 1), request)) {
        sameWeight = false;
        break;
      }
    }
    weightWrapper.setSameWight(sameWeight);
    weightWrapper.setSumWeight(sumWeight);
    return weightWrapper;
  }
}
