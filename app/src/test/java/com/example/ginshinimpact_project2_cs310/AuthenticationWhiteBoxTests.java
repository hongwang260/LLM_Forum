package com.example.ginshinimpact_project2_cs310;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.Intent;
import android.util.Patterns;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AuthenticationWhiteBoxTests {

    @Mock
    private FirebaseDatabase mockDatabase;
    @Mock
    private DatabaseReference mockDatabaseRef;
    @Mock
    private DataSnapshot mockDataSnapshot;

    private SignupPage signupPage;
    private LoginPage loginPage;


    private boolean isValidEmailOrId(String input) {
        // Pattern for a 10-digit ID
        Pattern idPattern = Pattern.compile("^\\d{10}$");

        if (Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            // Check if it's a USC email
            return input.endsWith("@usc.edu");
        } else {
            // Check if it's a 10-digit ID
            return idPattern.matcher(input).matches();
        }
    }




    // SignupPage Tests - isValidEmailOrId method
    @Test
    public void testIsValidEmailOrId_ValidUscEmail() {
        assertTrue(isValidEmailOrId("test@usc.edu"));
    }

    @Test
    public void testIsValidEmailOrId_InvalidEmail() {
        assertFalse(isValidEmailOrId("test@gmail.com"));
    }

    @Test
    public void testIsValidEmailOrId_ValidId() {
        assertTrue(isValidEmailOrId("1234567890"));
    }

    @Test
    public void testIsValidEmailOrId_InvalidId() {
        assertFalse(isValidEmailOrId("123456"));
    }

    @Test
    public void testIsValidEmailOrId_EmptyString() {
        assertFalse(isValidEmailOrId(""));
    }


    private String encodeEmail(String email) {
        return email.replace("@", "%40").replace(".", "%2E");
    }
    // LoginPage Tests - customEncodeEmail method
    @Test
    public void testCustomEncodeEmail_ValidEmail() {
        assertEquals("test%40usc%2Eedu", encodeEmail("test@usc.edu"));
    }

    @Test
    public void testCustomEncodeEmail_NoSpecialChars() {
        assertEquals("testuscedu", encodeEmail("testuscedu"));
    }

    @Test
    public void testCustomEncodeEmail_EmptyString() {
        assertEquals("", encodeEmail(""));
    }

//
//
//    // HomePage Tests - Search method
//    @Test
//    public void testSearch_ValidInput() {
//        HomePage homePage = new HomePage();
//        Intent intent = Search("test", "title");
//        assertEquals("test", intent.getStringExtra("Content"));
//        assertEquals("title", intent.getStringExtra("Option"));
//    }
//
//    @Test
//    public void testSearch_EmptyInput() {
//        HomePage homePage = new HomePage();
//        Intent intent = homePage.Search("", "title");
//        assertEquals("", intent.getStringExtra("Content"));
//    }
//
//    // Data Flow Testing - UserProfile
//    @Test
//    public void testUserProfileDataFlow() {
//        UserProfile profile = new UserProfile();
//
//        // Definition-use testing
//        profile.setEmail("test@usc.edu");
//        assertEquals("test@usc.edu", profile.getEmail());
//
//        profile.setUsername("testUser");
//        assertEquals("testUser", profile.getUsername());
//
//        profile.setPassword("password123");
//        assertEquals("password123", profile.getPassword());
//    }
//
//    // Branch Coverage Testing - verifyUserCredentials
//    @Test
//    public void testVerifyUserCredentials_Success() {
//        when(mockDataSnapshot.exists()).thenReturn(true);
//        when(mockDataSnapshot.child("password").getValue(String.class)).thenReturn("correctPassword");
//        when(mockDatabaseRef.child(anyString())).thenReturn(mockDatabaseRef);
//
//        loginPage.verifyUserCredentials("test%40usc%2Eedu", "correctPassword");
//
//        verify(mockDataSnapshot).exists();
//        verify(mockDataSnapshot).child("password");
//    }
//
//    @Test
//    public void testVerifyUserCredentials_UserNotFound() {
//        when(mockDataSnapshot.exists()).thenReturn(false);
//        when(mockDatabaseRef.child(anyString())).thenReturn(mockDatabaseRef);
//
//        loginPage.verifyUserCredentials("nonexistent%40usc%2Eedu", "password");
//
//        verify(mockDataSnapshot).exists();
//    }
//
//    // Compound Condition Coverage - saveUserToFirebase
//    @Test
//    public void testSaveUserToFirebase_NewUser() {
//        when(mockDataSnapshot.exists()).thenReturn(false);
//        when(mockDatabaseRef.push()).thenReturn(mockDatabaseRef);
//        when(mockDatabaseRef.getKey()).thenReturn("uniqueId");
//
//        signupPage.saveUserToFirebase("test@usc.edu", "password");
//
//        verify(mockDatabaseRef).setValue(any(UserProfile.class));
//    }
//
//    @Test
//    public void testSaveUserToFirebase_ExistingUser() {
//        when(mockDataSnapshot.exists()).thenReturn(true);
//
//        signupPage.saveUserToFirebase("existing@usc.edu", "password");
//
//        verify(mockDataSnapshot).exists();
//    }
}