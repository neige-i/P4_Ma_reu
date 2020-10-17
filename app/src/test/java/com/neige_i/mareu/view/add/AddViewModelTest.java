package com.neige_i.mareu.view.add;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DI;
import com.neige_i.mareu.view.add.model.MeetingUi;
import com.neige_i.mareu.view.add.model.MemberUi;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class AddViewModelTest {

    // ---------- TEST RULE

    // Avoid error when MutableLiveData.setValue() is called in source code
    @Rule
    public final TestRule rule = new InstantTaskExecutorRule();

    // ---------- REQUIRED DATA TO INITIALIZE THE VIEW MODEL

//    @NonNull
//    private final MeetingRepository repository = DI.getRepository();
    @NonNull
    private final List<String> availableMembers = DI.getAvailableMembers();
    @NonNull
    private final MutableLiveData<MeetingUi> meetingUi = DI.getMeetingUi();
    @NonNull
    private final LocalDate defaultDate = LocalDate.of(2020, 9, 20);
    @NonNull
    private final LocalTime defaultTime = LocalTime.of(20, 10);
    @NonNull
    private final ZoneId defaultZoneId = ZoneId.systemDefault();
    @Mock
    private final Context context = Mockito.mock(Context.class);

    // ---------- OBJECT UNDER TEST

    @NonNull
    private final AddViewModel viewModel = new AddViewModel(DI.getRepository(), meetingUi, Clock.fixed(
        ZonedDateTime.of(defaultDate, defaultTime, defaultZoneId).toInstant(),
        defaultZoneId
    ));

    // ---------- OTHER LOCAL VARIABLE

    @NonNull
    private final String NON_NULL = "arbitrary non-null value";

    // ---------- TEST TOPIC

    // ASKME: duplicate code
    @Test
    public void onTopicChanged_nonEmptyString_withoutError() {
        // Given: a null topic error
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            /*this*/ null, null, null, null, null,
            new ArrayList<>()
        ));

        // When: the topic is changed to a non-empty String
        final String expectedTopic = "Some value";
        viewModel.onTopicChanged(expectedTopic);

        // Then: the topic value is updated and the error remains null
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(expectedTopic, meetingUi.getTopic());
        assertNull(meetingUi.getTopicError());
    }

    @Test
    public void onTopicChanged_emptyString_withoutError() {
        // Given: a null topic error
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            /*this*/ null, null, null, null, null,
            new ArrayList<>()
        ));

        // When: the topic is changed to an empty String (or something similar)
        final String expectedTopic = "   ";
        viewModel.onTopicChanged(expectedTopic);

        // Then: the topic value is updated and the error remains null
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(expectedTopic, meetingUi.getTopic());
        assertNull(meetingUi.getTopicError());
    }

    @Test
    public void onTopicChanged_nonEmptyString_withError() {
        // Given: a non-null topic error
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            NON_NULL, null, null, null, null,
            new ArrayList<>()
        ));
        // ASKME: using repo to get an initial state of the LiveData instead of calling ViewModel's method
//        viewModel.onAddMeeting(context);

        // When: the topic is changed to a non-empty String
        final String expectedTopic = "Some value";
        viewModel.onTopicChanged(expectedTopic);

        // Then: the topic value is updated and the error is removed (i.e. equals null)
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(expectedTopic, meetingUi.getTopic());
        assertNull(meetingUi.getTopicError());
    }

    @Test
    public void onTopicChanged_emptyString_withError() {
        // Given: a non-null topic error
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            NON_NULL, null, null, null, null,
            new ArrayList<>()
        ));

        // When: the topic is changed to an empty String (or something similar)
        final String expectedTopic = "   ";
        viewModel.onTopicChanged(expectedTopic);

        // Then: the topic value is updated but the error is not removed
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(expectedTopic, meetingUi.getTopic());
        assertNotNull(meetingUi.getTopicError());
    }

    // ---------- TEST DATE

    @Test
    public void onDateClicked_empty() {
        // Given: a date which has not been set yet
        meetingUi.setValue(new MeetingUi(
            "", /*this*/ "", "", "", "",
            null, null, null, null, null,
            new ArrayList<>()
        ));

        // When: the date input is clicked
        viewModel.onDateClicked();

        // Then: the date picker equals the default date value
        assertEquals(defaultDate, viewModel.getDatePicker().getValue());
    }

    @Test
    public void onDateClicked_nonEmpty() {
        // Given: a date which has been set
        meetingUi.setValue(new MeetingUi(
            "", "12/10/2020", "", "", "",
            null, null, null, null, null,
            new ArrayList<>()
        ));

        // When: the date input is clicked
        viewModel.onDateClicked();

        // Then: the date picker equals the expected date value
        assertEquals(LocalDate.of(2020, 10, 12), viewModel.getDatePicker().getValue());
    }

    @Test
    public void onDateChanged_withoutError() {
        // Given: a null date error
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, /*this*/ null, null, null, null,
            new ArrayList<>()
        ));

        // When: the date value is changed
        viewModel.onDateChanged(2020, 10, 12);

        // Then: the date value is updated and the error remains null
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        // ASKME: hard-coded LocalDate instead
//        final String expectedDate = LocalDate.of(2020, 10, 12).format(DATE_FORMAT);
//        assertEquals(expectedDate, meetingUi.getDate());
        assertEquals("12/10/2020", meetingUi.getDate());
        assertNull(meetingUi.getDateError());
    }

    @Test
    public void onDateChanged_withError() {
        // Given: a non-null date error
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, NON_NULL, null, null, null,
            new ArrayList<>()
        ));

        // When: the date value is changed
        viewModel.onDateChanged(2020, 10, 12);

        // Then: the date value is updated and the error is removed
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals("12/10/2020", meetingUi.getDate());
        assertNull(meetingUi.getDateError());
    }

    // ---------- TEST TIME

    @Test
    public void onTimeClicked_emptyStart_nonEmptyEnd() {
        // Given: a start time which has not been set yet and an end time which has been set
        meetingUi.setValue(new MeetingUi(
            "", "", /*this*/ "", "14:05", "",
            null, null, null, null, null,
            new ArrayList<>()
        ));

        // When: the start time input is clicked
        viewModel.onTimeClicked(R.id.start_time_input);

        // Then: the time picker equals the default date value
        assertEquals(defaultTime, viewModel.getTimePicker().getValue());

        // ASKME: 2 When-Then

        // When: the end time input is clicked
        viewModel.onTimeClicked(R.id.end_time_input);

        // Then: the time picker equals the expected time value
        assertEquals(LocalTime.of(14, 5), viewModel.getTimePicker().getValue());
    }

    @Test
    public void onTimeClicked_nonEmptyStart_emptyEnd() {
        // Given: a start time which has been set and an end time which has not been set yet
        meetingUi.setValue(new MeetingUi(
            "", "", "14:15", /*this*/ "", "",
            null, null, null, null, null,
            new ArrayList<>()
        ));

        // When: the start time input is clicked
        viewModel.onTimeClicked(R.id.start_time_input);

        // Then: the time picker equals the expected time value
        assertEquals(LocalTime.of(14, 15), viewModel.getTimePicker().getValue());

        // When: the end time input is clicked
        viewModel.onTimeClicked(R.id.end_time_input);

        // Then: the time picker equals the default date value
        assertEquals(defaultTime, viewModel.getTimePicker().getValue());
    }

    @Test
    public void onTimeChanged_start_withoutError() {
        // Given: the start time input being clicked and a null start time error
        viewModel.onTimeClicked(R.id.start_time_input);
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, /*this*/ null, null, null,
            new ArrayList<>()
        ));

        // When: the start time value is changed
        viewModel.onTimeChanged(14, 20, context);

        // Then: the start time value is updated and the error remains null
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals("14:20", meetingUi.getStartTime());
        assertNull(meetingUi.getStartTimeError());
    }

    @Test
    public void onTimeChanged_end_withoutError() {
        // Given: the end time input being clicked and a null end time error
        viewModel.onTimeClicked(R.id.end_time_input);
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, null, /*this*/ null, null,
            new ArrayList<>()
        ));

        // When: the end time value is changed
        viewModel.onTimeChanged(14, 20, context);

        // Then: the end time value is updated and the error remains null
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals("14:20", meetingUi.getEndTime());
        assertNull(meetingUi.getEndTimeError());
    }

    @Test
    public void onTimeChanged_start_withError() {
        // Given: the start time input being clicked and a non-null start time error
        viewModel.onTimeClicked(R.id.start_time_input);
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, NON_NULL, null, null,
            new ArrayList<>()
        ));

        // When: the start time value is changed
        viewModel.onTimeChanged(14, 20, context);

        // Then: the start time value is updated and the error is removed
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals("14:20", meetingUi.getStartTime());
        assertNull(meetingUi.getStartTimeError());
    }

    @Test
    public void onTimeChanged_end_withError() {
        // Given: the end time input being clicked and a non-null end time error
        viewModel.onTimeClicked(R.id.end_time_input);
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, null, NON_NULL, null,
            new ArrayList<>()
        ));

        // When: the end time value is changed
        viewModel.onTimeChanged(14, 20, context);

        // Then: the end time value is updated and the error is removed
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals("14:20", meetingUi.getEndTime());
        assertNull(meetingUi.getEndTimeError());
    }

    @Test
    public void onTimeChanged_startBeforeEnd() {
        // Given: the start time input being clicked and a preset end time value
        viewModel.onTimeClicked(R.id.start_time_input);
        setEndValueTo14_30();

        // When: the start time value is changed to a time before the end one
        viewModel.onTimeChanged(14, 29, context);

        // Then: the start time value is updated and the error remains null
        MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals("14:29", meetingUi.getStartTime());
        assertNull(meetingUi.getStartTimeError());
    }

    @Test
    public void onTimeChanged_startEqualsEnd() {
        // Given: the start time input being clicked and a preset end time value
        viewModel.onTimeClicked(R.id.start_time_input);
        setEndValueTo14_30();

        // When: the start time value is changed to the same time as the end one
        // FIXME: incorrect mock
        when(context.getString(R.string.start_time_error, meetingUi.getValue().getEndTime())).thenReturn("");
        viewModel.onTimeChanged(14, 30, context);

        // Then: the start time value is updated and an error is triggered
        MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals("14:30", meetingUi.getStartTime());
        assertNotNull(meetingUi.getStartTimeError());
    }

    @Test
    public void onTimeChanged_startAfterEnd() {
        // Given: the start time input being clicked and a preset end time value
        viewModel.onTimeClicked(R.id.start_time_input);
        setEndValueTo14_30();

        // When: the start time value is changed to a time after the end one
        when(context.getString(R.string.start_time_error, meetingUi.getValue().getEndTime())).thenReturn("");
        viewModel.onTimeChanged(14, 31, context);

        // Then: the start time value is updated and an error is triggered
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals("14:31", meetingUi.getStartTime());
        assertNotNull(meetingUi.getStartTimeError());
    }

    private void setEndValueTo14_30() {
        meetingUi.setValue(new MeetingUi(
            "", "", "", "14:30", "",
            null, null, null, null, null,
            new ArrayList<>()
        ));
    }

    @Test
    public void onTimeChanged_endAfterStart() {
        // Given: the end time input being clicked and a preset start time value
        viewModel.onTimeClicked(R.id.end_time_input);
        setStartValueTo14_30();

        // When: the end time value is changed to a time after the start one
        viewModel.onTimeChanged(14, 31, context);

        // Then: the end time value is updated and the error remains null
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals("14:31", meetingUi.getEndTime());
        assertNull(meetingUi.getEndTimeError());
    }

    @Test
    public void onTimeChanged_endEqualsStart() {
        // Given: the end time input being clicked and a preset start time value
        viewModel.onTimeClicked(R.id.end_time_input);
        setStartValueTo14_30();

        // When: the end time value is changed to the same time as the start one
        when(context.getString(R.string.end_time_error, meetingUi.getValue().getStartTime())).thenReturn("");
        viewModel.onTimeChanged(14, 30, context);

        // Then: the end time value is updated and an error is triggered
        MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals("14:30", meetingUi.getEndTime());
        assertNotNull(meetingUi.getEndTimeError());
    }

    @Test
    public void onTimeChanged_endBeforeStart() {
        // Given: the end time input being clicked and a preset start time value
        viewModel.onTimeClicked(R.id.end_time_input);
        setStartValueTo14_30();

        // When: the end time value is changed to a time before the start one
        when(context.getString(R.string.end_time_error, meetingUi.getValue().getStartTime())).thenReturn("");
        viewModel.onTimeChanged(14, 29, context);

        // Then: the end time value is updated and an error is triggered
        MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals("14:29", meetingUi.getEndTime());
        assertNotNull(meetingUi.getEndTimeError());
    }

    private void setStartValueTo14_30() {
        meetingUi.setValue(new MeetingUi(
            "", "", "14:30", "", "",
            null, null, null, null, null,
            new ArrayList<>()
        ));
    }

    // ---------- TEST PLACE

    @Test
    public void onPlaceChanged_withoutError() {
        // Given: a null place error
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, null, null, /*this*/ null,
            new ArrayList<>()
        ));

        // When: the place value is changed
        final String expectedPlace = "place";
        viewModel.onPlaceChanged(expectedPlace);

        // Then: the place value is updated and the error remains null
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(expectedPlace, meetingUi.getPlace());
        assertNull(meetingUi.getPlaceError());
    }

    @Test
    public void onPlaceChanged_withError() {
        // Given: a non-null place error
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, null, null, NON_NULL,
            new ArrayList<>()
        ));

        // When: the place value is changed
        final String expectedPlace = "place";
        viewModel.onPlaceChanged(expectedPlace);

        // Then: the date value is updated and the error is removed
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(expectedPlace, meetingUi.getPlace());
        assertNull(meetingUi.getPlaceError());
    }

    // ---------- TEST MEMBER LIST

    @Test
    public void onAddMember_correctPosition() {
        // Given: a member list with 4 elements
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, null, null, null,
            // A member with ID = 0 is automatically created in DI
            new ArrayList<>(Arrays.asList(
                new MemberUi("", null, View.VISIBLE), // ID = 1
                new MemberUi("", null, View.VISIBLE), // ID = 2
                new MemberUi("", null, View.VISIBLE), // ID = 3
                new MemberUi("", null, View.VISIBLE) // ID = 4
            ))
        ));

        // When: a member is added after the second element
        viewModel.onAddMember(1); // ID = 5

        // FIXME: correct assertion, expected member and not index
        // Then: the member list contains the last added member (with ID 5) at the 3rd position
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(5, meetingUi.getMemberList().size());
        assertEquals(2, meetingUi.getMemberList().indexOf(new MemberUi(5, "", null, View.VISIBLE)));
    }

    @Test
    public void onAddMember_changeButtonVisibility() {
        // Given: a member list with an only element
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, null, null, null,
            new ArrayList<>(Collections.singletonList(new MemberUi("", null, View.INVISIBLE)))
        ));

        // When: a member is added to the list
        viewModel.onAddMember(0);

        // Then: the member list size has increased and the first element's button visibility has changed
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(2, meetingUi.getMemberList().size());
        assertEquals(View.VISIBLE, meetingUi.getMemberList().get(0).getRemoveButtonVisibility());
    }

    @Test
    public void onRemoveMember_nonEmptyEmail() {
        // Given: a member list with 3 elements and a preset list of available members
        final MemberUi memberToRemove = new MemberUi(10, "maxime@lamzone.com", null, View.VISIBLE);
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, null, null, null,
            new ArrayList<>(Arrays.asList(
                memberToRemove,
                new MemberUi(11, "", null, View.VISIBLE),
                new MemberUi(12, "", null, View.VISIBLE)
            ))
        ));
        initAvailableMembers("maxime@lamzone.com"); // availableMembers size = 7

        // When: a member (with a non-empty email) is removed from the list
        viewModel.onRemoveMember(memberToRemove);

        // Then: the member list size has decreased and the available member list contains the removed member's email
        // FIXME: assert expectedList
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(2, meetingUi.getMemberList().size());
        assertFalse(meetingUi.getMemberList().contains(memberToRemove));
        assertEquals(8, availableMembers.size());
        assertTrue(availableMembers.contains("maxime@lamzone.com"));
    }

    @Test
    public void onRemoveMember_emptyEmail() {
        // Given: a member list with an 2 elements and the default list of available members
        final MemberUi memberToRemove = new MemberUi(21, "", null, View.VISIBLE);
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, null, null, null,
            new ArrayList<>(Arrays.asList(
                new MemberUi(21, "", null, View.VISIBLE),
                new MemberUi(22, "", null, View.VISIBLE)
            ))
        ));
        

        // When: a member (with an empty email) is removed from the list
        viewModel.onRemoveMember(memberToRemove);

        // Then: the member list size has decreased but the available member list is unchanged
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(1, meetingUi.getMemberList().size());
        assertFalse(meetingUi.getMemberList().contains(memberToRemove));
        assertEquals(3, availableMembers.size());
        assertFalse(availableMembers.contains(""));
    }

    @Test
    public void onRemoveMember_changeButtonVisibility() {
        // Given: a member list with an 2 elements
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, null, null, null,
            new ArrayList<>(Arrays.asList(
                new MemberUi(50, "", null, View.VISIBLE),
                new MemberUi(51, "", null, View.VISIBLE)
            ))
        ));

        // When: a member is removed from the list
        final MemberUi memberToRemove = new MemberUi(50, "", null, View.VISIBLE);
        viewModel.onRemoveMember(memberToRemove);

        // Then: the member list size has decreased and the first element's button visibility has changed
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(1, meetingUi.getMemberList().size());
        assertFalse(meetingUi.getMemberList().contains(memberToRemove));
        assertEquals(View.INVISIBLE, meetingUi.getMemberList().get(0).getRemoveButtonVisibility());
    }

    @Test
    public void onUpdateMember_withoutError() {
        // Given: a null email error
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, null, null, null,
            new ArrayList<>(Collections.singletonList(new MemberUi("", null, View.INVISIBLE)))
        ));

        // When: the email value is changed
        final String expectedEmail = "email";
        viewModel.onUpdateMember(0, expectedEmail);

        // Then: the email value is updated and the error remains null
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(expectedEmail, meetingUi.getMemberList().get(0).getEmail());
        assertNull(meetingUi.getMemberList().get(0).getEmailError());
    }

    @Test
    public void onUpdateMember_withError() {
        // Given: a non-null email error
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, null, null, null,
            new ArrayList<>(Collections.singletonList(new MemberUi("", NON_NULL, View.INVISIBLE)))
        ));

        // When: the email value is changed
        final String expectedEmail = "email";
        viewModel.onUpdateMember(0, expectedEmail);

        // Then: the email value is updated and the error is removed
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(expectedEmail, meetingUi.getMemberList().get(0).getEmail());
        assertNull(meetingUi.getMemberList().get(0).getEmailError());
    }

    @Test
    public void onUpdateMember_nonEmptyOldEmail() {
        // Given: a member list with an 3 elements and a preset list of available members
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, null, null, null,
            new ArrayList<>(Arrays.asList(
                new MemberUi(60, "email0", null, View.VISIBLE),
                new MemberUi(61, "", null, View.VISIBLE),
                new MemberUi(62, "", null, View.VISIBLE)
            ))
        ));

        // When: the first member's email (which is non_empty) is changed with the second value of the available members
        MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        final String expectedEmail = availableMembers.get(1);
        viewModel.onUpdateMember(0, expectedEmail);

        // Then: the email value is updated and the available member list is changed
        meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(expectedEmail, meetingUi.getMemberList().get(0).getEmail());
        assertEquals(3, availableMembers.size());
        assertFalse(availableMembers.contains(expectedEmail));
        assertTrue(availableMembers.contains("email0"));
    }

    @Test
    public void onUpdateMember_emptyOldEmail() {
        // Given: a member list with an 3 elements and a preset list of available members
        meetingUi.setValue(new MeetingUi(
            "", "", "", "", "",
            null, null, null, null, null,
            new ArrayList<>(Arrays.asList(
                new MemberUi(70, "email0", null, View.VISIBLE),
                new MemberUi(71, "", null, View.VISIBLE),
                new MemberUi(72, "", null, View.VISIBLE)
            ))
        ));

        // When: the second member's email (which is non_empty) is changed with the first value of the available members
        MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        final String expectedEmail = availableMembers.get(0);
        viewModel.onUpdateMember(1, expectedEmail);

        // Then: the email value is updated and the available member list is changed
        meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertEquals(expectedEmail, meetingUi.getMemberList().get(1).getEmail());
        assertEquals(2, availableMembers.size());
        assertFalse(availableMembers.contains(expectedEmail));
        assertFalse(availableMembers.contains(""));
    }

    private void initAvailableMembers(String... email) {
        availableMembers.removeAll(Arrays.asList(email));
    }
/*
    @Test
    public void onUpdateMember() {
        // Given: change the first member's email
        viewModel.onAddMember(0);
        viewModel.onUpdateMember(0, "firstEmail");

        // When: change the 1st member's email
        viewModel.onUpdateMember(0, "newEmail");

        // Then: ...
        final List<MemberUi> memberUiList = viewModel.getMeetingUiLiveData().getValue().getMemberList();
        final List<String> availableMembers = viewModel.getMeetingUiLiveData().getValue().getAvailableMembers();
        assertTrue(availableMembers.contains("firstEmail")); // ...the available members contains the member's old email
        assertFalse(availableMembers.contains("newEmail")); // ...the available members doesn't contain the member's new email
        assertEquals("newEmail", memberUiList.get(0).getEmail()); // ...the first member's email is changed

    }

    @Test
    public void onAddMeeting_allFieldsOk() {
        //Given: fill the required fields
        viewModel.onTopicChanged("topic");
        viewModel.onDateChanged(2020, 10, 1);
        viewModel.onTimeChanged(23, 59);
        viewModel.onPlaceChanged("place");
        viewModel.onUpdateMember(0, "email");

        // When: add the meeting
        viewModel.onAddMeeting();

        // Then: the repository changes
        final List<Meeting> meetingList = repository.getAllMeetings().getValue();
        final Meeting expectedMeeting = new Meeting(
            0,
            "topic",
            "place",
            LocalDateTime.of(2020, 10, 1, 23, 59),
            Collections.singletonList("email")
        );
        assertEquals(1, meetingList.size());
        assertEquals(expectedMeeting, meetingList.get(0));
    }

    @Test
    public void onAddMeeting_allFieldsWrong() {
        // When: add meeting while all fields aren't filled
        viewModel.onAddMeeting();

        // Then: the Snackbar shows a specific message
        assertEquals(R.string.mandatory_fields, viewModel.getShowSnack().getValue().intValue());
    }

    @Test
    public void onAddMeeting_meetingAlreadyExisting() {
        // Given: fill the required fields with the same place and date/time as the meeting which is contained in repository
        viewModel.onTopicChanged("new topic");
        viewModel.onDateChanged(2020, 10, 1);
        viewModel.onStartTimeValidated(23, 59);
        viewModel.onPlaceChanged("place");
        viewModel.onUpdateMember(0, "new email");
        repository.addMeeting(new Meeting(
            "topic",
            "place",
            LocalDateTime.of(2020, 10, 1, 23, 59),
            Collections.singletonList("email")
        ));

        // When: add the meeting
        viewModel.onAddMeeting();

        // Then: the Snackbar shows a specific message
        assertEquals(R.string.meeting_already_exist, viewModel.getShowSnack().getValue().intValue());
    }
 */
}