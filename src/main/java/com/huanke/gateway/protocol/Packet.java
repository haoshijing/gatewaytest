package com.huanke.gateway.protocol;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

/**
 * @author haoshijing
 * @version 2018年03月13日 11:00
 **/
public class Packet {

    @Getter
    @Setter
    private Integer cmd;
    @Getter
    @Setter
    private String msg;

    public static  Packet decode(ByteBuffer byteBuffer){
        Packet packet = new Packet();
        int limit = byteBuffer.limit();
        int cmd = byteBuffer.getInt();
        byte[] datas = new byte[limit];

        byteBuffer.get(datas);
        packet.msg = new String(datas);
        return packet;
    }

}
