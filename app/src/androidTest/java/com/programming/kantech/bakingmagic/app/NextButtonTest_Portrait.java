package com.programming.kantech.bakingmagic.app;


import android.content.pm.ActivityInfo;
import android.os.SystemClock;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.programming.kantech.bakingmagic.app.R;
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

// Test the next button
@LargeTest
@RunWith(AndroidJUnit4.class)
public class NextButtonTest_Portrait {

    @Rule
    public ActivityTestRule<Activity_Main> mActivityTestRule = new ActivityTestRule<>(Activity_Main.class);

    @Test
    public void nextButton_Test() {

        // Not sure how to force


//        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        SystemClock.sleep(100);


//        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        ViewInteraction recipe_list = onView(
                allOf(withId(R.id.rv_recipes),
                        withParent(allOf(withId(R.id.coordinator_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));

        // Select 'Yellow Cake'
        recipe_list.perform(actionOnItemAtPosition(3, click()));

        // Not sure how to force the orientation to portrait for the last 2 interactions of this test

        ViewInteraction details_list = onView(
                allOf(withId(R.id.rv_details_list), isDisplayed()));
        details_list.perform(actionOnItemAtPosition(4, click()));

        ViewInteraction tv_next_step = onView(
                allOf(withId(R.id.tv_next_step), withText("Next Step"),
                        withParent(withId(R.id.layout_for_step_navigation)),
                        isDisplayed()));
        tv_next_step.perform(click());

        ViewInteraction tv_step_title = onView(
                allOf(withId(R.id.tv_step_title), withText("Finish filling prep"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container_master),
                                        0),
                                0),
                        isDisplayed()));

        tv_step_title.check(matches(withText("Finish filling prep")));

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
