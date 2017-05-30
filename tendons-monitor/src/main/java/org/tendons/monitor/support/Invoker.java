package org.tendons.monitor.support;

import org.tendons.common.service.RegisterServiceUrl;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月30日 下午3:51:42 
 */
public interface Invoker<T> {

  RegisterServiceUrl getUrl();

  boolean isAvailable();

  void destroy();

}
