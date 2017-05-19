package org.tendons.transport.server.tcp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tendons.common.server.RpcServer;
import org.tendons.common.service.ServiceWrapper;

/**
 * @author: xiongchengwei
 * @date:2017年5月19日 上午10:59:51 
 */
public class TendonsRpcServer implements RpcServer {

  private static final Logger LOGGER = LoggerFactory.getLogger(TendonsRpcServer.class);

  /* (non-Javadoc) 
   * @throws Exception 
   * @see org.tendons.common.server.RpcServer#start() 
   */
  @Override
  public void start() throws Exception {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc) 
   *  
   * @see org.tendons.common.server.RpcServer#stop() 
   */
  @Override
  public void stop() {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc) 
   *  
   * @see org.tendons.common.server.RpcServer#publishAll() 
   */
  @Override
  public void publishAll() {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc) 
   * @param serviceWrapper 
   * @see org.tendons.common.server.RpcServer#unpublish(org.tendons.common.service.ServiceWrapper) 
   */
  @Override
  public void unpublish(ServiceWrapper serviceWrapper) {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc) 
   *  
   * @see org.tendons.common.server.RpcServer#unpublishAll() 
   */
  @Override
  public void unpublishAll() {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc) 
   * @return 
   * @see org.tendons.common.server.RpcServer#allRegisteredServices() 
   */
  @Override
  public List<ServiceWrapper> allRegisteredServices() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc) 
   * @param serviceWrapper 
   * @see org.tendons.common.server.RpcServer#publish(org.tendons.common.service.ServiceWrapper) 
   */
  @Override
  public void publish(ServiceWrapper serviceWrapper) {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc) 
   * @param serviceWrappers 
   * @see org.tendons.common.server.RpcServer#publish(org.tendons.common.service.ServiceWrapper[]) 
   */
  @Override
  public void publish(ServiceWrapper... serviceWrappers) {
    // TODO Auto-generated method stub

  }

}
