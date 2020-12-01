package io.github.waterdev.mmoserv.authserver.networking;

import io.github.waterdev.mmoserv.authserver.AuthServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class AuthServerBootstrap {

    //handling of accepting and establishing client connections
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    //handling of established connections
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    ServerBootstrap bootstrap = new ServerBootstrap();
    ChannelFuture channelFuture;

    public void start(int port) throws InterruptedException {

        AuthServer.logger.info("Starting Server at Port " + port);

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new TCPChannelInitialzer())
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        channelFuture = bootstrap.bind(port).sync();

        if(channelFuture.isSuccess()) AuthServer.logger.info("Server started!");

    }

    public void stop() throws InterruptedException {

        //Waiting for all connections to close
        channelFuture.channel().closeFuture().sync();

        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();

    }

}
