package org.tendons.transport.server.tcp.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * <pre>
 * TCP的编码器 TODO
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月29日 下午4:28:45
 */
public class TendonsRpcEncoder extends MessageToByteEncoder<Object> {

  private static final Logger LOGGERE = LoggerFactory.getLogger(TendonsRpcEncoder.class);

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

  }

}
