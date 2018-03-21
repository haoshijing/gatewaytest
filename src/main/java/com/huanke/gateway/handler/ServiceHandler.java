package com.huanke.gateway.handler;

import com.huanke.gateway.protocol.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author haoshijing
 * @version 2018年03月13日 11:31
 **/
@Slf4j
public class ServiceHandler extends ChannelInboundHandlerAdapter {

    static ConcurrentHashMap<String,Channel> channelConcurrentHashMap = new ConcurrentHashMap<String, Channel>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel id {} connected ",ctx.channel().id());
        channelConcurrentHashMap.putIfAbsent(ctx.channel().id().asLongText(),ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelConcurrentHashMap.remove(ctx.channel().id().asLongText());
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MqttConnectMessage packet = (MqttConnectMessage)msg;
        log.info("packet.msg = {},channelId = {}", packet.payload(),ctx.channel().id().asLongText());
        ctx.writeAndFlush(msg);
    }
}
