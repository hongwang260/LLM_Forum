package com.example.ginshinimpact_project2_cs310;

import android.text.TextUtils;

import org.junit.Test;
import static org.junit.Assert.*;
public class ValidPostTest {

    public boolean validInput(String title, String llmKind, String content, String note){
        if (title.isEmpty() || llmKind.isEmpty() || content.isEmpty()) {
            return false;
        }
        return true;
    }

    @Test
    public void testValidInput(){
        assertTrue(validInput("My post", "GPT", "This is my post", ""));
        assertTrue(validInput("My post", "GPT", "This is my post", "I am the author"));
    }

    @Test
    public void testInvalidInput(){
        assertFalse(validInput("My post", "GPT", "", ""));
        assertFalse(validInput("My post", "GPT", "", "I am the author"));

        assertFalse(validInput("My post", "", "My Content", ""));
        assertFalse(validInput("My post", "", "", "I am the author"));

        assertFalse(validInput("", "GPT", "MyContent", ""));
        assertFalse(validInput("", "", "", "I am the author"));
        assertFalse(validInput("", "", "", ""));
    }
}
