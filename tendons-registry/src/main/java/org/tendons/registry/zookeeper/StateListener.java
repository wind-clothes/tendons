package org.tendons.registry.zookeeper;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月29日 下午8:55:43
 */
public interface StateListener {
  int DISCONNECTED = 0;

  int CONNECTED = 1;

  int RECONNECTED = 2;

  void stateChanged(int connected);
}
