package org.tendons.transport.server;

import java.net.InetAddress;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tendons.common.server.RpcServer;
import org.tendons.common.service.ServiceWrapper;
import org.tendons.transport.config.TendonsRpcServerConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 通信层的服务器端
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月10日 下午11:00:58
 */
public class TendonsRpcServer implements RpcServer {

  private final static Logger LOGGER = LoggerFactory.getLogger(TendonsRpcServer.class);

  private EventLoopGroup bosserGroup;
  private NioEventLoopGroup workerGroup;
  private TendonsRpcServerConfig config;

  @Override
  public void start() throws Exception {
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    bosserGroup = new NioEventLoopGroup(
        availableProcessors * config.getBossEventLoopsAvailableProcessorsMultiple());
    workerGroup = new NioEventLoopGroup(
        availableProcessors * config.getWorkerEventLoopsAvailableProcessorsMultiple());
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    serverBootstrap.group(bosserGroup, workerGroup).channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, config.getBacklog())
        .childHandler(new HttpServerInitializer());// 这块后期需要拓展成可配置的，因为后期需要支持其他协议的
    Channel channel = serverBootstrap.bind(config.getPort()).sync().channel();
    LOGGER.info(String.format("httpServer is start ip:【%s】;port:【%s】",
        InetAddress.getLocalHost().getHostAddress(), config.getPort()));
    channel.closeFuture().sync();
  }

  @Override
  public void stop() {
    bosserGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
  }

  @Override
  public void publishAll() {
    // TODO Auto-generated method stub

  }

  @Override
  public void unpublish(ServiceWrapper serviceWrapper) {
    // TODO Auto-generated method stub

  }

  @Override
  public void unpublishAll() {
    // TODO Auto-generated method stub

  }

  @Override
  public List<ServiceWrapper> allRegisteredServices() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void publish(ServiceWrapper serviceWrapper) {
    // TODO Auto-generated method stub

  }

  @Override
  public void publish(ServiceWrapper... serviceWrappers) {
    // TODO Auto-generated method stub

  }

}
