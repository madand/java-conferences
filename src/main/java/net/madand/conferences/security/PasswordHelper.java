package net.madand.conferences.security;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilities for password hashing.
 */
public class PasswordHelper {
    private PasswordHelper() {}

    /**
     * Securely hash the given password.
     *
     * @param password the password as plain text.
     * @return the password hash.
     */
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Check whether the given plaintext password is correct for the given hash.
     *
     * @param plaintext the password in plain text.
     * @param hashed the password hash.
     * @return true if password is correct, false - otherwise.
     */
    public static boolean checkPassword(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }
}
