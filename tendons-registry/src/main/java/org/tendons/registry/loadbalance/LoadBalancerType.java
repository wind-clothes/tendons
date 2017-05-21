package org.tendons.registry.loadbalance;

/**
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午4:04:28
 */
public enum LoadBalancerType {
  RANDOM("随机"), WEIGHT_RANDOM("加权随机"), ROUND_ROBIN("轮询"), WEIGHT_ROUND_ROBIN("加权轮询"), HASH("源地址哈希");
  private String label;

  private LoadBalancerType(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }
}
