package com.example.project;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class JoinTeamActivityTest {

    @Rule
    public ActivityScenarioRule<JoinTeamActivity> activityScenarioRule = new ActivityScenarioRule<>(JoinTeamActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testDisplayTeamName() {
        // Check if the team name autocomplete view is displayed
        Espresso.onView(withId(R.id.tvTeamName)).check(matches(isDisplayed()));
    }

    @Test
    public void testDisplayUserType() {
        // Check if the user type autocomplete view is displayed
        Espresso.onView(withId(R.id.tvUserType)).check(matches(isDisplayed()));
    }

    @Test
    public void testDisplayJoinButton() {
        // Check if the join button is displayed
        Espresso.onView(withId(R.id.btnJoin)).check(matches(isDisplayed()));
    }

    @Test
    public void testJoiningAsFan() {
        // Perform actions to join as a fan
        Espresso.onView(withId(R.id.tvTeamName)).perform(click());
        Espresso.onData(anything()).inRoot(isPlatformPopup()).atPosition(0).perform(click());

        Espresso.onView(withId(R.id.tvUserType)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is("Fan"))).inRoot(isPlatformPopup()).perform(click());

        Espresso.onView(withId(R.id.btnJoin)).perform(click());
    }

    @Test
    public void testJoiningAsPlayer() {
        // Perform actions to join as a player
        Espresso.onView(withId(R.id.tvTeamName)).perform(click());
        Espresso.onData(anything()).inRoot(isPlatformPopup()).atPosition(1).perform(click());

        Espresso.onView(withId(R.id.tvUserType)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is("Player"))).inRoot(isPlatformPopup()).perform(click());

        // Input number and position
        Espresso.onView(withId(R.id.etNumber)).perform(ViewActions.typeText("5"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.tvPosition)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is("PG"))).inRoot(isPlatformPopup()).perform(click());

        Espresso.onView(withId(R.id.btnJoin)).perform(click());

    }
}