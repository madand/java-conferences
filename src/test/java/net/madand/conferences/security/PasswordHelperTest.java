package net.madand.conferences.security;

import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordHelperTest {
    @Test
    public void hash() {
        Logger.getLogger(PasswordHelperTest.class).error("testing error");
        String password1 = "foo";
        String hash1 = PasswordHelper.hash(password1);
        assertNotEquals("Result must not be empty", "", hash1);

        String hash2 = PasswordHelper.hash(password1);
        assertNotEquals("Result must not be the same for the same input", hash1, hash2);

        String password2 = "bar";
        String hash3 = PasswordHelper.hash(password2);
        assertNotEquals("Result must not be the same for the different inputs", hash1, hash3);
    }

    @Test
    public void checkPassword() {
        String password1 = "foo";
        String password2 = "bar";
        String hash1 = PasswordHelper.hash(password1);
        assertTrue("Must verify correct password", PasswordHelper.checkPassword(password1, hash1));
        assertFalse("Must reject wrong password", PasswordHelper.checkPassword(password2, hash1));
    }
}