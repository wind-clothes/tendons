package org.tendons.common.codec;


import java.io.IOException;

import org.tendons.common.serialization.RpcSerializer;
import org.tendons.common.transport.channel.Channel;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年6月11日 下午2:57:13
 */
public abstract class TendonsRpcAbstractCodec implements TendonsRpcCodec {

  @Override
  public byte[] encode(Channel channel, Object message) throws IOException {
    return doEncode(channel, message, loadRpcSerializer());
  }

  @Override
  public Object decode(Channel channel, byte[] buffer) throws IOException {
    return doDencode(channel, buffer, loadRpcSerializer());
  }

  protected abstract byte[] doEncode(Channel channel, Object message, RpcSerializer serializer);

  protected abstract Object doDencode(Channel channel, byte[] buffer, RpcSerializer serializer);

  protected abstract RpcSerializer loadRpcSerializer();
}
