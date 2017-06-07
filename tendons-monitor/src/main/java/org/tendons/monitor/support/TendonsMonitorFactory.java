package org.tendons.monitor.support;

import org.tendons.common.service.RegisterServiceUrl;
import org.tendons.monitor.AbstractMonitorFactory;
import org.tendons.monitor.Monitor;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月30日 下午3:45:26 
 */
@Deprecated
public class TendonsMonitorFactory extends AbstractMonitorFactory {

  @Override
  protected Monitor createMonitor(RegisterServiceUrl url) {
    return null;
  }

}
