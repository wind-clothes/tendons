package org.tendons.registry.zookeeper.client;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.tendons.registry.RegistryCenterConfig;
import org.tendons.registry.RegistryCenterConfig.Authorization;
import org.tendons.registry.zookeeper.ChildListener;
import org.tendons.registry.zookeeper.StateListener;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月29日 下午9:28:19
 */
public class CuratorZookeeperClient extends AbstractZookeeperClient<CuratorWatcher> {

  /**
   * zookeeper 客户端
   */
  private final CuratorFramework client;

  public CuratorZookeeperClient(RegistryCenterConfig config) {
    super(config);
    // init zookeeper client
    final Builder builder = CuratorFrameworkFactory.builder().connectString(config.getServer())
        .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))
        .connectionTimeoutMs(config.getConnectionTimeoutMs()).sessionTimeoutMs(config.getTimeout());
    final Authorization authorization = config.getAuthorization();
    if (authorization != null) {
      builder.authorization("digest", authorization.getBytes());
    }
    client = builder.build();
    client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
      @Override
      public void stateChanged(CuratorFramework client, ConnectionState newState) {
        switch (newState) {
          case LOST:
            CuratorZookeeperClient.this.stateChanged(StateListener.DISCONNECTED);
            break;
          case RECONNECTED:
            CuratorZookeeperClient.this.stateChanged(StateListener.RECONNECTED);
            break;
          case CONNECTED:
            CuratorZookeeperClient.this.stateChanged(StateListener.CONNECTED);
            break;
          default:
            break;
        }
      }
    });
    client.start();
  }

  @Override
  public void delete(String path) {
    try {
      client.delete().deletingChildrenIfNeeded().forPath(path);
    } catch (Exception e) {
      LOGGER.warn("delete path:{} is error {}", path, e.getMessage());
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  @Override
  public List<String> getChildren(String path) {
    try {
      return client.getChildren().forPath(path);
    } catch (Exception e) {
      LOGGER.warn("getChildren path:{} is error {}", path, e.getMessage());
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  @Override
  protected void doData(String path, byte[] data) {
    try {
      client.setData().forPath(path, data);
    } catch (Exception e) {
      LOGGER.warn("set data is error {}", e.getMessage());
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  @Override
  protected byte[] findData(String path) {
    try {
      return client.getData().usingWatcher(new CuratorWatcher() {

        @Override
        public void process(WatchedEvent event) throws Exception {
          // TODO I NEED DO EVERYTHING
        }
      }).forPath(path);
    } catch (Exception e) {
      LOGGER.warn("find data is error {}", e.getMessage());
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  @Override
  protected void doClose() {
    client.close();
  }

  @Override
  protected void createEphemeral(String path) {
    try {
      client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
    } catch (Exception e) {
      LOGGER.warn("createEphemeral is error {}", e.getMessage());
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  @Override
  protected void createPersistent(String path) {
    try {
      client.create().withMode(CreateMode.PERSISTENT).forPath(path);
    } catch (Exception e) {
      LOGGER.warn("createPersistent is error {}", e.getMessage());
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  @Override
  protected CuratorWatcher createTargetChildListener(String path, ChildListener listener) {
    return new CuratorWatcherImpl(listener);
  }

  @Override
  protected List<String> addTargetChildListener(String path, CuratorWatcher listener) {
    try {
      return client.getChildren().usingWatcher(listener).forPath(path);
    } catch (Exception e) {
      LOGGER.warn("addTargetChildListener is error {}", e.getMessage());
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  @Override
  protected void removeTargetChildListener(String path, CuratorWatcher listener) {
    ((CuratorWatcherImpl) listener).unwatch();
  }

  @Override
  public boolean isConnected() {
    return client.getZookeeperClient().isConnected();
  }

  /**
   * 监视器
   * 
   * @author: chengweixiong@uworks.cc
   * @date: 2017年6月10日 下午10:32:42
   */
  private class CuratorWatcherImpl implements CuratorWatcher {

    private volatile ChildListener listener;

    public CuratorWatcherImpl(ChildListener listener) {
      this.listener = listener;
    }

    public void unwatch() {
      this.listener = null;
    }

    @Override
    public void process(WatchedEvent event) throws Exception {
      if (listener != null) {
        listener.childChanged(event.getPath(),
            client.getChildren().usingWatcher(this).forPath(event.getPath()));
      }
    }
  }
}
