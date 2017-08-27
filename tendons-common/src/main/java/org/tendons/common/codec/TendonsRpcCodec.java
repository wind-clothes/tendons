package org.tendons.common.codec;

import java.io.IOException;

import org.tendons.common.extension.SPI;
import org.tendons.common.transport.channel.Channel;

/**
 * <pre>
 * 为了支持不同的编解码协议的公共接口
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年6月11日 下午2:51:09
 */
@SPI("codec")
public interface TendonsRpcCodec {

  byte[] encode(Channel channel, Object message) throws IOException;

  Object decode(Channel channel, byte[] buffer) throws IOException;

}
