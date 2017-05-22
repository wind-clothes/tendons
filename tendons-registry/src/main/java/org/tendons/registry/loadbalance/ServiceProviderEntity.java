package org.tendons.registry.loadbalance;


public class ServiceProviderEntity {
  public int replicaNumber;

  public ServiceProviderEntity() {}
  

  public int getReplicaNumber() {
    return replicaNumber;
  }


  public void setReplicaNumber(int replicaNumber) {
    this.replicaNumber = replicaNumber;
  }


  @Override
  public String toString() {
    return super.toString();
  }
}