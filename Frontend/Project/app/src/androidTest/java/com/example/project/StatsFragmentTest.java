package com.example.project;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

// Mock the RequestServerForService class
@RunWith(AndroidJUnit4.class)
public class StatsFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testStatsPageCreation() {
        Espresso.onView(withId(R.id.stats)).perform(click());
        Espresso.onView(withId(R.id.stats)).check(matches(isDisplayed()));
    }
    @Test
    public void testStatsFragmentUIElements() {
        Espresso.onView(withId(R.id.stats)).perform(click());
        Espresso.onView(withId(R.id.stats)).check(matches(isDisplayed()));


        Espresso.onView(withId(R.id.header)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.gamesVal)).check(matches(isDisplayed()));

    }
}