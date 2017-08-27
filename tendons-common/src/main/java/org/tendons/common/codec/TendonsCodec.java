package org.tendons.common.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tendons.common.serialization.RpcSerializer;
import org.tendons.common.transport.channel.Channel;

/**
 * <pre>
 * tendons自身的编解码器，如果需要扩展新的编解码器，请继承{@link TendonsRpcAbstractCodec}进行扩展
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年6月11日 下午3:03:03
 */
public class TendonsCodec extends TendonsRpcAbstractCodec {

  private static final Logger LOGGER = LoggerFactory.getLogger(TendonsCodec.class);

  @Override
  protected byte[] doEncode(Channel channel, Object message, RpcSerializer serializer) {
    return null;
  }

  @Override
  protected Object doDencode(Channel channel, byte[] buffer, RpcSerializer serializer) {
    return null;
  }

  @Override
  protected RpcSerializer loadRpcSerializer() {
    return null;
  }
}
