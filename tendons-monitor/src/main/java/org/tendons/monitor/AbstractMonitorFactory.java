package org.tendons.monitor;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.tendons.common.constants.Constants;
import org.tendons.common.service.RegisterServiceUrl;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月30日 下午3:44:07
 */
public abstract class AbstractMonitorFactory implements MonitorFactory {

  private static final ReentrantLock LOCK = new ReentrantLock();

  /**
   * 一个服务一个监视器
   */
  private static final Map<String, Monitor> MONITORS = new ConcurrentHashMap<String, Monitor>();

  public static Collection<Monitor> getMonitors() {
    return Collections.unmodifiableCollection(MONITORS.values());
  }

  @Override
  public Monitor getMonitor(RegisterServiceUrl url) {
    url = url.setPath(MonitorService.class.getName()).addParameter(Constants.INTERFACE_KEY,
        MonitorService.class.getName());
    String key = url.toServiceString();
    LOCK.lock();
    try {
      Monitor monitor = MONITORS.get(key);
      if (monitor != null) {
        return monitor;
      }
      monitor = createMonitor(url);
      if (monitor == null) {
        throw new IllegalStateException("Can not create monitor " + url);
      }
      MONITORS.put(key, monitor);
      return monitor;
    } finally {
      // 释放锁
      LOCK.unlock();
    }
  }

  protected abstract Monitor createMonitor(RegisterServiceUrl url);
}
