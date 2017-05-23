package org.tendons.transport.server.tcp;

import org.tendons.common.protocol.RpcTransportInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author: xcw
 * @date:2017年5月23日 下午8:18:19
 */
public class TendonsRpcServerInitializer extends ChannelInitializer<SocketChannel> implements RpcTransportInitializer {

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    final ChannelPipeline pipeline = ch.pipeline();
    // pipeline.addLast(name, handler);
    pipeline.addLast("tendonsRpcServerHandler", new TendonsRpcServerHandler());
  }

}
