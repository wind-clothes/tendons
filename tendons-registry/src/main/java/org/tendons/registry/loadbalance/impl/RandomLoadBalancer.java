package org.tendons.registry.loadbalance.impl;

import java.util.List;

import org.tendons.common.RequestWrapper;
import org.tendons.common.util.RandomUtil;
import org.tendons.registry.loadbalance.AbstractLoadBalancer;
import org.tendons.registry.loadbalance.ServiceProvider;

/**
 * <pre>
 * 随机负载均衡-算法描述如下:
 * 在选取server的时候，通过Random的nextInt方法取0~MAXSIZE区间的一个随机值，
 * 从而从服务器列表中随机获取到一台服务器地址进行返回。
 * 基于概率统计的理论，吞吐量越大，随机算法的效果越接近于轮询算法的效果
 * </pre>
 *
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午4:01:20
 */
public class RandomLoadBalancer extends AbstractLoadBalancer {

  @Override
  protected <T> ServiceProvider<T> doSelected(List<ServiceProvider<T>> serviceProviders,
      RequestWrapper request) {
    final int maxSize = serviceProviders.size();
    final int randomInt = RandomUtil.random(maxSize);
    return serviceProviders.get(randomInt);
  }

}
