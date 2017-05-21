package org.tendons.registry.loadbalance;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午7:17:26
 */
public final class ServiceProvider<T> {

  private T obj;
  private String serviceName;
  // 权重
  private int weight;
  // 预热的时间
  private int warmUp;
  // 时间戳，发布的时间
  private long timestamp;

  public T getObj() {
    return obj;
  }

  public void setObj(T obj) {
    this.obj = obj;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public int getWarmUp() {
    return warmUp;
  }

  public void setWarmUp(int warmUp) {
    this.warmUp = warmUp;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

}
