package org.tendons.common.config;

/**
 * @author: xcw
 * @date:2017年5月23日 下午4:44:24
 */
public class TendonsTcpServerConfig extends TendonsRpcServerConfig {

  private int connectTimeOut = 4000;
  private boolean keepLive;
  private boolean tcpNnodelay;

  public TendonsTcpServerConfig() {
    super();
  }

  public int getConnectTimeOut() {
    return connectTimeOut;
  }

  public void setConnectTimeOut(int connectTimeOut) {
    this.connectTimeOut = connectTimeOut;
  }

  public boolean isKeepLive() {
    return keepLive;
  }

  public void setKeepLive(boolean keepLive) {
    this.keepLive = keepLive;
  }

  public boolean isTcpNnodelay() {
    return tcpNnodelay;
  }

  public void setTcpNnodelay(boolean tcpNnodelay) {
    this.tcpNnodelay = tcpNnodelay;
  }

}
