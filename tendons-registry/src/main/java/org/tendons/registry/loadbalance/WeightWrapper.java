package org.tendons.registry.loadbalance;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午8:33:00
 */
public final class WeightWrapper {

  private int sumWeight;
  private boolean sameWight;

  public WeightWrapper() {}

  public WeightWrapper(boolean sameWight) {
    this(sameWight, 0);
  }

  public WeightWrapper(boolean sameWight, int sumWeight) {
    this.sumWeight = sumWeight;
    this.sameWight = sameWight;
  }

  public int getSumWeight() {
    return sumWeight;
  }

  public void setSumWeight(int sumWeight) {
    this.sumWeight = sumWeight;
  }

  public boolean getSameWight() {
    return sameWight;
  }

  public void setSameWight(boolean sameWight) {
    this.sameWight = sameWight;
  }

}
