package org.tendons.transport.server.tcp.codec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tendons.common.codec.TendonsRpcCodec;
import org.tendons.common.transport.channel.Channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
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

    private final TendonsRpcCodec codec;
    private final Channel channel;

    public TendonsRpcDecoder(TendonsRpcCodec codec, Channel channel) {
        this.codec = codec;
        this.channel = channel;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
        throws Exception {
        try {
            final Object object = codec.decode(channel, ByteBufUtil.getBytes(in));
            out.add(object);
        } catch (Exception e) {
            LOGGERE.warn("TendonsRpcDecoder decode is error {}", e.getMessage());
            throw e;
        } finally {
            // TODO i need do everything
        }
    }

}
