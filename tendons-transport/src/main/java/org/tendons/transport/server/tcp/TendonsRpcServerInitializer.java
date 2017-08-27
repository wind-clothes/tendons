package org.tendons.transport.server.tcp;

import org.tendons.common.protocol.RpcTransportInitializer;
import org.tendons.transport.server.tcp.codec.TendonsRpcCodecAdapter;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author: xcw
 * @date:2017年5月23日 下午8:18:19
 */
public class TendonsRpcServerInitializer extends ChannelInitializer<SocketChannel>
    implements RpcTransportInitializer {

    private final TendonsRpcCodecAdapter codecAdapter;

    public TendonsRpcServerInitializer(TendonsRpcCodecAdapter codecAdapter) {
        this.codecAdapter = codecAdapter;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        final ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("rpc-timeout", new IdleStateHandler(0, 0, 1000));
        pipeline.addLast("rpc-codecAdapter-decoder", codecAdapter.getRpcDecoder());
        pipeline.addLast("rpc-codecAdapter-encoder", codecAdapter.getRpcEncoder());
        pipeline.addLast("tendonsRpcServerHandler", new TendonsRpcServerHandler());
    }

}
