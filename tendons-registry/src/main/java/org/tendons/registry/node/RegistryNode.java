package org.tendons.registry.node;

import org.tendons.common.service.RegisterServiceUrl;

/**
 * <pre>
 * 注册中心 - 节点服务接口
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月30日 下午3:11:27
 */
public interface RegistryNode {

  RegisterServiceUrl getUrl();

  boolean isAvailable();

  void destroy();
}
