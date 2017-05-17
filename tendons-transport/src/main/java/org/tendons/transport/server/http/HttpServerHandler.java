package org.tendons.transport.server.http;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpResponseStatus.TEMPORARY_REDIRECT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.Calendar;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tendons.common.constants.Constants;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

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
    // TODO 执行相关请求,重构响应数据
    try {
      final Object result = null;

      buildReponseSuccess(ctx, keepAlive, msg, result);
    } catch (Exception e) {
      buildReponseFaiure(ctx, msg);
    }
    if (LOGGER.isDebugEnabled()) {
      LOGGER.info("HttpServerHandler-channelRead0 is end");
    }
  }

  private void buildReponseSuccess(ChannelHandlerContext ctx, boolean keepAlive, HttpRequest msg,
      Object result) {
    final FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
        msg.decoderResult().isSuccess() ? OK : BAD_REQUEST,
        Unpooled.copiedBuffer(Objects.toString(result), CharsetUtil.UTF_8));
    buildReponseHeaders(keepAlive, response);
    processResponse(keepAlive, response, ctx);
  }

  public void writeResponseOnRedirect(ChannelHandlerContext ctx, String redirectURL) {
    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, (TEMPORARY_REDIRECT));
    response.headers().set(HttpHeaderNames.LOCATION, redirectURL);
    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
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


  private void buildReponseFaiure(ChannelHandlerContext ctx, HttpRequest msg) {
    FullHttpResponse response;
    if (!msg.decoderResult().isSuccess()) {
      response = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST,
          Unpooled.copiedBuffer("params is error", CharsetUtil.UTF_8));
    } else {
      response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND,
          Unpooled.copiedBuffer("resources is not found", CharsetUtil.UTF_8));
    }
    processResponse(false, response, ctx);
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
