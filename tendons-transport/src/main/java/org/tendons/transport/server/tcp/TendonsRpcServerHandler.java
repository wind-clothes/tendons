package org.tendons.transport.server.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author: xcw
 * @date:2017年5月23日 下午8:23:11 
 */
public class TendonsRpcServerHandler extends SimpleChannelInboundHandler<Object>{

  private static final Logger LOGGER = LoggerFactory.getLogger(TendonsRpcServerHandler.class);

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
    LOGGER.info("start");
  }

}
