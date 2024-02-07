package com.example.project;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class RegistrationActivityTest {

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Test
    public void testEmptyFieldsValidation() {
        ActivityScenario<RegistrationActivity> activityScenario = ActivityScenario.launch(RegistrationActivity.class);

        // Click the sign-up button without filling in any fields
        onView(withId(R.id.btnSignUp)).perform(click());

        // Check if the "Field cannot be empty" error messages are displayed for each field

        // Check username field
        onView(allOf(withId(R.id.tilUserName), ViewMatchers.hasDescendant(withText("Field cannot be empty")))).check(matches(isDisplayed()));

        // Check first name field
        onView(allOf(withId(R.id.tilFirstName), ViewMatchers.hasDescendant(withText("Field cannot be empty")))).check(matches(isDisplayed()));

        // Check last name field
        onView(allOf(withId(R.id.tilLastName), ViewMatchers.hasDescendant(withText("Field cannot be empty")))).check(matches(isDisplayed()));

        // Check email field
        onView(allOf(withId(R.id.tilEmail), ViewMatchers.hasDescendant(withText("Field cannot be empty")))).check(matches(isDisplayed()));

        // Check phone number field
        onView(allOf(withId(R.id.tilPhoneNumber), ViewMatchers.hasDescendant(withText("Field cannot be empty")))).check(matches(isDisplayed()));

        // Check password field
        onView(allOf(withId(R.id.tilPassword), ViewMatchers.hasDescendant(withText("Field cannot be empty")))).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginButtonNavigation() {
        // Launch the RegistrationActivity using ActivityScenario
        ActivityScenario<RegistrationActivity> activityScenario = ActivityScenario.launch(RegistrationActivity.class);

        // Perform click action on the "Login" button
        Espresso.onView(withId(R.id.btnLogin)).perform(ViewActions.click());

        // Check if LoginActivity is launched
        Intents.intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void testSuccessfulRegistration() {
        ActivityScenario<RegistrationActivity> activityScenario = ActivityScenario.launch(RegistrationActivity.class);

        Espresso.onView(withId(R.id.etUserName)).perform(ViewActions.typeText("testUser"));
        Espresso.onView(withId(R.id.etFirstName)).perform(ViewActions.typeText("John"));
        Espresso.onView(withId(R.id.etLastName)).perform(ViewActions.typeText("Doe"));
        Espresso.onView(withId(R.id.etEmail)).perform(ViewActions.typeText("john@example.com"));
        Espresso.onView(withId(R.id.etPhoneNumber)).perform(ViewActions.typeText("1234567890"));
        Espresso.onView(withId(R.id.etPassword)).perform(ViewActions.typeText("password"), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.btnSignUp)).perform(ViewActions.click());
    }
}

