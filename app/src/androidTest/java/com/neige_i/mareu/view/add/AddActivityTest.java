package com.neige_i.mareu.view.add;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DummyGenerator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.neige_i.mareu.util.ClickViewAction.clickOnView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AddActivityTest {

    @Rule
    public ActivityScenarioRule<AddActivity> activityRule = new ActivityScenarioRule<>(AddActivity.class);

    @Test
    public void onActivityStarted_checkInitialState() {
        // Given: initial state

        // When: do nothing

        // Then:
        // All fields are empty
        onView(withId(R.id.topic_input)).check(matches(withText("")));
        onView(withId(R.id.date_input)).check(matches(withText("")));
        onView(withId(R.id.start_time_input)).check(matches(withText("")));
        onView(withId(R.id.end_time_input)).check(matches(withText("")));
        onView(withId(R.id.place_input)).check(matches(withText("")));
        // The RecyclerView must contain only one element
        onView(withId(R.id.list_member)).check(matches(hasChildCount(1)));
        // The 'remove' button should not be displayed
        onView(withId(R.id.remove_member)).check(matches(not(isDisplayed())));
    }

    @Test
    public void onAddButtonClicked_triggerErrors() {
        // Given: initial state

        // When: click on 'add' button
        onView(withId(R.id.add_button)).perform(click());

        // Then: all errors are triggered
        checkLayoutWithError(R.id.topic_layout);
        checkLayoutWithError(R.id.date_layout);
        checkLayoutWithError(R.id.start_time_layout);
        checkLayoutWithError(R.id.end_time_layout);
        checkLayoutWithError(R.id.place_layout);
        checkLayoutWithError(R.id.email_layout);
    }

    @Test
    public void onTopicTyped_removeError() {
        // Given: errors are triggered
        onView(withId(R.id.add_button)).perform(ViewActions.click());

        onView(withId(R.id.topic_input)).perform(replaceText("    "));
        checkLayoutWithError(R.id.topic_layout);

        // When: type text in topic field
        onView(withId(R.id.topic_input)).perform(typeText("new topic"));

        // Then: topic error is removed
        checkLayoutWithoutError(R.id.topic_layout);
    }

    @Test
    public void onPlaceSelected_updateValue() {
        onView(withId(R.id.add_button)).perform(ViewActions.click());
        onView(withId(R.id.place_input)).perform(click());

        onView(withText("Mario")).inRoot(isPlatformPopup()).perform(click());

        onView(withId(R.id.place_input)).check(matches(withText("Mario")));
        checkLayoutWithoutError(R.id.place_layout);
    }

    @Test
    public void onMemberSelected_updateValue() {
        onView(withId(R.id.add_button)).perform(ViewActions.click());
        onView(withId(R.id.email_input)).perform(click());

        onView(withText("maxime@lamzone.com")).inRoot(isPlatformPopup()).perform(click());

        onView(withId(R.id.email_input)).check(matches(withText("maxime@lamzone.com")));
        checkLayoutWithoutError(R.id.email_layout);
    }

    @Test
    public void onAddMember_changeFirstRemoveButtonVisibility() {
        onView(withId(R.id.list_member))
            .perform(actionOnItemAtPosition(0, clickOnView(R.id.add_member)))
            .check(matches(hasChildCount(2)));

        onView(atPositionOnView(0, R.id.remove_member)).check(matches(isDisplayed()));
    }

    @Test
    public void onAddMaximumMember_changeAddButtonVisibility() {
        final int maxMemberCount = DummyGenerator.EMAILS.size();
        for (int i = 0; i < maxMemberCount - 1; i++) // There is already a member in the list
            onView(withId(R.id.list_member)).perform(actionOnItemAtPosition(0, clickOnView(R.id.add_member)));

        for (int i = 0; i < maxMemberCount; i++) {
            onView(withId(R.id.list_member)).perform(scrollToPosition(i));
            onView(atPositionOnView(i, R.id.add_member)).check(matches(not(isDisplayed())));
        }
    }

    private void checkLayoutWithError(@IdRes int layoutId) {
        getErrorView(layoutId).check(matches(withText(R.string.required_field_error)));
    }

    private void checkLayoutWithoutError(@IdRes int layoutId) {
        getErrorView(layoutId).check(matches(not(isDisplayed())));
    }

    private ViewInteraction getErrorView(@IdRes int layoutId) {
        return onView(allOf(withId(R.id.textinput_error), isDescendantOfA(withId(layoutId))));
    }

    private Matcher<View> atPositionOnView(final int position, final int targetViewId) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
            }

            @Override
            public boolean matchesSafely(View view) {
                try {
                    return view == ((RecyclerView) view.getRootView().findViewById(R.id.list_member))
                        .findViewHolderForAdapterPosition(position).itemView.findViewById(targetViewId);
                } catch (NullPointerException e) {
                    return false;
                }
            }
        };
    }
}
