package org.tendons.registry.zookeeper.client;

import org.tendons.registry.RegistryCenterConfig;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月30日 上午11:23:29
 */
public class ZookeeperClientFactory {

  private ZookeeperClientFactory() {}

  public static ZookeeperClient getZookeeperClient(RegistryCenterConfig config) {
    return new CuratorZookeeperClient(config);
  }
}
