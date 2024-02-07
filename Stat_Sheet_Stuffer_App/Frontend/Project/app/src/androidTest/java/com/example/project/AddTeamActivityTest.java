package com.example.project;

import static android.app.PendingIntent.getActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

// Mock the RequestServerForService class
@RunWith(AndroidJUnit4.class)

public class AddTeamActivityTest {

    @Rule
    public ActivityScenarioRule<AddTeamActivity> activityScenarioRule = new ActivityScenarioRule<>(AddTeamActivity.class);

    @Before
    public void launchActivity() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Test
    public void testAddingPublicTeam() {



        String typedText = "Hoopers";
        Espresso.onView(withId(R.id.etTeamname)).perform(ViewActions.typeText(typedText), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.tvTeamType)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is("Public")))
                .inRoot(isPlatformPopup())
                .perform(click());

        Espresso.onView(withId(R.id.etTeamname))
                .check(matches(withText(typedText)));


        String selectedTeamType = "Public";
        Espresso.onView(withId(R.id.tvTeamType))
                .check(matches(withText(containsString(selectedTeamType))));


    }

    @Test
    public void testAddingPrivateTeam() {

        String typedText = "Hoopers";
        String typePassword = "Password";
        Espresso.onView(withId(R.id.etTeamname)).perform(ViewActions.typeText(typedText), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.tvTeamType)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is("Private")))
                .inRoot(isPlatformPopup())
                .perform(click());
        Espresso.onView(withId(R.id.etTeamPassword)).perform(ViewActions.typeText(typePassword), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.etTeamname))
                .check(matches(withText(typedText)));

        Espresso.onView(withId(R.id.etTeamPassword))
                .check(matches(withText(typePassword)));

        String selectedTeamType = "Private";
        Espresso.onView(withId(R.id.tvTeamType))
                .check(matches(withText(containsString(selectedTeamType))));


    }

    @Test
    public void testEmptyTeamName() {

        Espresso.onView(withId(R.id.etTeamname)).perform(ViewActions.typeText(" "), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.tvTeamType)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is("Public")))
                .inRoot(isPlatformPopup())
                .perform(click());


        Espresso.onView(withId(R.id.finish)).perform(click());


        Espresso.onView(withText("Field cannot be empty"))
                .check(matches(isDisplayed()));
    }
}
