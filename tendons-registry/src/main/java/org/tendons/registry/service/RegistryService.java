package org.tendons.registry.service;

import java.util.List;

import org.tendons.common.service.RegisterServiceUrl;

/**
 * <pre>
 * 注册中心 - 服务接口
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月29日 下午8:04:56
 */
public interface RegistryService {

  public void register(RegisterServiceUrl serviceUrl);

  public void unregister(RegisterServiceUrl serviceUrl);

  void subscribe(RegisterServiceUrl serviceUrl, NotifyListener listener);

  void unsubscribe(RegisterServiceUrl serviceUrl, NotifyListener listener);

  List<RegisterServiceUrl> lookup(RegisterServiceUrl serviceUrl);
}
