package org.tendons.registry;

import java.util.List;

import org.tendons.common.service.RegisterServiceUrl;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月30日 下午3:17:41
 */
public abstract class FailbackRegistry extends AbstractRegistry {

  @Override
  public void register(RegisterServiceUrl serviceUrl) {

  }

  @Override
  public void unregister(RegisterServiceUrl serviceUrl) {

  }

  @Override
  public void subscribe(RegisterServiceUrl serviceUrl, NotifyListener listener) {

  }

  @Override
  public void unsubscribe(RegisterServiceUrl serviceUrl, NotifyListener listener) {

  }

  @Override
  public List<RegisterServiceUrl> lookup(RegisterServiceUrl serviceUrl) {
    return null;
  }

  @Override
  public RegisterServiceUrl getUrl() {
    return null;
  }

  @Override
  public boolean isAvailable() {
    return false;
  }

  @Override
  public void destroy() {

  }

  @Override
  protected void doConnection(RegistryCenterConfig centerConfig) {

  }

  @Override
  protected void doClose() {

  }

}
