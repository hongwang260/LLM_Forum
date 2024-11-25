package com.example.ginshinimpact_project2_cs310;

import static org.junit.Assert.*;

import android.util.Patterns;

import org.junit.Before;
import org.junit.Test;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationWhiteBoxTests {

    // Method to validate email or ID
    public boolean isValidEmailOrId(String input) {
        // Pattern for a 10-digit ID
        Pattern idPattern = Pattern.compile("^\\d{10}$");

        return (input.endsWith("@usc.edu") && input.length() > 8) || idPattern.matcher(input).matches();
    }

    // Tests for isValidEmailOrId
    @Test
    public void testIsValidEmailOrId_ValidUscEmail() {
        assertTrue(isValidEmailOrId("test@usc.edu"));
        assertTrue(isValidEmailOrId("123@usc.edu"));
    }

    @Test
    public void testIsValidEmailOrId_InvalidEmail() {
        assertFalse(isValidEmailOrId("test@gmail.com"));
        assertFalse(isValidEmailOrId("test@yahoo.com"));
        assertFalse(isValidEmailOrId("test@usc-com"));
    }

    @Test
    public void testIsValidEmailOrId_ValidId() {
        assertTrue(isValidEmailOrId("1234567890"));
        assertTrue(isValidEmailOrId("1000000000"));
    }

    @Test
    public void testIsValidEmailOrId_InvalidId() {
        assertFalse(isValidEmailOrId("123456"));
        assertFalse(isValidEmailOrId("21648h4383"));
    }

    @Test
    public void testIsValidEmailOrId_EmptyString() {
        assertFalse(isValidEmailOrId(""));
    }
}
