package org.tendons.transport.server;

import org.tendons.common.protocol.RpcTransportInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月11日 上午12:08:27 
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel>  implements RpcTransportInitializer {

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    
  }

}
