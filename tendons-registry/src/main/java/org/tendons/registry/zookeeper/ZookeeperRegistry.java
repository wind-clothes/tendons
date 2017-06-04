package org.tendons.registry.zookeeper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tendons.common.service.RegisterServiceUrl;
import org.tendons.registry.AbstractRegistry;
import org.tendons.registry.RegistryCenterConfig;
import org.tendons.registry.service.NotifyListener;
import org.tendons.registry.zookeeper.client.ZookeeperClient;
import org.tendons.registry.zookeeper.client.ZookeeperClientFactory;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月29日 下午7:56:26
 */
public class ZookeeperRegistry extends AbstractRegistry {

  private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegistry.class);

  private ZookeeperClient client;

  public ZookeeperRegistry() {
    super();
  }

  @Override
  public void doClose() {
    client.close();
    LOGGER.info("ZookeeperRegistry is close");
  }

  @Override
  public void doConnection(RegistryCenterConfig centerConfig) {
    client = ZookeeperClientFactory.getZookeeperClient(centerConfig);
    client.addStateListener(new StateListener() {
      @Override
      public void stateChanged(int state) {

      }
    });
  }

  @Override
  public void register(RegisterServiceUrl serviceUrl) {

  }

  @Override
  public void unregister(RegisterServiceUrl serviceUrl) {

  }

  @Override
  public void subscribe(RegisterServiceUrl serviceUrl, NotifyListener listener) {

  }

  @Override
  public void unsubscribe(RegisterServiceUrl serviceUrl, NotifyListener listener) {

  }

  @Override
  public List<RegisterServiceUrl> lookup(RegisterServiceUrl serviceUrl) {
    return null;
  }

  @Override
  public RegisterServiceUrl getUrl() {
    return null;
  }

  @Override
  public boolean isAvailable() {
    return false;
  }

  @Override
  public void destroy() {

  }

}
