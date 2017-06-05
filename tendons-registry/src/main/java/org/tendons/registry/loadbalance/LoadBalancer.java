package org.tendons.registry.loadbalance;

import java.util.List;

import org.tendons.common.extension.SPI;
import org.tendons.common.request.RequestWrapper;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午1:48:05
 */
@SPI
public interface LoadBalancer {

  <T> ServiceProvider<T> selected(List<ServiceProvider<T>> serviceProviders, RequestWrapper request);

}
