package com.huanke.gateway;

import com.huanke.gateway.codec.NettyDecoder;
import com.huanke.gateway.codec.NettyEncoder;
import com.huanke.gateway.handler.ServiceHandler;
import com.huanke.gateway.protocol.Packet;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author haoshijing
 * @version 2018年03月13日 11:20
 **/
@SpringBootApplication
@EnableScheduling
@Slf4j
public class Starter {

    public static void initVert() {
        Vertx vertx = Vertx.vertx(new VertxOptions());
        vertx.createSharedWorkerExecutor("HandlerWork", 20);
        NetServer server = vertx.createNetServer(
                new NetServerOptions().setPort(8077).setHost("localhost")
        );
        vertx.eventBus().consumer("test",(data)->{
            byte[] datas = (byte[])data.body();
            try {
                log.info("datas.length = " + (datas != null ? datas.length : 0));
                Thread.sleep(3000);
            }catch (Exception e){

            }

        });
        server.connectHandler(sock -> {
            sock.handler(buffer -> {
                // Write the data straight back
                vertx.eventBus().publish("test",buffer.getBytes());
            });
        }).listen();

    }

    public static void initNetty() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("BossThread"));

        NioEventLoopGroup workGroup = new NioEventLoopGroup(4, new DefaultThreadFactory("WorkThread"));

        EventExecutorGroup handlerExecutorGroup = new DefaultEventExecutorGroup(4, new DefaultThreadFactory("handlerWorkThread"));

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup);
        serverBootstrap.channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        MqttEncoder mqttEncoder = MqttEncoder.INSTANCE;
                        pipeline.addLast(new MqttDecoder())
                                .addLast(mqttEncoder)
                                .addLast(handlerExecutorGroup, new ServiceHandler());
                    }
                });
        ChannelFuture sync = serverBootstrap.bind(8077).sync();
    }

    public static void main(String[] args) {
        try {
            initNetty();
        } catch (Exception e) {

        }
        SpringApplication.run(Starter.class, args);
    }
}
