package com.artsolo.musicplayer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncrypter {
    public static String encryptPassword(String password) {
        String encryptedPassword = null;
        try {
            // Create a MessageDigest object with the SHA-256 algorithm
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Encrypt the password and convert it into a byte array
            byte[] passwordBytes = md.digest(password.getBytes());

            // Convert the array of bytes into a string in the hexadecimal number system
            StringBuilder sb = new StringBuilder();
            for (byte b : passwordBytes) {
                sb.append(String.format("%02x", b));
            }
            encryptedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedPassword;
    }
}
