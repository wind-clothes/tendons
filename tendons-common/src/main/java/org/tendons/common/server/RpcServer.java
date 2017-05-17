package org.tendons.common.server;

import java.util.List;

import org.tendons.common.service.ServiceWrapper;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月10日 下午11:04:09
 */
public interface RpcServer {
  /**
   * 开启服务器
   * 
   * @return void
   */
  void start() throws Exception;

  /**
   * 停止服务
   * 
   * @return void
   */
  void stop();

  /**
   * 发布本地所有服务到注册中心.
   */
  void publishAll();

  /**
   * 从注册中心把指定服务下线.
   */
  void unpublish(ServiceWrapper serviceWrapper);

  /**
   * 从注册中心把本地所有服务全部下线.
   */
  void unpublishAll();

  /**
   * 注册所有服务到本地容器.
   */
  List<ServiceWrapper> allRegisteredServices();

  /**
   * 发布指定服务到注册中心.
   */
  void publish(ServiceWrapper serviceWrapper);

  /**
   * 发布指定服务列表到注册中心.
   */
  void publish(ServiceWrapper... serviceWrappers);
}
