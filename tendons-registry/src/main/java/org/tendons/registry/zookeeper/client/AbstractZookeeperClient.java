package org.tendons.registry.zookeeper.client;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tendons.registry.RegistryCenterConfig;
import org.tendons.registry.zookeeper.ChildListener;
import org.tendons.registry.zookeeper.StateListener;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月29日 下午9:01:05
 */
public abstract class AbstractZookeeperClient<TargetChildListener> implements ZookeeperClient {

  protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractZookeeperClient.class);

  private final RegistryCenterConfig config;

  private final Set<StateListener> stateListeners = new CopyOnWriteArraySet<>();

  private final ConcurrentMap<String, ConcurrentMap<ChildListener, TargetChildListener>> childListeners =
      new ConcurrentHashMap<>();

  private volatile boolean closed = false;

  public AbstractZookeeperClient(RegistryCenterConfig config) {
    super();
    this.config = config;
  }

  public RegistryCenterConfig getConfig() {
    return config;
  }

  @Override
  public void create(String path, boolean ephemeral) {
    final int i = path.lastIndexOf('/');
    if (i > 0) {
      create(path.substring(0, i), false);
    }
    if (ephemeral) {
      createEphemeral(path);
    } else {
      createPersistent(path);
    }
  }

  @Override
  public List<String> addChildListener(String path, ChildListener listener) {
    ConcurrentMap<ChildListener, TargetChildListener> listeners = childListeners.get(path);
    if (listeners == null) {
      childListeners.putIfAbsent(path, new ConcurrentHashMap<ChildListener, TargetChildListener>());
      listeners = childListeners.get(path);
    }
    TargetChildListener targetListener = listeners.get(listener);
    if (targetListener == null) {
      listeners.putIfAbsent(listener, createTargetChildListener(path, listener));
      targetListener = listeners.get(listener);
    }
    return addTargetChildListener(path, targetListener);
  }

  @Override
  public void removeChildListener(String path, ChildListener listener) {
    ConcurrentMap<ChildListener, TargetChildListener> listeners = childListeners.get(path);
    if (listeners != null) {
      TargetChildListener targetListener = listeners.remove(listener);
      if (targetListener != null) {
        removeTargetChildListener(path, targetListener);
      }
    }
  }

  @Override
  public void removeStateListener(StateListener listener) {
    stateListeners.remove(listener);
  }

  @Override
  public void addStateListener(StateListener listener) {
    stateListeners.add(listener);
  }

  protected void stateChanged(int state) {
    for (StateListener sessionListener : stateListeners) {
      sessionListener.stateChanged(state);
    }
  }

  @Override
  public void close() {
    if (closed) {
      return;
    }
    this.closed = true;
    try {
      doClose();
    } catch (Throwable throwable) {
      LOGGER.error("ZookeeperClient is close error: {}", throwable.getMessage());
    }
  }

  protected abstract void doClose();

  // 创建临时的连接
  protected abstract void createEphemeral(String path);

  // 创建永久的连接
  protected abstract void createPersistent(String path);

  // 创建一个新的观察者
  protected abstract TargetChildListener createTargetChildListener(String path,
      ChildListener listener);

  // 将一个新的观察者增加到指定的路径下
  protected abstract List<String> addTargetChildListener(String path, TargetChildListener listener);

  // 移除一个观察者
  protected abstract void removeTargetChildListener(String path, TargetChildListener listener);

}
