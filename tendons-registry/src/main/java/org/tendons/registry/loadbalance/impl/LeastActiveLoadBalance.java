package org.tendons.registry.loadbalance.impl;

import java.util.List;

import org.tendons.common.request.RequestWrapper;
import org.tendons.common.util.RandomUtil;
import org.tendons.registry.loadbalance.AbstractLoadBalancer;
import org.tendons.registry.loadbalance.ServiceProvider;

/**
 * <pre>
 * 最小连接数算法
 * 最小连接数算法比较灵活和智能，由于后端服务器的配置不尽相同，对于请求的处理有快有慢，
 * 它是根据后端服务器当前的连接情况，动态地选取其中当前积压连接数最少的一台服务器来处理当前的请求，
 * 尽可能地提高后端服务的利用效率，将负责合理地分流到每一台服务器。<br>
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午9:34:38
 */
public class LeastActiveLoadBalance extends AbstractLoadBalancer {

  /**
   * <pre>
   * 算法：
   * 通过遍历获取所有最小连接数的的实例，然后，如果权重相同，直接在最小实例的集合中随机取一个；
   * 如果不相同，则针对最小连接数集合,先生成随机 的权重weight，然后再针对weight按权重匹配到具体的实例中  {@link WeightRandomLoadBalancer}
   * 核心就是必須的獲得每一個實例的連接數。
   * </pre>
   */
  @Override
  protected <T> ServiceProvider<T> doSelected(List<ServiceProvider<T>> serviceProviders, RequestWrapper request) {
    final int maxSize = serviceProviders.size(); // 总个数
    int leastActive = -1; // 最小的活跃数
    int leastCount = 0; // 相同最小活跃数的个数
    final int[] leastIndexs = new int[maxSize]; // 相同最小活跃数的下标
    int sumWeight = 0; // 总权重
    int firstWeight = 0; // 第一个权重，用于于计算是否相同
    boolean sameWeight = true; // 是否所有权重相同
    for (int i = 0; i < maxSize; i++) {
      ServiceProvider<T> serviceProvider = serviceProviders.get(i);
      final int active = 0; // 活跃数 TODO
      int weight = serviceProvider.getWeight(); // 权重
      if (leastActive == -1 || active < leastActive) { // 发现更小的活跃数，重新开始
        leastActive = active; // 记录最小活跃数
        leastCount = 1; // 重新统计相同最小活跃数的个数
        leastIndexs[0] = i; // 重新记录最小活跃数下标
        sumWeight = weight; // 重新累计总权重
        firstWeight = weight; // 记录第一个权重
        sameWeight = true; // 还原权重相同标识
      } else if (active == leastActive) { // 累计相同最小的活跃数
        leastIndexs[leastCount++] = i; // 累计相同最小活跃数下标
        sumWeight += weight; // 累计总权重
        // 判断所有权重是否一样
        if (sameWeight && i > 0 && weight != firstWeight) {
          sameWeight = false;
        }
      }
    }
    if (leastCount == 1) {
      // 如果只有一个最小则直接返回
      return serviceProviders.get(leastIndexs[0]);
    }
    if (!sameWeight && sumWeight > 0) {
      // 如果权重不相同且权重大于0则按总权重数随机
      int offsetWeight = RandomUtil.random(sumWeight);
      // 并确定随机值落在哪个片断上
      for (int i = 0; i < leastCount; i++) {
        int leastIndex = leastIndexs[i];
        offsetWeight -= getWeight(serviceProviders.get(leastIndex), request);
        if (offsetWeight <= 0) {
          return serviceProviders.get(leastIndex);
        }
      }
    }
    // 如果权重相同或权重为0则均等随机
    return serviceProviders.get(leastIndexs[RandomUtil.random(leastCount)]);
  }

}
