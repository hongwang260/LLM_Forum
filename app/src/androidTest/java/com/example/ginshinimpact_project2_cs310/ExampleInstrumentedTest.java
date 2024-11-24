package com.example.ginshinimpact_project2_cs310;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ActivityScenario;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.allOf;
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.JVM)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.ginshinimpact_project2_cs310", appContext.getPackageName());
    }

    // Test if the sign up button is functional in the Sign Up page
    @Test
    public void SuccessfulSignUp(){
        ActivityScenario.launch(SignupPage.class);
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("mwang@usc.edu"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonSignup)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.textViewTitle)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Profile")));
    }

    @Test
    public void TestLoginButton(){
        ActivityScenario.launch(LoginPage.class);
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("mwang@usc.edu"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.HomeTitle))
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

    // Test if the search button is functional in the Home Page for LLM search
    @Test
    public void TestSearchLLM(){
        ActivityScenario.launch(HomePage.class);
        Espresso.onView(ViewMatchers.withId(R.id.search_bar)).perform(ViewActions.typeText("GPT"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.op1)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.topic)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Search Result")));
    }

    // Test if the search button is functional in the Home Page for Title search
    @Test
    public void TestSearchTitle() {
        ActivityScenario.launch(HomePage.class);
        Espresso.onView(ViewMatchers.withId(R.id.search_bar)).perform(ViewActions.typeText("GPT"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.op2)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.topic)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Search Result")));
    }

    // Test if the search button is functional in the Home Page for full text search
    @Test
    public void TestSearchText() {
        ActivityScenario.launch(HomePage.class);
        Espresso.onView(ViewMatchers.withId(R.id.search_bar)).perform(ViewActions.typeText("GPT"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.op3)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.topic)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Search Result")));
    }

    // Test if clicking on the post will redirect
    @Test
    public void DetailReturnButton() {
        // log in using existing account and test
        ActivityScenario.launch(LoginPage.class);
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("mwang@usc.edu"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click());

        // Locate the post with the specific text "AngelaQA2" and click it
        Espresso.onView(
                        allOf(
                                ViewMatchers.withText("AngelaQA2"), // Match the text of the post
                                ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.linearLayoutPosts)) // Ensure it's in the LinearLayout
                        ))
                .check(matches(ViewMatchers.isDisplayed())) // Check if it is displayed
                .perform(ViewActions.click()); // Perform click action

        // Perform actions in the detail page (e.g., click the back button)
        Espresso.onView(ViewMatchers.withId(R.id.buttonBack)).perform(ViewActions.click());

        // Verify that the app navigates back to the explore screen
        Espresso.onView(ViewMatchers.withId(R.id.HomeTitle))
                .check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Explore Prompts")));
    }

    // test to see if the make post button redirect to the proper page
    @Test
    public void MakePostButton() {
        // login first with a valid user
        ActivityScenario.launch(LoginPage.class);
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("mwang@usc.edu"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click());

        // check if new post button works for redirecting to the correct page
        Espresso.onView(ViewMatchers.withId(R.id.nav_new_post)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.AddPostTitle)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Share Your Prompt")));
    }

    @Test
    public void TestProfileButton() {
        ActivityScenario.launch(LoginPage.class);
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("mwang@usc.edu"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click());

        // check if profile button works for redirecting to the correct page
        Espresso.onView(ViewMatchers.withId(R.id.nav_user_profile)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.textViewTitle)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Profile")));
    }

    @Test
    public void TestMinePostButton() {
        ActivityScenario.launch(LoginPage.class);
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("mwang@usc.edu"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click());

        // check if profile button works for redirecting to the correct page
        Espresso.onView(ViewMatchers.withId(R.id.nav_my_posts)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.PostsTitle)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Mine Posts")));
    }

    @Test
    public void TestMineCommentsButton() {
        ActivityScenario.launch(LoginPage.class);
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("mwang@usc.edu"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click());

        // check if profile button works for redirecting to the correct page
        Espresso.onView(ViewMatchers.withId(R.id.nav_my_comments)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.commentsTitle)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Mine Comments")));
    }

    @Test
    public void TestMakePost () {
        // log in with valid user
        ActivityScenario.launch(LoginPage.class);
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("mwang@usc.edu"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click());

        // fill in required fields to make a post
        Espresso.onView(ViewMatchers.withId(R.id.nav_new_post)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTitle)).perform(ViewActions.typeText("Espresso test title"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextLLMKind)).perform(ViewActions.typeText("espresso"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextContent)).perform(ViewActions.typeText("N/A"), ViewActions.closeSoftKeyboard());

        // make the post and check if it returns back to home page
        Espresso.onView(ViewMatchers.withId(R.id.buttonSavePost)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.HomeTitle)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Explore Prompts")));
    }

    @Test
    public void TestAddCommentButton() {
        // login with valid user
        ActivityScenario.launch(LoginPage.class);
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("mwang@usc.edu"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click());

        // Locate the post with the espresso test title
        Espresso.onView(
                        allOf(
                                ViewMatchers.withText("Espresso test title"), // Match the text of the post
                                ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.linearLayoutPosts)) // Ensure it's in the LinearLayout
                        ))
                .perform(ViewActions.scrollTo()) // Scroll to the post
                .check(matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click());

        // check if add comment button redirect to the correct page
        Espresso.onView(ViewMatchers.withId(R.id.buttonAddComment)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.AddCommentTitle)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Add Comment")));
    }

    @Test
    public void DeletePostButton() {
        // log in using existing account and test
        ActivityScenario.launch(LoginPage.class);
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("mwang@usc.edu"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click());

        // Locate the post with the espresso test title and delete it
        Espresso.onView(
                        allOf(
                                ViewMatchers.withText("Espresso test title"), // Match the text of the post
                                ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.linearLayoutPosts)) // Ensure it's in the LinearLayout
                        ))
                .perform(ViewActions.scrollTo()) // Scroll to the post
                .check(matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click());

        // check if clicking on the delete will return to the home page
        Espresso.onView(ViewMatchers.withId(R.id.buttonDeletePost)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.HomeTitle)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Explore Prompts")));
    }

    @Test
    public void TestLogoutButton() {
        // login first with valid user
        ActivityScenario.launch(LoginPage.class);
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail)).perform(ViewActions.typeText("mwang@usc.edu"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword)).perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click());

        // go to profile page
        ActivityScenario.launch(ProfileActivity.class);

        Espresso.onView(ViewMatchers.withId(R.id.buttonLogout)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.textViewTitle)).check(matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("PromptShare")));
    }
}