package org.tendons.transport.server.tcp;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tendons.common.config.TendonsTcpServerConfig;
import org.tendons.common.server.AbstractServer;
import org.tendons.common.util.concurrrent.DefineThreadFactory;
import org.tendons.transport.server.tcp.codec.TendonsRpcCodecAdapter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author: xiongchengwei
 * @date:2017年5月19日 上午10:59:51
 */
public class TendonsRpcServer extends AbstractServer {

  private static final Logger LOGGER = LoggerFactory.getLogger(TendonsRpcServer.class);

  private EventLoopGroup bossGroup;
  private EventLoopGroup workerGroup;
  private NettyChannelManager channelManager;

  public TendonsRpcServer() {
    this(new TendonsTcpServerConfig());
  }

  public TendonsRpcServer(TendonsTcpServerConfig config) {
    super(config);
    this.channelManager = new NettyChannelManager();
  }

  @Override
  public void doStart() throws Throwable {
    bossGroup = new NioEventLoopGroup(config.getBossEventLoopsAvailableProcessorsMultiple(),
        new DefineThreadFactory("TENDONS-TCP-BOSS"));

    workerGroup = new NioEventLoopGroup(config.getWorkerEventLoopsAvailableProcessorsMultiple(),
        new DefineThreadFactory("TENDONS-TCP-WORKER"));

    final TendonsRpcCodecAdapter adapter =
        new TendonsRpcCodecAdapter(getCodec(), TendonsRpcServer.this);

    final ServerBootstrap bootstrap = new ServerBootstrap();

    bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
        .childHandler(new TendonsRpcServerInitializer(adapter));
    bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeOut())
        .option(ChannelOption.SO_BACKLOG, config.getBacklog())
        .option(ChannelOption.SO_REUSEADDR, true) // disable this on windows, open it on linux
        .option(ChannelOption.TCP_NODELAY, config.isTcpNnodelay())
        .childOption(ChannelOption.SO_KEEPALIVE, config.isKeepLive())
        .option(ChannelOption.ALLOCATOR, new UnpooledByteBufAllocator(false))// 是否开启读写缓冲区大小的动态调整
        .childOption(ChannelOption.SO_RCVBUF, 8192 * 128)// 读入的缓存默认大小
        .childOption(ChannelOption.SO_SNDBUF, 8192 * 128);// 写出的缓存的默认大小

    final ChannelFuture channelFuture =
        bootstrap.bind(new InetSocketAddress(config.getPort())).sync();
    final Channel channel = channelFuture.channel();

    channel.closeFuture().sync();
    LOGGER.info("tendons rpc tcp server is start");
  }

  @Override
  public void doStop() throws Throwable {
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
    LOGGER.info("tendons rpc tcp server is down end");
  }

}
