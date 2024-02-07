package com.example.project;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class ProfileFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        // Initialize Espresso Intents
        Intents.init();
    }

    @After
    public void cleanup() {
        // Release Espresso Intents after each test
        Intents.release();
    }

    @Test
    public void testNavigationToEditProfileActivity() {
        // Navigate to the ProfileFragment
        onView(withId(R.id.profile)).perform(click());

        // Check if the Edit Profile button is displayed
        onView(withId(R.id.btnEditProfile)).check(matches(isDisplayed()));

        // Perform click action on the 'Edit Profile' button
        onView(withId(R.id.btnEditProfile)).perform(click());

        // Check if EditProfileActivity is launched
        Intents.intended(hasComponent(EditProfileActivity.class.getName()));
    }

    @Test
    public void testLogoutFunctionality() {
        // Navigate to the ProfileFragment
        onView(withId(R.id.profile)).perform(click());

        // Check if the Logout button is displayed
        onView(withId(R.id.btnLogout)).check(matches(isDisplayed()));

        // Perform click action on the 'Logout' button
        onView(withId(R.id.btnLogout)).perform(click());

        // Check if LoginActivity is launched after logout
        Intents.intended(hasComponent(LoginActivity.class.getName()));
    }

}
