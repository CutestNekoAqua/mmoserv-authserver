package io.github.waterdev.mmoserv.authserver.MySQL;

public class MySQLConfig {

    String user = "authserver";
    String password = "changeme";
    String ipAndPort = "localhost:3306";
    String database = "authserver";
    String salt = ".drinkmilk.opensource";

    public MySQLConfig(String user, String password, String ipAndPort, String database, String salt) {
        this.user = user;
        this.password = password;
        this.ipAndPort = ipAndPort;
        this.database = database;
        this.salt = salt;
    }

    public MySQLConfig() {
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getIpAndPort() {
        return ipAndPort;
    }

    public String getDatabase() {
        return database;
    }

    public String getSalt() {
        return salt;
    }
}
