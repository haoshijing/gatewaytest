package com.huanke.gateway.codec;

import com.huanke.gateway.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteBuffer;

/**
 * @author haoshijing
 * @version 2018年03月13日 10:46
 **/
public class NettyDecoder extends LengthFieldBasedFrameDecoder {
    public NettyDecoder(){
        super( 65535,  0, 4,  0, 4);
    }

    @Override
     protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
         ByteBuf frame = null;
         try {
             frame = (ByteBuf) super.decode(ctx, in);
             if (null == frame) {
                 return null;
             }

             ByteBuffer byteBuffer = frame.nioBuffer();

             return Packet.decode(byteBuffer);
         } catch (Exception e) {

         } finally {
             if (null != frame) {
                 frame.release();
             }
         }

         return null;
     }
}
