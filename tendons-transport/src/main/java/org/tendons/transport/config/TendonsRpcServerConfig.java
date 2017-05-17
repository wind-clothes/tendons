package org.tendons.transport.config;

import org.tendons.common.config.RpcTranspotConfig;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月10日 下午11:46:51
 */
public class TendonsRpcServerConfig implements RpcTranspotConfig {

  /**
   * 默认端口
   */
  private final static int DEFAULT_PORT = 80;
  private final static int DEFAULT_NETTY_BACKLOG = 1024;
  private final static int DEFAULT_NETTY_WORKER_NEVENTLOOPS_AVAILABLEPROCESSORS_MULTIPLE = 2;
  private final static int DEFAULT_NETTY_BOSS_NEVENTLOOPS_AVAILABLEPROCESSORS_MULTIPLE = 2;

  private final int port;
  private final int backlog;
  private final int workerEventLoopsAvailableProcessorsMultiple;
  private final int bossEventLoopsAvailableProcessorsMultiple;

  public TendonsRpcServerConfig(int port, int backlog,
      int workerEventLoopsAvailableProcessorsMultiple,
      int bossEventLoopsAvailableProcessorsMultiple) {
    this.port = port == 0 ? DEFAULT_PORT : port;
    this.backlog = backlog == 0 ? DEFAULT_NETTY_BACKLOG : backlog;
    this.workerEventLoopsAvailableProcessorsMultiple =
        workerEventLoopsAvailableProcessorsMultiple == 0
            ? DEFAULT_NETTY_WORKER_NEVENTLOOPS_AVAILABLEPROCESSORS_MULTIPLE
            : workerEventLoopsAvailableProcessorsMultiple;
    this.bossEventLoopsAvailableProcessorsMultiple =
        (bossEventLoopsAvailableProcessorsMultiple == 0)
            ? DEFAULT_NETTY_BOSS_NEVENTLOOPS_AVAILABLEPROCESSORS_MULTIPLE
            : bossEventLoopsAvailableProcessorsMultiple;
  }

  public int getPort() {
    return port;
  }

  public int getBacklog() {
    return backlog;
  }

  public int getWorkerEventLoopsAvailableProcessorsMultiple() {
    return workerEventLoopsAvailableProcessorsMultiple;
  }

  public int getBossEventLoopsAvailableProcessorsMultiple() {
    return bossEventLoopsAvailableProcessorsMultiple;
  }

}
