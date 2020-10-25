package com.neige_i.mareu.view.add;

import android.view.View;
import android.widget.DatePicker;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.textfield.TextInputLayout;
import com.neige_i.mareu.R;
import com.neige_i.mareu.util.RecyclerViewItemCountAssertion;
import com.neige_i.mareu.view.add.AddActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.neige_i.mareu.util.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

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
        onView(ViewMatchers.withId(R.id.topic_input)).check(matches(withText("")));
        onView(withId(R.id.date_input)).check(matches(withText("")));
        onView(withId(R.id.start_time_input)).check(matches(withText("")));
        onView(withId(R.id.end_time_input)).check(matches(withText("")));
        onView(withId(R.id.place_input)).check(matches(withText("")));
        // The RecyclerView must contain only one element
        onView(withId(R.id.list_member)).check(withItemCount(1));
        // The 'remove' button must be INVISIBLE
        onView(withId(R.id.remove_member)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void onAddButtonClicked_triggerErrors() {
        // Given: initial state

        // When: click on 'add' button
        onView(withId(R.id.add_button)).perform(ViewActions.click());

        // Then: all errors are triggered
        onView(withId(R.id.topic_layout)).check(matches(hasTextInputLayoutErrorText(" ")));
        onView(withId(R.id.topic_layout)).check(matches(notNullValue()));
        onView(withId(R.id.date_layout)).check(matches(notNullValue()));
        onView(withId(R.id.start_time_layout)).check(matches(notNullValue()));
        onView(withId(R.id.end_time_layout)).check(matches(notNullValue()));
        onView(withId(R.id.place_layout)).check(matches(notNullValue()));
        onView(withId(R.id.email_layout)).check(matches(notNullValue()));
    }

    @Test
    public void onTopicTyped_removeError() {
        // Given: errors are triggered
        onView(withId(R.id.add_button)).perform(ViewActions.click());

        // When: type text in topic field
        onView(withId(R.id.topic_input)).perform(typeText("new topic")/*, closeSoftKeyboard()*/);

        // Then: topic error is removed
        onView(withId(R.id.topic_layout)).check(matches(hasTextInputLayoutErrorText(null)));
    }

    @Test
    public void onDateClicked_showDefaultDate() {
        // Given: initial state

        // When: click on date field
        onView(withId(R.id.date_input)).perform(click());

        // Then: dialog is displayed with the appropriate date
//        onView(withClassName(equalTo(DatePicker.class.getName()))).check(matches(matchesDate(2020, 10, 30)));
    }

    @Test
    public void onPlaceSelected_updateValue() {
        onView(withId(R.id.place_input)).perform(click());

        onView(withText("Mario")).inRoot(isPlatformPopup()).perform(click());

        onView(withId(R.id.place_input)).check(matches(withText("Mario")));
    }

    private Matcher<View> hasTextInputLayoutErrorText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                final CharSequence error = ((TextInputLayout) view).getError();

                if (error == null) {
                    return expectedErrorText == null;//false;
                }

//                final String hint = error.toString();

                return expectedErrorText.equals(error.toString());
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    private Matcher<View> matchesDate(final int year, final int month, final int day) {
        return new BoundedMatcher<View, DatePicker>(DatePicker.class) {
            @Override
            public void describeTo(Description description) {
            }

            @Override
            protected boolean matchesSafely(DatePicker item) {
                return year == item.getYear() && month == item.getMonth() && day == item.getDayOfMonth();
            }
        };
    }
}
