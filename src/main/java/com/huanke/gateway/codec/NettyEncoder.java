package com.huanke.gateway.codec;

import com.huanke.gateway.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author haoshijing
 * @version 2018年03月13日 10:46
 **/
public class NettyEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        int length = 4 + packet.getMsg().getBytes().length;
        byteBuf.writeInt(length);
        byteBuf.writeBytes(packet.getMsg().getBytes());
    }
}
