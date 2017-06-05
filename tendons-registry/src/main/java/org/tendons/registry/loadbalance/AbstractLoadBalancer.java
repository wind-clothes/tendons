package org.tendons.registry.loadbalance;

import java.util.List;

import org.tendons.common.request.RequestWrapper;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午2:18:31
 */
public abstract class AbstractLoadBalancer implements LoadBalancer {

  public <T> ServiceProvider<T> selected(List<ServiceProvider<T>> serviceProviders, RequestWrapper request) {
    if (serviceProviders == null || serviceProviders.size() == 0) {
      return null;
    }
    if (serviceProviders.size() == 1) {
      return serviceProviders.get(0);
    }
    return doSelected(serviceProviders, request);
  }

  protected abstract <T> ServiceProvider<T> doSelected(List<ServiceProvider<T>> serviceProviders,
      RequestWrapper request);

  // 计算权重, 包含预热逻辑
  protected int getWeight(ServiceProvider<?> serviceProvider, RequestWrapper request) {
    final int weight = serviceProvider.getWeight();
    if (weight > 0) {
      final long timestamp = serviceProvider.getTimestamp();
      if (timestamp > 0L) {
        // 上线时间
        final int upTime = (int) (System.currentTimeMillis() - timestamp);
        // 预热时间
        final int warmUp = serviceProvider.getWarmUp();
        if (warmUp > 0 && upTime < warmUp) {
          // 服务预热中, 计算预热权重
          return calculateWarmupWeight(upTime, warmUp, weight);
        }
      }
    }
    return weight;
  }

  // TODO
  static int calculateWarmupWeight(int upTime, int warmUp, int weight) {
    int ww = (int) ((float) upTime / ((float) warmUp / (float) weight));
    return ww < 1 ? 1 : (ww > weight ? weight : ww);
  }


  /**
   * 获得服务的唯一key值 TODO
   * @param serviceProviders
   * @return String
   */
  protected <E> String buildKey(List<ServiceProvider<E>> serviceProviders) {
    return serviceProviders.get(0).getServiceName();
  }
}
