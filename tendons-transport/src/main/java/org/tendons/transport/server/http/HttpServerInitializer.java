package org.tendons.transport.server.http;

import org.tendons.common.protocol.RpcTransportInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.ReadTimeoutHandler;


/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月11日 上午12:08:27
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel>
    implements RpcTransportInitializer {

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    final ChannelPipeline channelPipeline = ch.pipeline();
    // 超时时间
    channelPipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(70));
    // 可以新增加密解密的逻辑
    // 编码与解码,
    channelPipeline.addLast("serverCodec", new HttpServerCodec());
    // 内容压缩
    channelPipeline.addLast("deflater", new HttpContentCompressor());
    // 自定义处理handler
    channelPipeline.addLast("HttpServerHandler", new HttpServerHandler());
  }
}
