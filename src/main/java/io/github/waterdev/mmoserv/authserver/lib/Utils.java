package io.github.waterdev.mmoserv.authserver.lib;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static final String version = "1.0.0";
    public static final String gm_key = "gamemasterSpecialUWU"; //Login token for gamemasters that isn't a UUID. Should be save cause the Redis server is private :thinking:

    public static String stringToSHA265(String original) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return bytesToHex(digest.digest(original.getBytes(StandardCharsets.UTF_8)));
    }

    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static final String header = "\n" +
            " ▄▄   ▄▄ ▄▄   ▄▄ ▄▄▄▄▄▄▄    ▄▄▄▄▄▄▄ ▄▄▄▄▄▄▄ ▄▄▄▄▄▄   ▄▄   ▄▄ \n" +
            "█  █▄█  █  █▄█  █       █  █       █       █   ▄  █ █  █ █  █\n" +
            "█       █       █   ▄   █  █  ▄▄▄▄▄█    ▄▄▄█  █ █ █ █  █▄█  █\n" +
            "█       █       █  █ █  █  █ █▄▄▄▄▄█   █▄▄▄█   █▄▄█▄█       █\n" +
            "█       █       █  █▄█  █  █▄▄▄▄▄  █    ▄▄▄█    ▄▄  █       █\n" +
            "█ ██▄██ █ ██▄██ █       █   ▄▄▄▄▄█ █   █▄▄▄█   █  █ ██     █ \n" +
            "█▄█   █▄█▄█   █▄█▄▄▄▄▄▄▄█  █▄▄▄▄▄▄▄█▄▄▄▄▄▄▄█▄▄▄█  █▄█ █▄▄▄█  \n" +
            "Version: " + version + " | Your OpenSource MMO Server running under the MIT License";

}
