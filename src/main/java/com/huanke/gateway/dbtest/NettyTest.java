package com.huanke.gateway.dbtest;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * @author haoshijing
 * @version 2018年04月04日 09:31
 **/
public class NettyTest {
    public static void main(String[] args) {
        EventLoopGroup eventExecutors = new DefaultEventLoopGroup(8,new DefaultThreadFactory("TestThread"));

        eventExecutors.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("这这是一个测试");
            }
        });
    }
}
