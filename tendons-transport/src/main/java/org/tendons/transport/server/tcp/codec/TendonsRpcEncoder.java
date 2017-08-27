package org.tendons.transport.server.tcp.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tendons.common.codec.TendonsRpcCodec;
import org.tendons.common.transport.channel.Channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
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

  private final TendonsRpcCodec codec;
  private final Channel channel;

  public TendonsRpcEncoder(TendonsRpcCodec codec, Channel channel) {
    this.codec = codec;
    this.channel = channel;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
    try {
      // TODO
      final byte[] bytes = codec.encode(channel, msg);
    } catch (Exception e) {
      LOGGERE.warn("TendonsRpcEncoder decode is error {}", e.getMessage());
      throw e;
    } finally {
      // TODO i need do everything
    }
  }

}
