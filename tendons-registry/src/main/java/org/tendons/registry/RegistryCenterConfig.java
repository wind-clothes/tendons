package org.tendons.registry;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月29日 下午8:39:21
 */
public class RegistryCenterConfig {

  // 注册中心的地址
  private String server;
  // 连接超时的时间
  private Integer connectionTimeoutMs;
  // 缓存保存的时间
  private Integer timeOut;
  private Authorization authorization;

  public static class Authorization {
    private byte[] bytes;

    public byte[] getBytes() {
      return bytes;
    }

    public void setBytes(byte[] bytes) {
      this.bytes = bytes;
    }

  }

  public Authorization getAuthorization() {
    return authorization;
  }

  public void setAuthorization(Authorization authorization) {
    this.authorization = authorization;
  }

  public String getServer() {
    return server;
  }

  public void setServer(String server) {
    this.server = server;
  }

  public Integer getConnectionTimeoutMs() {
    return connectionTimeoutMs;
  }

  public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
    this.connectionTimeoutMs = connectionTimeoutMs;
  }

  public Integer getTimeOut() {
    return timeOut;
  }

  public void setTimeOut(Integer timeOut) {
    this.timeOut = timeOut;
  }

  public Integer getTimeout() {
    return timeOut;
  }

}
