package org.tendons.registry;

import org.tendons.common.service.RegisterServiceUrl;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月29日 下午8:21:11
 */
public interface RegistryCenterFactory {

  RegistryCenter getRegistryCenter(RegisterServiceUrl serviceUrl);
}
