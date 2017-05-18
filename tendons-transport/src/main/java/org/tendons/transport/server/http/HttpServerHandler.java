package org.tendons.transport.server.http;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpResponseStatus.TEMPORARY_REDIRECT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tendons.common.constants.Constants;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

/**
 * http 协议-处理可读数据
 * 
 * @author: chengweixiong@uworks.cc
 * @param <I>
 * @date: 2017年5月11日 下午11:35:09
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpRequest> {

  private final static Logger LOGGER = LoggerFactory.getLogger(HttpServerHandler.class);

  public HttpServerHandler() {
    super();
    if (LOGGER.isDebugEnabled()) {
      LOGGER.info("初始化==========HttpServerHandler===========END");
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
    ctx.channel().close();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
    final boolean keepAlive = HttpUtil.isKeepAlive(msg);
    if (HttpUtil.is100ContinueExpected(msg)) {
      processResponse(keepAlive,
          new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE), ctx);
      return;
    }
    try {
      if (msg.decoderResult().isFailure()) {
        buildReponseFaiure(ctx, BAD_REQUEST);
        return;
      }
      final Object result = null;
      // 解析请求地址和参数并绑定到具体的Service中 TODO
      buildReponseSuccess(ctx, keepAlive, result);
    } catch (Exception e) {
      LOGGER.info("HttpServerHandler is error,{}", e.getMessage());
      buildReponseFaiure(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
    }
    if (LOGGER.isDebugEnabled()) {
      LOGGER.info("HttpServerHandler-channelRead0 is end");
    }
  }

  private void buildReponseSuccess(ChannelHandlerContext ctx, boolean keepAlive, Object result) {
    final FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, OK,
        Unpooled.copiedBuffer(buildReponseEntity(result)));
    buildReponseHeaders(keepAlive, response);
    processResponse(keepAlive, response, ctx);
  }


  private void buildReponseHeaders(boolean keepAlive, FullHttpResponse response) {
    if (!response.status().equals(TEMPORARY_REDIRECT)) {
      response.headers().set(HttpHeaderNames.CONTENT_TYPE, Constants.APPLICATION_JSON);
      response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
    }
    if (keepAlive) {
      response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
    }
    response.headers().set(HttpHeaderNames.DATE, System.currentTimeMillis());
  }


  private void buildReponseFaiure(ChannelHandlerContext ctx, HttpResponseStatus statusCode) {
    FullHttpResponse response = null;
    switch (statusCode.code()) {
      case 400:
        response = new DefaultFullHttpResponse(HTTP_1_1, statusCode,
            Unpooled.copiedBuffer(buildReponseEntity("params is error")));
        break;
      case 404:
        response = new DefaultFullHttpResponse(HTTP_1_1, statusCode,
            Unpooled.copiedBuffer(buildReponseEntity("resources is not found")));
        break;
      case 500:
        response = new DefaultFullHttpResponse(HTTP_1_1, statusCode,
            Unpooled.copiedBuffer(buildReponseEntity("server is error")));
        break;
      default:
        break;
    }
    if (response != null) {
      processResponse(false, response, ctx);
    }
  }

  /**
   * <pre>
   *  构建各种格式的响应数据：JSON,XML,...
   * </pre>
   * 
   * @Title: buildReponseEntity
   * @param object
   * @return byte[]
   */
  private byte[] buildReponseEntity(Object object) {
    return null;
  }

  public void writeResponseOnRedirect(ChannelHandlerContext ctx, String redirectURL) {
    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, (TEMPORARY_REDIRECT));
    response.headers().set(HttpHeaderNames.LOCATION, redirectURL);
    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
  }

  private void processResponse(boolean keepAlive, FullHttpResponse response,
      ChannelHandlerContext ctx) {
    if (!keepAlive) {
      ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    } else {
      ctx.writeAndFlush(response);
    }
  }
}
