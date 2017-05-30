package org.tendons.registry;

import org.tendons.registry.node.RegistryNode;
import org.tendons.registry.service.RegistryService;

/**
 * <pre>
 * 注册中心 - 接口
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月29日 下午7:55:29
 */
public interface RegistryCenter extends RegistryService, RegistryNode {

  void connection(RegistryCenterConfig centerConfig);

  void close();
}
