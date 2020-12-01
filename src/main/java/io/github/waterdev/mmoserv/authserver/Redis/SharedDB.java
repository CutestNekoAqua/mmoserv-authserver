package io.github.waterdev.mmoserv.authserver.Redis;

import io.github.waterdev.mmoserv.authserver.AuthServer;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisStringAsyncCommands;

import java.util.concurrent.ExecutionException;

public class SharedDB {

    RedisClient redisClient;
    StatefulRedisConnection<String, String> connection;
    RedisStringAsyncCommands<String, String> async;

    public void connect() {

        if(AuthServer.redisConfig.getPassword().isEmpty()) {
            redisClient = RedisClient.create("redis://" + AuthServer.redisConfig.getUrl());
        } else {
            redisClient = RedisClient.create("redis://" + AuthServer.redisConfig.getPassword() + "@" + AuthServer.redisConfig.getUrl());
        }

        connection = redisClient.connect();
        async = connection.async();

    }

    public void setValue(String key, String value) {
        async.set(key, value);
    }

    public String getValue(String key) throws ExecutionException, InterruptedException {
        return async.get(key).get();
    }

    public void close() {
        connection.close();
        redisClient.shutdown();
    }

}
