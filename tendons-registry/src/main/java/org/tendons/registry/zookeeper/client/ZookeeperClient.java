package org.tendons.registry.zookeeper.client;

import java.util.List;

import org.tendons.registry.zookeeper.ChildListener;
import org.tendons.registry.zookeeper.StateListener;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月29日 下午8:54:23
 */
public interface ZookeeperClient {

  void create(String path, boolean ephemeral);

  void delete(String path);

  List<String> getChildren(String path);

  List<String> addChildListener(String path, ChildListener listener);

  void removeChildListener(String path, ChildListener listener);

  void addStateListener(StateListener listener);

  void removeStateListener(StateListener listener);

  boolean isConnected();

  void close();

}
