package com.neige_i.mareu.view.list;

import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.neige_i.mareu.R;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ListActivityTest {

    // ---------------------------------- ACTIVITY RULE VARIABLE -----------------------------------

    @Rule
    public ActivityScenarioRule<ListActivity> activityRule = new ActivityScenarioRule<>(ListActivity.class);

    // ----------------------------------------- INIT TEST -----------------------------------------

    @Test
    public void onActivityStarted_checkInitialState() {
        // Given: initial state

        // When: nothing

        // Then: the meeting list is empty
        onView(withId(R.id.list_meeting)).check(withItemCount(0));
    }

    // ------------------------------------ DRAWER LAYOUT TEST -------------------------------------

    @Test
    public void onMenuItemClicked_showOrHideLayout() {
        // Given: initial state

        // When: click on the filter item menu
        onView(withId(R.id.filter)).perform(click());

        // Then: the filter layout is displayed
        onView(withId(R.id.filter_drawer)).check(matches(isDisplayed()));

        // When: click on the filter item menu again
        onView(withId(R.id.filter)).perform(click());

        // Then: the filter layout is hidden
        onView(withId(R.id.filter_drawer)).check(matches(not(isDisplayed())));
    }

    // ------------------------------------- MEETING LIST TEST -------------------------------------

    @Test
    public void onAddButtonLongClicked_showItems() {
        // Given: initial state

        // When: long click on the 'add' button
        onView(withId(R.id.add_meeting)).perform(longClick());

        // Then: the list contains the 6 dummy meetings
        onView(withId(R.id.list_meeting)).check(withItemCount(6));
    }

    @Test
    public void onDeleteMeeting_removeItem() {
        // Given: add 6 dummy meetings
        onView(withId(R.id.add_meeting)).perform(longClick());

        onView(withId(R.id.list_meeting))
            // When: click on the 'remove' button of the first meeting
            .perform(actionOnItemAtPosition(0, clickOnView(R.id.delete_meeting)))

            // Then: the list does not contain 'Meeting A' anymore (which is the topic of the 1st meeting
            .check(withItemCount(5))
            .check(matches(not(hasDescendant(withText("Meeting A")))));
    }

    // ---------------------------------------- FILTER TEST ----------------------------------------

    @Test
    public void onSelectStartDateFilter_showOnlyMeetingAfter() {
        // Given: add 6 dummy meetings and open filter layout
        onView(withId(R.id.add_meeting)).perform(longClick());
        onView(withId(R.id.filter)).perform(click());

        // When: click on the start date filter, select a date and validate
        onView(withId(R.id.start_date_filter_input)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 9));
        onView(withId(android.R.id.button1)).perform(click());

        // Then: list only contains 'Meeting E' & 'Meeting F' which date are from 19/11/2020
        onView(withId(R.id.list_meeting))
            .check(withItemCount(2))
            .check(matches(not(hasDescendant(withText("Meeting A")))))
            .check(matches(not(hasDescendant(withText("Meeting B")))))
            .check(matches(not(hasDescendant(withText("Meeting C")))))
            .check(matches(not(hasDescendant(withText("Meeting D")))))
            .check(matches(hasDescendant(withText("Meeting E"))))
            .check(matches(hasDescendant(withText("Meeting F"))));
    }

    @Test
    public void onClearStartDateFilter_showAllMeetings() {
        // Given: add 6 dummy meetings, open filter layout and select a start date filter
        onView(withId(R.id.add_meeting)).perform(longClick());
        onView(withId(R.id.filter)).perform(click());
        onView(withId(R.id.start_date_filter_input)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 12));
        onView(withId(android.R.id.button1)).perform(click());

        // When: click on 'clear' icon of the start date filter
        onView(allOf(
            withId(R.id.text_input_end_icon),
            isDescendantOfA(withId(R.id.start_date_filter_layout)),
            withContentDescription("Clear text"))
        ).perform(click());

        // Then: list contains all 6 dummy meetings again
        onView(withId(R.id.list_meeting)).check(withItemCount(6));
    }

    @Test
    public void onSelectPlaceFilter_showOnlyMeeting() {
        // Given: add 6 dummy meetings and open filter layout
        onView(withId(R.id.add_meeting)).perform(longClick());
        onView(withId(R.id.filter)).perform(click());

        // When: click on the 2nd place filter (which corresponds to the Luigi place)
        onView(withId(R.id.place_list)).perform(actionOnItemAtPosition(1, clickOnView(R.id.place_button)));

        // Then: list only contains 'Meeting B' which is held in Luigi's place
        onView(withId(R.id.list_meeting))
            .check(withItemCount(1))
            .check(matches(hasDescendant(withText("Meeting B"))));
    }

    @Test
    public void onSelectMemberFilter_showOnlyMeeting() {
        // Given: add 6 dummy meetings and open filter layout
        onView(withId(R.id.add_meeting)).perform(longClick());
        onView(withId(R.id.filter)).perform(click());

        // When: click on the 8th member filter (which corresponds to alexandra)
        onView(withId(R.id.member_list)).perform(actionOnItemAtPosition(7, clickOnView(R.id.member_button)));

        // Then: list only contains 'Meeting C' which has alexandra as member
        onView(withId(R.id.list_meeting))
            .check(withItemCount(1))
            .check(matches(hasDescendant(withText("Meeting C"))));
    }

    // ---------------------------------- CUSTOM ESPRESSO METHODS ----------------------------------

    private ViewAction clickOnView(@IdRes int viewId) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.findViewById(viewId).performClick();
            }
        };
    }

    private ViewAssertion withItemCount(int count) {
        return (view, noViewFoundException) -> {
            if (noViewFoundException != null)
                throw noViewFoundException;

            assertThat(((RecyclerView) view).getAdapter().getItemCount(), is(count));
        };
    }
}