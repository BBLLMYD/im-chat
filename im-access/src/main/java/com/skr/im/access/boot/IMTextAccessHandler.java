package com.skr.im.access.boot;

import com.skr.im.access.enumz.UserActionEnum;
import com.skr.im.access.event.EventGenerator;
import com.skr.im.access.utils.SpringContextHolder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author : mengqingwen 
 * create at:  2020-07-15
 * @description:
 * */
@Slf4j
@ChannelHandler.Sharable
@Component
public class IMTextAccessHandler  extends SimpleChannelInboundHandler<TextWebSocketFrame>{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间" + LocalDateTime.now() + " " + msg.text()));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        String longText = ctx.channel().id().asLongText();
        ApplicationEvent event = EventGenerator.buildEvent(UserActionEnum.GO_ONLINE, longText, ctx.channel());
        SpringContextHolder.publishEvent(event);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String longText = ctx.channel().id().asLongText();
        ApplicationEvent event = EventGenerator.buildEvent(UserActionEnum.GO_OFFLINE, longText, ctx.channel());
        SpringContextHolder.publishEvent(event);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}