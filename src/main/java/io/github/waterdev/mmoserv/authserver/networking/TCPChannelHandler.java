package io.github.waterdev.mmoserv.authserver.networking;

import io.github.waterdev.mmoserv.authserver.AuthServer;
import io.github.waterdev.mmoserv.authserver.MySQL.AccDB;
import io.github.waterdev.mmoserv.authserver.lib.Utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.UUID;

public class TCPChannelHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        AuthServer.logger.info(ctx.channel().remoteAddress() + ": Channel Active");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        AuthServer.logger.info(ctx.channel().remoteAddress() + ": " + msg);
        String[] args = msg.split(" ");
        if(args[0].equalsIgnoreCase("register") && args.length == 4) {

            int result = AuthServer.accDB.createUser(args[1], args[2], Utils.stringToSHA265(args[3] + AuthServer.mySQLConfig.getSalt()), false);

            switch (result) {
                case 1:
                    ctx.channel().writeAndFlush("success");
                    break;
                case AccDB.EMAIL_EXISTS:
                    ctx.channel().writeAndFlush("error_email_used");
                    break;
                case AccDB.NAME_EXISTS:
                    ctx.channel().writeAndFlush("error_name_used");
                    break;
                default:
                    ctx.channel().writeAndFlush("error_unknown");
            }
        } else if(args[0].equalsIgnoreCase("login") && args.length == 3) {

            int result = AuthServer.accDB.checkUser(args[1], Utils.stringToSHA265(args[2] + AuthServer.mySQLConfig.getSalt()));

            switch (result) {
                case AccDB.USER_ACCOUNT:
                    UUID sessionToken = UUID.randomUUID();
                    ctx.channel().writeAndFlush("login_user " + sessionToken + "");
                    AuthServer.sharedDB.setValue(args[1], Utils.gm_key);
                    break;
                case AccDB.GM_ACCOUNT:
                    AuthServer.sharedDB.setValue(args[1], Utils.gm_key);
                    ctx.channel().writeAndFlush("login_gm " + Utils.gm_key + "");
                    break;
                default:
                    ctx.channel().writeAndFlush("nope");
            }
        } else {
            ctx.channel().writeAndFlush("\n");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        AuthServer.logger.info(ctx.channel().remoteAddress() + ": Channel Inactive");
    }
}
