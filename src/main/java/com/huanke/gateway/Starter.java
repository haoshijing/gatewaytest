package com.huanke.gateway;

import com.huanke.gateway.codec.NettyDecoder;
import com.huanke.gateway.codec.NettyEncoder;
import com.huanke.gateway.handler.ServiceHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author haoshijing
 * @version 2018年03月13日 11:20
 **/
@SpringBootApplication
@EnableScheduling
public class Starter {

    public static  void initNetty() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1,new DefaultThreadFactory("BossThread"));

        NioEventLoopGroup workGroup = new NioEventLoopGroup(4,new DefaultThreadFactory("WorkThread"));

        EventExecutorGroup handlerExecutorGroup =new DefaultEventExecutorGroup(4,new DefaultThreadFactory("handlerWorkThread"));

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup);
        serverBootstrap.channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new NettyDecoder())
                                .addLast(new NettyEncoder())
                                .addLast(new ServiceHandler());
                    }
                });
        ChannelFuture sync = serverBootstrap.bind(8077).sync();
    }

    public static void main(String[] args) {
        try {
            initNetty();
        }catch (Exception e){

        }
        SpringApplication.run(Starter.class, args);
    }
}
