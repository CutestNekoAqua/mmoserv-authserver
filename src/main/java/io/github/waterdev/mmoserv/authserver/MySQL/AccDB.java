package io.github.waterdev.mmoserv.authserver.MySQL;

import io.github.waterdev.mmoserv.authserver.AuthServer;

import java.sql.*;

public class AccDB {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public static final int EMAIL_EXISTS = 22;
    public static final int NAME_EXISTS = 23;

    public static final int WRONG_CREDENTIALS = -1;
    public static final int USER_ACCOUNT = 0;
    public static final int GM_ACCOUNT = 1;

    Connection conn = null;

    public void connect() {
        try {

            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection("jdbc:mysql://" + AuthServer.mySQLConfig.getIpAndPort() + "/" + AuthServer.mySQLConfig.getDatabase(), AuthServer.mySQLConfig.getUser(), AuthServer.mySQLConfig.getPassword());

            AuthServer.logger.debug("Checking if Table 'accounts' exists. If not we create it.");
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs;

            rs = md.getTables(null, null, "accounts", null);
            if(!rs.next()) {
                AuthServer.logger.info("Creating new Database 'accounts'..");

                Statement stmt = conn.createStatement();
                int result = stmt.executeUpdate("create table accounts(" +
                        "id INT NOT NULL AUTO_INCREMENT," +
                        "email VARCHAR(40) NOT NULL," +
                        "name VARCHAR(20) NOT NULL," +
                        "hash CHAR(64) NOT NULL," +
                        "creation DATE NOT NULL," +
                        "last_login DATE," +
                        "gamemaster BOOLEAN NOT NULL," +
                        "PRIMARY KEY ( id )" +
                        ");");

                if(result != 0) {
                    AuthServer.logger.error("Something went not as planned while trying to create 'accounts' Database");
                }
                stmt.close();

            }

        } catch (Exception e) {
            AuthServer.logger.error(e);
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                AuthServer.logger.error(stackTraceElement);
            }
        }
    }

    /*
    Using ints for switch cause I rushed it xD
    TODO: Change to Enum
     */
    public int createUser(String email, String name, String hash, boolean gamemaster) throws SQLException {

        Statement statement = conn.createStatement();

        ResultSet rs1 = statement.executeQuery("SELECT email FROM accounts WHERE email = '" + email + "';");

        if(rs1.next()) return EMAIL_EXISTS;
        rs1.close();

        ResultSet rs2 = statement.executeQuery("SELECT name FROM accounts WHERE name = '" + name + "';");

        if(rs2.next()) return NAME_EXISTS;
        rs2.close();

        int result = statement.executeUpdate("INSERT INTO `accounts` (`email`, `name`, `hash`, `creation`, `last_login`, `gamemaster`)" +
                " VALUES ('" + email + "', '"+ name + "', '" + hash + "', '" + new Date(new java.util.Date().getTime()).toString() + "', NULL, '" + (gamemaster ? 1 : 0) + "');");

        statement.close();

        return result;

    }

    /*
    TODO Change to Enum
     */
    public int checkUser(String name, String hash) throws SQLException {

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT gamemaster FROM accounts WHERE name = '" + name + "' AND hash = '" + hash + "';");

        if(!rs.next()) return WRONG_CREDENTIALS;
        return rs.getInt("gamemaster") == 0 ? USER_ACCOUNT : GM_ACCOUNT;

    }

    // Cleaning connection
    public void close() {
        try {
            conn.close();
        } catch (Exception e) {
            AuthServer.logger.error(e);
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                AuthServer.logger.error(stackTraceElement);
            }
        }
    }

}
