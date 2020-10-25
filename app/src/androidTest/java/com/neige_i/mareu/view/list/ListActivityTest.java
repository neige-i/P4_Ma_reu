package com.neige_i.mareu.view.list;

import android.view.View;
import android.widget.DatePicker;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.neige_i.mareu.R;
import com.neige_i.mareu.view.add.AddActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ListActivityTest {

    @Rule
    public ActivityScenarioRule<ListActivity> activityRule = new ActivityScenarioRule<>(ListActivity.class);

    @Before
    public void setUp() {
        init();
    }

    @Test
    public void onActivityStarted_checkInitialState() {
        // Given: initial state
        // When: nothing
        // Then: list is empty
        onView(withId(R.id.list_meeting)).check(matches(hasChildCount(0)));
    }

    @Test
    public void onMenuItemClicked_showOrHideLayout() {
        // Given: initial state

        // When: click on filter item menu
        onView(withId(R.id.filter)).perform(click());

        // Then: filter layout is displayed
        onView(withId(R.id.filter_drawer)).check(matches(isDisplayed()));

        // When: click on filter item menu again
        onView(withId(R.id.filter)).perform(click());

        // Then: filter layout is hidden
        onView(withId(R.id.filter_drawer)).check(matches(not(isDisplayed())));
    }

    @Test
    public void onAddButtonClicked_startActivity() {
        // Given: initial state

        // When: click on 'add' button
        onView(withId(R.id.add_meeting)).perform(click());

        // Then: AddActivity is started
        intended(hasComponent(AddActivity.class.getName()));
    }

    @Test
    public void onAddButtonLongClicked_showItems() {
        // Given: initial state

        // When: long click on 'add' button
        onView(withId(R.id.add_meeting)).perform(longClick());

        // Then: list contains the 6 dummy meetings
        onView(withId(R.id.list_meeting)).check(matches(hasChildCount(6)));
    }

    @Test
    public void onDeleteMeeting_removeItem() {
        // Given: add 6 dummy meetings
        onView(withId(R.id.add_meeting)).perform(longClick());

        // When: click on 'remove' button of the first meeting
        // ASKME: perform click twice
        onView(withId(R.id.list_meeting)).perform(actionOnItemAtPosition(0, deleteMeeting())).perform(click());

        // Then: list does not contain 'Meeting A' anymore (which is the first meeting's topic)
        onView(withId(R.id.list_meeting))
            .check(matches(hasChildCount(5)))
            .check(matches(not(hasDescendant(withText("Meeting A")))));
    }

    @Test
    public void onSelectStartDateFilter_showOnlyMeetingAfter() {
        // Given: add 6 dummy meetings and open filter layout
        onView(withId(R.id.add_meeting)).perform(longClick());
        onView(withId(R.id.filter)).perform(click());

        // When: click on the start date filter, select a date and validate
        onView(withId(R.id.start_date_filter_input)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 10, 19));
        onView(withId(android.R.id.button1)).perform(click());

        // Then: list only contains 'Meeting E' & 'Meeting F' which date are from 19/10/2020
        onView(withId(R.id.list_meeting))
            .check(matches(hasChildCount(2)))
            .check(matches(not(hasDescendant(withText("Meeting A")))))
            .check(matches(not(hasDescendant(withText("Meeting B")))))
            .check(matches(not(hasDescendant(withText("Meeting C")))))
            .check(matches(not(hasDescendant(withText("Meeting D")))))
            .check(matches(hasDescendant(withText("Meeting E"))))
            .check(matches(hasDescendant(withText("Meeting F"))));
    }

    @Test
    public void onSelectEndDateFilter_showOnlyMeetingBefore() {
        // Given: add 6 dummy meetings and open filter layout
        onView(withId(R.id.add_meeting)).perform(longClick());
        onView(withId(R.id.filter)).perform(click());

        // When: click on the end date filter, select a date and validate
        onView(withId(R.id.end_date_filter_input)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 10, 17));
        onView(withId(android.R.id.button1)).perform(click());

        // Then: list does not contain 'Meeting E' & 'Meeting F' which dates are after 17/10/2020
        onView(withId(R.id.list_meeting))
            .check(matches(hasChildCount(4)))
            .check(matches(hasDescendant(withText("Meeting A"))))
            .check(matches(hasDescendant(withText("Meeting B"))))
            .check(matches(hasDescendant(withText("Meeting C"))))
            .check(matches(hasDescendant(withText("Meeting D"))))
            .check(matches(not(hasDescendant(withText("Meeting E")))))
            .check(matches(not(hasDescendant(withText("Meeting F")))));
    }

    @Test
    public void onClearStartDateFilter_showAllMeetings() {
        // Given: add 6 dummy meetings, open filter layout and select a start date filter
        onView(withId(R.id.add_meeting)).perform(longClick());
        onView(withId(R.id.filter)).perform(click());
        onView(withId(R.id.start_date_filter_input)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 10, 17));
        onView(withId(android.R.id.button1)).perform(click());

        // When: click on 'clear' of the start date filter
        onView(withId(R.id.start_date_filter_layout)).perform(clickEndIcon());
    }

    @Test
    public void onSelectPlaceFilter_showOnlyMeeting() {
        // Given: add 6 dummy meetings and open filter layout
        onView(withId(R.id.add_meeting)).perform(longClick());
        onView(withId(R.id.filter)).perform(click());

        // When: click on the 2nd place filter (which corresponds to the Luigi place)
        // ASKME: apparently selects 2 places
        onView(withId(R.id.place_list)).perform(actionOnItemAtPosition(1, selectPlace())).perform(click());

        // Then: list only contains 'Meeting B' which place is Luigi
        onView(withId(R.id.list_meeting))
            .check(matches(hasChildCount(1)))
            .check(matches(hasDescendant(withText("Meeting B"))));
    }

    private ViewAction deleteMeeting() {
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
                view.findViewById(R.id.delete_meeting).performClick();
            }
        };
    }

    private ViewAction clickEndIcon() {
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
                view.findViewById(R.id.text_input_end_icon).performClick();
            }
        };
    }

    private ViewAction selectPlace() {
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
                view.findViewById(R.id.place_button).performClick();
            }
        };
    }
}