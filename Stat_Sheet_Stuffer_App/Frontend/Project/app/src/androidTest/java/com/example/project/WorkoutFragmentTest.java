package com.example.project;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.IdlingRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class WorkoutFragmentTest {

    private CountingIdlingResource idlingResource;

    @Before
    public void setup() {
        // Initialize Espresso Intents
        Intents.init();

        // Initialize your IdlingResource
        idlingResource = new CountingIdlingResource("DataLoader");
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void cleanup() {
        // Release Espresso Intents and IdlingResource after each test
        Intents.release();
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test
    public void testBeginWorkoutButton() {
        // Launch the MainActivity using ActivityScenario
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);

        // Navigate to WorkoutFragment
        Espresso.onView(withId(R.id.workout)).perform(click());

        // Perform click action on the 'Begin Workout' button
        Espresso.onView(withId(R.id.btnBegin)).perform(click());

        // Check if WorkoutActivity is launched
        Intents.intended(hasComponent(WorkoutActivity.class.getName()));

    }
}
