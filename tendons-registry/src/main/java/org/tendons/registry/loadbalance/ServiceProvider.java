package org.tendons.registry.loadbalance;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午7:17:26
 */
public final class ServiceProvider<T> {
    //
    private T obj;
    //
    private String serviceName;
    // 权重
    private int weight;
    // 预热的时间
    private int warmUp;
    // 时间戳，发布的时间
    private long timestamp;

    private ServiceProviderEntity entity = new ServiceProviderEntity();

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

    public ServiceProviderEntity getEntity() {
        return entity;
    }

    public void setEntity(ServiceProviderEntity entity) {
        this.entity = entity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;

        int result = 1;
        result = prime * result + ((entity == null) ? 0 : entity.hashCode());
        result = prime * result + ((obj == null) ? 0 : obj.hashCode());
        result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        result = prime * result + warmUp;
        result = prime * result + weight;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServiceProvider other = (ServiceProvider) obj;
        if (entity == null) {
            if (other.entity != null)
                return false;
        } else if (!entity.equals(other.entity))
            return false;
        if (this.obj == null) {
            if (other.obj != null)
                return false;
        } else if (!this.obj.equals(other.obj))
            return false;
        if (serviceName == null) {
            if (other.serviceName != null)
                return false;
        } else if (!serviceName.equals(other.serviceName))
            return false;
        if (timestamp != other.timestamp)
            return false;
        if (warmUp != other.warmUp)
            return false;
        if (weight != other.weight)
            return false;
        return true;
    }
}
