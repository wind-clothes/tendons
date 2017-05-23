package org.tendons.registry.loadbalance;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.tendons.registry.loadbalance.impl.HashLoadBalancer;
import org.tendons.registry.loadbalance.impl.LeastActiveLoadBalance;
import org.tendons.registry.loadbalance.impl.RandomLoadBalancer;
import org.tendons.registry.loadbalance.impl.RoundRobinLoadBalancer;
import org.tendons.registry.loadbalance.impl.WeightRandomLoadBalancer;
import org.tendons.registry.loadbalance.impl.WeightRoundRobinLoadBalancer;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午4:25:36 
 */
public final class LoadBalancerFactory {

  private final static Map<String, LoadBalancer> loadBalancers = new HashMap<String, LoadBalancer>(){
    
    private static final long serialVersionUID = 8101544408348355663L;

    {
      // 可以通过反射动态的获取
      put(LoadBalancerType.HASH.toString(), new HashLoadBalancer());
      put(LoadBalancerType.LEAST_ACTIVE.toString(), new LeastActiveLoadBalance());
      put(LoadBalancerType.RANDOM.toString(), new RandomLoadBalancer());
      put(LoadBalancerType.ROUND_ROBIN.toString(), new RoundRobinLoadBalancer());
      put(LoadBalancerType.WEIGHT_RANDOM.toString(), new WeightRandomLoadBalancer());
      put(LoadBalancerType.WEIGHT_ROUND_ROBIN.toString(), new WeightRoundRobinLoadBalancer());
    }
  };

  static{}

  public static LoadBalancer match(LoadBalancerType type) {
    Objects.requireNonNull(type, " LoadBalancerType must not null");
    return loadBalancers.get(type.toString());
  }
}
