package org.tendons.transport.server.tcp.codec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * <pre>
 * TCP的解码器 TODO
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月29日 下午4:25:13
 */
public class TendonsRpcDecoder extends ByteToMessageDecoder {

  private static final Logger LOGGERE = LoggerFactory.getLogger(TendonsRpcDecoder.class);

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

  }

}
