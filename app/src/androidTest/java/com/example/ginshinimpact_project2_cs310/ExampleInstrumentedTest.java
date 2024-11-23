package com.example.ginshinimpact_project2_cs310;

import android.content.Context;
import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ActivityScenario;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.ginshinimpact_project2_cs310", appContext.getPackageName());
    }

    @Test
    public void TestLoginButton(){
        ActivityScenario.launch(LoginPage.class);
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("mwang@usc.edu"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.commentsTitle))
                .check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Explore Prompts")));
    }

    // Test if the sign up button redirects to the Sign Up Page
    @Test
    public void TestSignUpButton(){
        ActivityScenario.launch(LoginPage.class);
        Espresso.onView(ViewMatchers.withId(R.id.buttonSignup)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.textViewTitle)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Sign Up")));
    }

    // Test if the sign up button is functional in the Sign Up page
    @Test
    public void SuccessfulSignUp(){
        ActivityScenario.launch(SignupPage.class);
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("bw123@usc.edu"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonSignup)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.textViewTitle)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Profile")));
    }

    // Test if the search button is functional in the Home Page
    @Test
    public void SearchButtonHome(){
        ActivityScenario.launch(HomePage.class);
        Espresso.onView(ViewMatchers.withId(R.id.search_bar)).perform(ViewActions.typeText("GPT"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.op1)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.topic)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Search Result")));
    }
}