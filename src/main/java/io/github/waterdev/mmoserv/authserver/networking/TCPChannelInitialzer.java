package io.github.waterdev.mmoserv.authserver.networking;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TCPChannelInitialzer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(new StringEncoder());
        socketChannel.pipeline().addLast(new StringDecoder());
        socketChannel.pipeline().addLast(new TCPChannelHandler());
    }
}