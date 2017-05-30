package org.tendons.registry.zookeeper;

import java.util.List;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月29日 下午8:55:04
 */
public interface ChildListener {

  void childChanged(String path, List<String> children);
}
