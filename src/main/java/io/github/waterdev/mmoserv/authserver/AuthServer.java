package io.github.waterdev.mmoserv.authserver;

import io.github.waterdev.mmoserv.authserver.Redis.SharedDB;
import io.github.waterdev.mmoserv.authserver.lib.ConfigLoader;
import io.github.waterdev.mmoserv.authserver.MySQL.AccDB;
import io.github.waterdev.mmoserv.authserver.MySQL.MySQLConfig;
import io.github.waterdev.mmoserv.authserver.Redis.RedisConfig;
import io.github.waterdev.mmoserv.authserver.lib.Utils;
import io.github.waterdev.mmoserv.authserver.networking.AuthServerBootstrap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class AuthServer {

    public static MySQLConfig mySQLConfig;
    public static RedisConfig redisConfig;
    public static Logger logger = LogManager.getLogger(AuthServer.class);
    public static AccDB accDB;
    public static SharedDB sharedDB;
    public static AuthServerBootstrap authServerBootstrap;

    public static void main(String[] args) {

        logger.info(Utils.header);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Thread.sleep(200);
                System.out.println("Shutting down ...");
                shutdown();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }));

        logger.debug("Loading MySQL Config..");

        try {
            mySQLConfig = ConfigLoader.loadConf(MySQLConfig.class, new File("mysql.json"));
        } catch (Exception e) {
            logger.error(e);
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                logger.error(stackTraceElement);
            }
            return;
        }

        logger.debug("Loading Redis Config..");

        try {
            redisConfig = ConfigLoader.loadConf(RedisConfig.class, new File("redis.json"));
        } catch (Exception e) {
            logger.error(e);
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                logger.error(stackTraceElement);
            }
            return;
        }

        if(mySQLConfig == null || redisConfig == null) {
            logger.warn("No config file could be found! A new config was created! Please fill the newly created json files with your login data and restart the programm!");
            return;
        }

        logger.info("Connecting to MySQl Database.. " + mySQLConfig.getIpAndPort());
        accDB = new AccDB();
        accDB.connect();

        logger.info("Connecting to Redis.. " + redisConfig.getUrl());
        sharedDB = new SharedDB();
        sharedDB.connect();


        authServerBootstrap = new AuthServerBootstrap();
        try {
            authServerBootstrap.start(9690);
        } catch (InterruptedException e) {
            logger.error(e);
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                logger.error(stackTraceElement);
            }
            return;
        }


        //Simple Code to keep process running
        /*logger.info("Type 'q' and enter to exit");
        Scanner in = new Scanner(System.in);
        while (true) {
            String typed = in.next();
            if(typed.toLowerCase().equalsIgnoreCase("q")) break;
        }*/

    }

    // Shuts everything correctly down
    public static void shutdown() throws InterruptedException {

        if(accDB != null) accDB.close();

        if(authServerBootstrap != null) authServerBootstrap.stop();

    }

}
