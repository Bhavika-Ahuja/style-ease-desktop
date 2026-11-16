package com.stylesync.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password hashing and verification
 */
public class PasswordUtil {
    
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
