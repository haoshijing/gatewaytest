package com.huanke.gateway.vert;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.impl.NetSocketInternal;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

/**
 * @author haoshijing
 * @version 2018年03月19日 10:05
 **/
public class NetWorkVerticle extends AbstractVerticle {
    private static  final int idleTimeout = 1000;
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start(startFuture);
        NetServerOptions opt = new NetServerOptions()
                .setTcpKeepAlive(true)
                .setIdleTimeout(idleTimeout) // in seconds; 0 means "don't timeout".
                .setPort(8077);


        NetServer netServer = vertx.createNetServer(opt);
        netServer.connectHandler(netSocket -> {
            NetSocketInternal netSocketInternal = (NetSocketInternal)netSocket;
            netSocket.handler(buffer -> {
            // Write the data straight back
            vertx.eventBus().publish("test",buffer.getBytes());
         });
        }).listen();
    }

}
