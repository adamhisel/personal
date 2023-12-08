package com.example.project;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
public class GameFragmentTest {

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
    public void testViewGameButton() {
        // Launch the MainActivity using ActivityScenario
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);

        // Navigate to the GameFragment
        Espresso.onView(withId(R.id.game)).perform(click());

        // Check if the "View Game" button is displayed
        onView(withId(R.id.btnView)).check(matches(isDisplayed()));

        // Perform click action on the 'View Game' button
        Espresso.onView(withId(R.id.btnView)).perform(click());

        // Check if GameWebsocketActivity is launched
        Intents.intended(hasComponent(GameWebsocketActivity.class.getName()));
    }
}
