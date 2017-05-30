package org.tendons.monitor;

import org.tendons.common.service.RegisterServiceUrl;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月30日 下午3:43:23
 */
public interface MonitorFactory {

  Monitor getMonitor(RegisterServiceUrl serviceUrl);
}
