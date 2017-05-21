package org.tendons.registry.loadbalance.impl;

import java.util.List;

import org.tendons.common.RequestWrapper;
import org.tendons.registry.loadbalance.AbstractLoadBalancer;
import org.tendons.registry.loadbalance.ServiceProvider;

/**
 * <pre>
 * 最小连接数算法
 * 最小连接数算法比较灵活和智能，由于后端服务器的配置不尽相同，对于请求的处理有快有慢，
 * 它是根据后端服务器当前的连接情况，动态地选取其中当前积压连接数最少的一台服务器来处理当前的请求，
 * 尽可能地提高后端服务的利用效率，将负责合理地分流到每一台服务器。<br>
 * 最小连接数算法比较灵活和智能，由于后端服务器的配置不尽相同，对于请求的处理有快有慢，
 * 它正是根据后端服务器当前的连接情况，动态地选取其中当前积压连接数最少的一台服务器来处理当前请求，
 * 尽可能地提高后端服务器的利用效率，将负载合理地分流到每一台机器。由于最小连接数设计服务器连接数的汇总和感知，
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午9:34:38
 */
public class LeastActiveLoadBalance extends AbstractLoadBalancer {

  @Override
  protected <T> ServiceProvider<T> doSelected(List<ServiceProvider<T>> serviceProviders,
      RequestWrapper request) {

    return null;
  }

}
