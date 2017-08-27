package org.tendons.monitor;

import java.io.Serializable;

import org.tendons.common.service.RegisterServiceUrl;

/**
 * <pre>
 * 监视的统计信息
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月30日 下午3:22:53
 */
public class ServiceStatistics implements Serializable {

  private static final long serialVersionUID = 1742820121266415658L;

  private RegisterServiceUrl serviceUrl;

  private String application;

  private String service;

  private String method;

  private String group;

  private String version;

  private String client;

  private String server;

  public ServiceStatistics(RegisterServiceUrl serviceUrl) {
    this.serviceUrl = serviceUrl;
    this.application = serviceUrl.getParameter(Contants.APPLICATION);
    this.service = serviceUrl.getParameter(Contants.INTERFACE);
    this.method = serviceUrl.getParameter(Contants.METHOD);
    this.group = serviceUrl.getParameter(Contants.GROUP);
    this.version = serviceUrl.getParameter(Contants.VERSION);
    this.client = serviceUrl.getParameter(Contants.CONSUMER, serviceUrl.getAddress());
    this.server = serviceUrl.getParameter(Contants.PROVIDER, serviceUrl.getAddress());
  }

  public RegisterServiceUrl getUrl() {
    return serviceUrl;
  }

  public void setUrl(RegisterServiceUrl serviceUrl) {
    this.serviceUrl = serviceUrl;
  }

  public String getApplication() {
    return application;
  }

  public ServiceStatistics setApplication(String application) {
    this.application = application;
    return this;
  }

  public String getService() {
    return service;
  }

  public ServiceStatistics setService(String service) {
    this.service = service;
    return this;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getMethod() {
    return method;
  }

  public ServiceStatistics setMethod(String method) {
    this.method = method;
    return this;
  }

  public String getClient() {
    return client;
  }

  public ServiceStatistics setClient(String client) {
    this.client = client;
    return this;
  }

  public String getServer() {
    return server;
  }

  public ServiceStatistics setServer(String server) {
    this.server = server;
    return this;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((application == null) ? 0 : application.hashCode());
    result = prime * result + ((client == null) ? 0 : client.hashCode());
    result = prime * result + ((group == null) ? 0 : group.hashCode());
    result = prime * result + ((method == null) ? 0 : method.hashCode());
    result = prime * result + ((server == null) ? 0 : server.hashCode());
    result = prime * result + ((service == null) ? 0 : service.hashCode());
    result = prime * result + ((version == null) ? 0 : version.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ServiceStatistics other = (ServiceStatistics) obj;
    if (application == null) {
      if (other.application != null)
        return false;
    } else if (!application.equals(other.application))
      return false;
    if (client == null) {
      if (other.client != null)
        return false;
    } else if (!client.equals(other.client))
      return false;
    if (group == null) {
      if (other.group != null)
        return false;
    } else if (!group.equals(other.group))
      return false;
    if (method == null) {
      if (other.method != null)
        return false;
    } else if (!method.equals(other.method))
      return false;
    if (server == null) {
      if (other.server != null)
        return false;
    } else if (!server.equals(other.server))
      return false;
    if (service == null) {
      if (other.service != null)
        return false;
    } else if (!service.equals(other.service))
      return false;
    if (version == null) {
      if (other.version != null)
        return false;
    } else if (!version.equals(other.version))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return serviceUrl.toString();
  }
}
