package org.tendons.registry;

/**
 * <pre>
 * 与服务注册中心的通知监听
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午2:02:38
 */
public interface NotifyListener {

  void notify(ServiceEntity serviceEntity, NotifyEvent event);

  enum NotifyEvent {
    CHILD_ADDED, CHILD_REMOVED
  }

}
