package com.programming.kantech.bakingmagic.app;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.programming.kantech.bakingmagic.app.views.activities.Activity_Main;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by patrick keogh on 2017-07-05.
 *
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityScreen_Test {

    public static final String RECIPE_NAME = "Brownies";

    @Rule
    public ActivityTestRule<Activity_Main> mActivityTestRule = new ActivityTestRule<>(Activity_Main.class);

    @Test
    public void testDetailsActivity() {

        // Get the recipe list
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.rv_recipes),
                        withParent(allOf(withId(R.id.coordinator_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));

        // Perform a click on the first item in the list
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        // Get the toolbar title view
        ViewInteraction textView = onView(
                allOf(withText(RECIPE_NAME),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()));

        // Make sure the title is set to the selected recipe
        textView.check(matches(withText(RECIPE_NAME)));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
