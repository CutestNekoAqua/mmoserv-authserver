package io.github.waterdev.mmoserv.authserver.Redis;

public class RedisConfig {

    String url = "localhost";
    String password = "canbeblank";

    public RedisConfig(String url, String password) {
        this.url = url;
        this.password = password;
    }

    public RedisConfig() {
    }

    public String getUrl() {
        return url;
    }

    public String getPassword() {
        return password;
    }
}
