package org.tendons.transport.server.tcp.codec;

/**
 * <pre>
 * TODO
 * </pre>
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月29日 下午4:25:13
 */
public class TendonsRpcCodecAdapter {

  private final TendonsRpcDecoder rpcDecoder = new TendonsRpcDecoder();
  private final TendonsRpcEncoder rpcEncoder = new TendonsRpcEncoder();

  public TendonsRpcCodecAdapter() {
    super();
  }

  public TendonsRpcDecoder getRpcDecoder() {
    return rpcDecoder;
  }

  public TendonsRpcEncoder getRpcEncoder() {
    return rpcEncoder;
  }

}
