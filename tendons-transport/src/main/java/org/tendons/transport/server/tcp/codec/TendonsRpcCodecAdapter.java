package org.tendons.transport.server.tcp.codec;

import org.tendons.common.codec.TendonsRpcCodec;
import org.tendons.common.server.RpcServer;
import org.tendons.common.transport.channel.Channel;

/**
 * <pre>
 * 编解码的适配器
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月29日 下午4:25:13
 */
public class TendonsRpcCodecAdapter {

  private final TendonsRpcDecoder rpcDecoder;

  private final TendonsRpcEncoder rpcEncoder;

  private final RpcServer server;

  public TendonsRpcCodecAdapter(TendonsRpcCodec codec, RpcServer server) {
    this.server = server;
    final Channel channel = null;
    this.rpcDecoder = new TendonsRpcDecoder(codec, channel);
    this.rpcEncoder = new TendonsRpcEncoder(codec, channel);
  }

  public TendonsRpcDecoder getRpcDecoder() {
    return rpcDecoder;
  }

  public TendonsRpcEncoder getRpcEncoder() {
    return rpcEncoder;
  }

}
