package com.neige_i.mareu.view.add;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DI;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnitParamsRunner.class)
public class AddViewModelTest {

    // ------------------------------------ TEST RULE VARIABLE -------------------------------------

    // Avoid error when MutableLiveData.setValue() is called in source code
    @Rule
    public final TestRule rule = new InstantTaskExecutorRule();

    // ------------------------------------- OBJECT UNDER TEST -------------------------------------

    @NonNull
    private static final LocalDate defaultDate = LocalDate.of(2020, 9, 20);
    @NonNull
    private static final LocalTime defaultTime = LocalTime.of(20, 10);

    @NonNull
    private final AddViewModel viewModel = new AddViewModel(
        DI.getRepository(),
        Clock.fixed(
            ZonedDateTime.of(defaultDate, defaultTime, ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
        )
    );

    // --------------------------------------- MOCK VARIABLE ---------------------------------------

    @Mock
    private static final Context context = Mockito.mock(Context.class);

    // -------------------------------------- LOCAL VARIABLE ---------------------------------------

    private static final String ERROR_MESSAGE = " ";

//    @NonNull
//    private final String NON_NULL = "arbitrary non-null value";

    // ---------------------------------------- TEST TOPIC -----------------------------------------

    @Test
    @Parameters(method = "changeTopic")
    public void onTopicChanged_test(boolean isErrorTriggered, String topic, String expectedError) {
        // Given: error was triggered or not
        if (isErrorTriggered)
            viewModel.onAddMeeting(context);

        // When: topic is changing
        viewModel.setTopic(topic);

        // Then: LiveData is updated
        assertThat(viewModel.getMeetingUiModel().getValue().getTopic(), is(topic));
        assertThat(viewModel.getMeetingUiModel().getValue().getTopicError(), is(expectedError));
    }

    /**
     * If error was not triggered (first two lines), it remains null whatever the topic value.<br />
     * Otherwise, the error is removed only if the topic is a non-empty String.
     */
    private Object[] changeTopic() {
        return new Object[]{
            // isErrorTriggered, topic, expectedError
            new Object[]{false, "   ", null},
            new Object[]{false, "some value", null},
            new Object[]{true, "   ", ERROR_MESSAGE},
            new Object[]{true, "some value", null},
            new Object[]{true, "another value", null},
            new Object[]{true, "another non-empty String", null},
        };
    }

    // ----------------------------------------- TEST DATE -----------------------------------------

    @Test
    @Parameters(method = "clickDate")
    public void onDateClicked_test(LocalDate currentDate, LocalDate expectedDate) {
        // Given: current date was already set or not
        if (currentDate != null)
            viewModel.setDate(
                currentDate.getYear(),
                currentDate.getMonthValue(),
                currentDate.getDayOfMonth()
            );

        // When: date is being clicked
        viewModel.showDatePickerDialog();

        // Then: SingleLiveEvent is updated
        assertThat(viewModel.getDatePickerEvent().getValue(), is(expectedDate));
    }

    /**
     * If the current date was already set, the expected one is the same.<br />
     * Otherwise, the expected date equals the default one.
     */
    private Object[] clickDate() {
        return new Object[]{
            // currentDate, expectedDate
            new Object[]{LocalDate.of(2020, 10, 12), LocalDate.of(2020, 10, 12)},
            new Object[]{null, defaultDate},
        };
    }

    @Test
    @Parameters(method = "changeDate")
    public void onDateChanged_test(boolean isErrorTriggered, int year, int month, int day,
                                   String expectedDate, String expectedError
    ) {
        // Given: error was triggered or not
        if (isErrorTriggered)
            viewModel.onAddMeeting(context);

        // When: date is changing
        viewModel.setDate(year, month, day);

        // Then: LiveData is updated
        assertThat(viewModel.getMeetingUiModel().getValue().getDate(), is(expectedDate));
        assertThat(viewModel.getMeetingUiModel().getValue().getDateError(), is(expectedError));
    }

    /**
     * The expected date always equals the String representation of the year, month and day.<br />
     * The expected error is always null after changing the date, whatever is was triggered or not.
     */
    private Object[] changeDate() {
        return new Object[]{
            // isErrorTriggered, year, month, day, expectedDate, expectedError
            new Object[]{false, 2020, 10, 12, "12/10/2020", null},
            new Object[]{false, 2020, 10, 20, "20/10/2020", null},
            new Object[]{true, 2020, 7, 3, "03/07/2020", null},
            new Object[]{true, 2020, 8, 5, "05/08/2020", null},
        };
    }

    // ----------------------------------------- TEST TIME -----------------------------------------

    @Test
    @Parameters(method = "clickTime")
    public void onTimeClicked_test(int timeInputId, LocalTime currentTime, LocalTime expectedTime) {
        // Given: current time was already set or not
        if (currentTime != null) {
            viewModel.showTimePickerDialog(timeInputId);
            viewModel.setTime(currentTime.getHour(), currentTime.getMinute(), context);
        }

        // When: time is being clicked
        viewModel.showTimePickerDialog(timeInputId);

        // Then: SingleLiveEvent is updated
        assertThat(viewModel.getTimePickerEvent().getValue(), is(expectedTime));
    }

    /**
     * If the current time was already set, the expected one is the same.<br />
     * Otherwise, the expected time equals the default one.
     */
    private Object[] clickTime() {
        return new Object[]{
            // timeInputId, currentTime, expectedTime
            new Object[]{R.id.start_time_input, LocalTime.of(14, 5), LocalTime.of(14, 5)},
            new Object[]{R.id.start_time_input, null, defaultTime},
            new Object[]{R.id.end_time_input, LocalTime.of(15, 46), LocalTime.of(15, 46)},
            new Object[]{R.id.end_time_input, null, defaultTime},
        };
    }

    @Test
    @Parameters(method = "changeStartTime")
    public void onTimeChanged_testStart(boolean isErrorTriggered, int hour, int minute,
                                        String expectedTime, String expectedError
    ) {
        // Given: end time was set to 13:15 and error was triggered or not
        viewModel.showTimePickerDialog(R.id.end_time_input);
        viewModel.setTime(13, 15, context);
        if (isErrorTriggered)
            viewModel.onAddMeeting(context);
        viewModel.showTimePickerDialog(R.id.start_time_input);

        // When: start time is changing
        viewModel.setTime(hour, minute, context);

        // Then: LiveData is updated
        assertThat(viewModel.getMeetingUiModel().getValue().getStartTime(), is(expectedTime));
        assertThat(viewModel.getMeetingUiModel().getValue().getStartTimeError(), is(expectedError));
    }

    /**
     * The expected date always equals the String representation of the year, month and day.<br />
     * The expected error is always null after changing the date, whatever is was triggered or not.
     */
    private Object[] changeStartTime() {
        return new Object[]{
            // isErrorTriggered, hour, minute, expectedTime, expectedError
            new Object[]{false, 9, 6, "09:06", null},
            new Object[]{false, 9, 6, "09:06", null},
            new Object[]{true, 9, 6, "09:06", null},
            new Object[]{true, 9, 6, "09:06", null},
        };
    }
//
//    @Test
//    public void onTimeClicked_emptyStart_nonEmptyEnd() {
//        // Given: a start time which has not been set yet and an end time which has been set
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", *//*this*//* "", "14:05", "",
//            null, null, null, null, null,
//            new ArrayList<>()
//        ));
//
//        // When: the start time input is clicked
//        viewModel.onTimeClicked(R.id.start_time_input);
//
//        // Then: the time picker equals the default date value
//        assertEquals(defaultTime, viewModel.getTimePicker().getValue());
//
//        // When: the end time input is clicked
//        viewModel.onTimeClicked(R.id.end_time_input);
//
//        // Then: the time picker equals the expected time value
//        assertEquals(LocalTime.of(14, 5), viewModel.getTimePicker().getValue());
//    }
//
//    @Test
//    public void onTimeClicked_nonEmptyStart_emptyEnd() {
//        // Given: a start time which has been set and an end time which has not been set yet
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "14:15", *//*this*//* "", "",
//            null, null, null, null, null,
//            new ArrayList<>()
//        ));
//
//        // When: the start time input is clicked
//        viewModel.onTimeClicked(R.id.start_time_input);
//
//        // Then: the time picker equals the expected time value
//        assertEquals(LocalTime.of(14, 15), viewModel.getTimePicker().getValue());
//
//        // When: the end time input is clicked
//        viewModel.onTimeClicked(R.id.end_time_input);
//
//        // Then: the time picker equals the default date value
//        assertEquals(defaultTime, viewModel.getTimePicker().getValue());
//    }
//
//    @Test
//    public void onTimeChanged_start_withoutError() {
//        // Given: the start time input being clicked and a null start time error
//        viewModel.onTimeClicked(R.id.start_time_input);
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, *//*this*//* null, null, null,
//            new ArrayList<>()
//        ));
//
//        // When: the start time value is changed
//        viewModel.onTimeChanged(14, 20, context);
//
//        // Then: the start time value is updated and the error remains null
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals("14:20", meetingUiModel.getStartTime());
//        assertNull(meetingUiModel.getStartTimeError());
//    }
//
//    @Test
//    public void onTimeChanged_end_withoutError() {
//        // Given: the end time input being clicked and a null end time error
//        viewModel.onTimeClicked(R.id.end_time_input);
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, null, *//*this*//* null, null,
//            new ArrayList<>()
//        ));
//
//        // When: the end time value is changed
//        viewModel.onTimeChanged(14, 20, context);
//
//        // Then: the end time value is updated and the error remains null
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals("14:20", meetingUiModel.getEndTime());
//        assertNull(meetingUiModel.getEndTimeError());
//    }
//
//    @Test
//    public void onTimeChanged_start_withError() {
//        // Given: the start time input being clicked and a non-null start time error
//        viewModel.onTimeClicked(R.id.start_time_input);
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, NON_NULL, null, null,
//            new ArrayList<>()
//        ));
//
//        // When: the start time value is changed
//        viewModel.onTimeChanged(14, 20, context);
//
//        // Then: the start time value is updated and the error is removed
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals("14:20", meetingUiModel.getStartTime());
//        assertNull(meetingUiModel.getStartTimeError());
//    }
//
//    @Test
//    public void onTimeChanged_end_withError() {
//        // Given: the end time input being clicked and a non-null end time error
//        viewModel.onTimeClicked(R.id.end_time_input);
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, null, NON_NULL, null,
//            new ArrayList<>()
//        ));
//
//        // When: the end time value is changed
//        viewModel.onTimeChanged(14, 20, context);
//
//        // Then: the end time value is updated and the error is removed
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals("14:20", meetingUiModel.getEndTime());
//        assertNull(meetingUiModel.getEndTimeError());
//    }
//
//    @Test
//    public void onTimeChanged_startBeforeEnd() {
//        // Given: the start time input being clicked and a preset end time value
//        viewModel.onTimeClicked(R.id.start_time_input);
//        setEndValueTo14_30();
//
//        // When: the start time value is changed to a time before the end one
//        viewModel.onTimeChanged(14, 29, context);
//
//        // Then: the start time value is updated and the error remains null
//        MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals("14:29", meetingUiModel.getStartTime());
//        assertNull(meetingUiModel.getStartTimeError());
//    }
//
//    @Test
//    public void onTimeChanged_startEqualsEnd() {
//        // Given: the start time input being clicked and a preset end time value
//        viewModel.onTimeClicked(R.id.start_time_input);
//        setEndValueTo14_30();
//
//        // When: the start time value is changed to the same time as the end one
//        // FIXME: incorrect mock
//        when(context.getString(R.string.start_time_error, meetingUi.getValue().getEndTime())).thenReturn("");
//        viewModel.onTimeChanged(14, 30, context);
//
//        // Then: the start time value is updated and an error is triggered
//        MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals("14:30", meetingUiModel.getStartTime());
//        assertNotNull(meetingUiModel.getStartTimeError());
//    }
//
//    @Test
//    public void onTimeChanged_startAfterEnd() {
//        // Given: the start time input being clicked and a preset end time value
//        viewModel.onTimeClicked(R.id.start_time_input);
//        setEndValueTo14_30();
//
//        // When: the start time value is changed to a time after the end one
//        when(context.getString(R.string.start_time_error, meetingUi.getValue().getEndTime())).thenReturn("");
//        viewModel.onTimeChanged(14, 31, context);
//
//        // Then: the start time value is updated and an error is triggered
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals("14:31", meetingUiModel.getStartTime());
//        assertNotNull(meetingUiModel.getStartTimeError());
//    }
//
//    private void setEndValueTo14_30() {
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "14:30", "",
//            null, null, null, null, null,
//            new ArrayList<>()
//        ));
//    }
//
//    @Test
//    public void onTimeChanged_endAfterStart() {
//        // Given: the end time input being clicked and a preset start time value
//        viewModel.onTimeClicked(R.id.end_time_input);
//        setStartValueTo14_30();
//
//        // When: the end time value is changed to a time after the start one
//        viewModel.onTimeChanged(14, 31, context);
//
//        // Then: the end time value is updated and the error remains null
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals("14:31", meetingUiModel.getEndTime());
//        assertNull(meetingUiModel.getEndTimeError());
//    }
//
//    @Test
//    public void onTimeChanged_endEqualsStart() {
//        // Given: the end time input being clicked and a preset start time value
//        viewModel.onTimeClicked(R.id.end_time_input);
//        setStartValueTo14_30();
//
//        // When: the end time value is changed to the same time as the start one
//        when(context.getString(R.string.end_time_error, meetingUi.getValue().getStartTime())).thenReturn("");
//        viewModel.onTimeChanged(14, 30, context);
//
//        // Then: the end time value is updated and an error is triggered
//        MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals("14:30", meetingUiModel.getEndTime());
//        assertNotNull(meetingUiModel.getEndTimeError());
//    }
//
//    @Test
//    public void onTimeChanged_endBeforeStart() {
//        // Given: the end time input being clicked and a preset start time value
//        viewModel.onTimeClicked(R.id.end_time_input);
//        setStartValueTo14_30();
//
//        // When: the end time value is changed to a time before the start one
//        when(context.getString(R.string.end_time_error, meetingUi.getValue().getStartTime())).thenReturn("");
//        viewModel.onTimeChanged(14, 29, context);
//
//        // Then: the end time value is updated and an error is triggered
//        MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals("14:29", meetingUiModel.getEndTime());
//        assertNotNull(meetingUiModel.getEndTimeError());
//    }
//
//    private void setStartValueTo14_30() {
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "14:30", "", "",
//            null, null, null, null, null,
//            new ArrayList<>()
//        ));
//    }
//
//    // ---------- TEST PLACE
//
//    @Test
//    public void onPlaceChanged_withoutError() {
//        // Given: a null place error
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, null, null, *//*this*//* null,
//            new ArrayList<>()
//        ));
//
//        // When: the place value is changed
//        final String expectedPlace = "place";
//        viewModel.onPlaceChanged(expectedPlace);
//
//        // Then: the place value is updated and the error remains null
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals(expectedPlace, meetingUiModel.getPlace());
//        assertNull(meetingUiModel.getPlaceError());
//    }
//
//    @Test
//    public void onPlaceChanged_withError() {
//        // Given: a non-null place error
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, null, null, NON_NULL,
//            new ArrayList<>()
//        ));
//
//        // When: the place value is changed
//        final String expectedPlace = "place";
//        viewModel.onPlaceChanged(expectedPlace);
//
//        // Then: the date value is updated and the error is removed
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals(expectedPlace, meetingUiModel.getPlace());
//        assertNull(meetingUiModel.getPlaceError());
//    }
//
//    // ---------- TEST MEMBER LIST
//
//    @Test
//    public void onAddMember_correctPosition() {
//        // Given: a member list with 4 elements
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, null, null, null,
//            // A member with ID = 0 is automatically created in DI
//            new ArrayList<>(Arrays.asList(
//                new MemberUiModel("", null, View.VISIBLE), // ID = 1
//                new MemberUiModel("", null, View.VISIBLE), // ID = 2
//                new MemberUiModel("", null, View.VISIBLE), // ID = 3
//                new MemberUiModel("", null, View.VISIBLE) // ID = 4
//            ))
//        ));
//
//        // When: a member is added after the second element
//        viewModel.onAddMember(1); // ID = 5
//
//        // FIXME: correct assertion, expected member and not index
//        // Then: the member list contains the last added member (with ID 5) at the 3rd position
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals(5, meetingUiModel.getMemberList().size());
//        assertEquals(2, meetingUiModel.getMemberList().indexOf(new MemberUiModel(5, "", null, View.VISIBLE)));
//    }
//
//    @Test
//    public void onAddMember_changeButtonVisibility() {
//        // Given: a member list with an only element
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, null, null, null,
//            new ArrayList<>(Collections.singletonList(new MemberUiModel("", null, View.INVISIBLE)))
//        ));
//
//        // When: a member is added to the list
//        viewModel.onAddMember(0);
//
//        // Then: the member list size has increased and the first element's button visibility has changed
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals(2, meetingUiModel.getMemberList().size());
//        assertEquals(View.VISIBLE, meetingUiModel.getMemberList().get(0).getRemoveButtonVisibility());
//    }
//
//    @Test
//    public void onRemoveMember_nonEmptyEmail() {
//        // Given: a member list with 3 elements and a preset list of available members
//        final MemberUiModel memberToRemove = new MemberUiModel(10, "maxime@lamzone.com", null, View.VISIBLE);
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, null, null, null,
//            new ArrayList<>(Arrays.asList(
//                memberToRemove,
//                new MemberUiModel(11, "", null, View.VISIBLE),
//                new MemberUiModel(12, "", null, View.VISIBLE)
//            ))
//        ));
//        initAvailableMembers("maxime@lamzone.com"); // availableMembers size = 7
//
//        // When: a member (with a non-empty email) is removed from the list
//        viewModel.onRemoveMember(memberToRemove);
//
//        // Then: the member list size has decreased and the available member list contains the removed member's email
//        // FIXME: assert expectedList
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals(2, meetingUiModel.getMemberList().size());
//        assertFalse(meetingUiModel.getMemberList().contains(memberToRemove));
//        assertEquals(8, availableMembers.size());
//        assertTrue(availableMembers.contains("maxime@lamzone.com"));
//    }
//
//    @Test
//    public void onRemoveMember_emptyEmail() {
//        // Given: a member list with an 2 elements and the default list of available members
//        final MemberUiModel memberToRemove = new MemberUiModel(21, "", null, View.VISIBLE);
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, null, null, null,
//            new ArrayList<>(Arrays.asList(
//                new MemberUiModel(21, "", null, View.VISIBLE),
//                new MemberUiModel(22, "", null, View.VISIBLE)
//            ))
//        ));
//
//
//        // When: a member (with an empty email) is removed from the list
//        viewModel.onRemoveMember(memberToRemove);
//
//        // Then: the member list size has decreased but the available member list is unchanged
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals(1, meetingUiModel.getMemberList().size());
//        assertFalse(meetingUiModel.getMemberList().contains(memberToRemove));
//        assertEquals(3, availableMembers.size());
//        assertFalse(availableMembers.contains(""));
//    }
//
//    @Test
//    public void onRemoveMember_changeButtonVisibility() {
//        // Given: a member list with an 2 elements
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, null, null, null,
//            new ArrayList<>(Arrays.asList(
//                new MemberUiModel(50, "", null, View.VISIBLE),
//                new MemberUiModel(51, "", null, View.VISIBLE)
//            ))
//        ));
//
//        // When: a member is removed from the list
//        final MemberUiModel memberToRemove = new MemberUiModel(50, "", null, View.VISIBLE);
//        viewModel.onRemoveMember(memberToRemove);
//
//        // Then: the member list size has decreased and the first element's button visibility has changed
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals(1, meetingUiModel.getMemberList().size());
//        assertFalse(meetingUiModel.getMemberList().contains(memberToRemove));
//        assertEquals(View.INVISIBLE, meetingUiModel.getMemberList().get(0).getRemoveButtonVisibility());
//    }
//
//    @Test
//    public void onUpdateMember_withoutError() {
//        // Given: a null email error
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, null, null, null,
//            new ArrayList<>(Collections.singletonList(new MemberUiModel("", null, View.INVISIBLE)))
//        ));
//
//        // When: the email value is changed
//        final String expectedEmail = "email";
//        viewModel.onUpdateMember(0, expectedEmail);
//
//        // Then: the email value is updated and the error remains null
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals(expectedEmail, meetingUiModel.getMemberList().get(0).getEmail());
//        assertNull(meetingUiModel.getMemberList().get(0).getEmailError());
//    }
//
//    @Test
//    public void onUpdateMember_withError() {
//        // Given: a non-null email error
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, null, null, null,
//            new ArrayList<>(Collections.singletonList(new MemberUiModel("", NON_NULL, View.INVISIBLE)))
//        ));
//
//        // When: the email value is changed
//        final String expectedEmail = "email";
//        viewModel.onUpdateMember(0, expectedEmail);
//
//        // Then: the email value is updated and the error is removed
//        final MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals(expectedEmail, meetingUiModel.getMemberList().get(0).getEmail());
//        assertNull(meetingUiModel.getMemberList().get(0).getEmailError());
//    }
//
//    @Test
//    public void onUpdateMember_nonEmptyOldEmail() {
//        // Given: a member list with an 3 elements and a preset list of available members
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, null, null, null,
//            new ArrayList<>(Arrays.asList(
//                new MemberUiModel(60, "email0", null, View.VISIBLE),
//                new MemberUiModel(61, "", null, View.VISIBLE),
//                new MemberUiModel(62, "", null, View.VISIBLE)
//            ))
//        ));
//
//        // When: the first member's email (which is non_empty) is changed with the second value of the available members
//        MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        final String expectedEmail = availableMembers.get(1);
//        viewModel.onUpdateMember(0, expectedEmail);
//
//        // Then: the email value is updated and the available member list is changed
//        meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals(expectedEmail, meetingUiModel.getMemberList().get(0).getEmail());
//        assertEquals(3, availableMembers.size());
//        assertFalse(availableMembers.contains(expectedEmail));
//        assertTrue(availableMembers.contains("email0"));
//    }
//
//    @Test
//    public void onUpdateMember_emptyOldEmail() {
//        // Given: a member list with an 3 elements and a preset list of available members
//        meetingUi.setValue(new MeetingUiModel(
//            "", "", "", "", "",
//            null, null, null, null, null,
//            new ArrayList<>(Arrays.asList(
//                new MemberUiModel(70, "email0", null, View.VISIBLE),
//                new MemberUiModel(71, "", null, View.VISIBLE),
//                new MemberUiModel(72, "", null, View.VISIBLE)
//            ))
//        ));
//
//        // When: the second member's email (which is non_empty) is changed with the first value of the available members
//        MeetingUiModel meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        final String expectedEmail = availableMembers.get(0);
//        viewModel.onUpdateMember(1, expectedEmail);
//
//        // Then: the email value is updated and the available member list is changed
//        meetingUiModel = viewModel.getMeetingUiLiveData().getValue();
//        assertEquals(expectedEmail, meetingUiModel.getMemberList().get(1).getEmail());
//        assertEquals(2, availableMembers.size());
//        assertFalse(availableMembers.contains(expectedEmail));
//        assertFalse(availableMembers.contains(""));
//    }
//
//    private void initAvailableMembers(String... email) {
//        availableMembers.removeAll(Arrays.asList(email));
//    }
//*//*
//    @Test
//    public void onUpdateMember() {
//        // Given: change the first member's email
//        viewModel.onAddMember(0);
//        viewModel.onUpdateMember(0, "firstEmail");
//
//        // When: change the 1st member's email
//        viewModel.onUpdateMember(0, "newEmail");
//
//        // Then: ...
//        final List<MemberUi> memberUiList = viewModel.getMeetingUiLiveData().getValue().getMemberList();
//        final List<String> availableMembers = viewModel.getMeetingUiLiveData().getValue().getAvailableMembers();
//        assertTrue(availableMembers.contains("firstEmail")); // ...the available members contains the member's old email
//        assertFalse(availableMembers.contains("newEmail")); // ...the available members doesn't contain the member's new email
//        assertEquals("newEmail", memberUiList.get(0).getEmail()); // ...the first member's email is changed
//
//    }
//
//    @Test
//    public void onAddMeeting_allFieldsOk() {
//        //Given: fill the required fields
//        viewModel.onTopicChanged("topic");
//        viewModel.onDateChanged(2020, 10, 1);
//        viewModel.onTimeChanged(23, 59);
//        viewModel.onPlaceChanged("place");
//        viewModel.onUpdateMember(0, "email");
//
//        // When: add the meeting
//        viewModel.onAddMeeting();
//
//        // Then: the repository changes
//        final List<Meeting> meetingList = repository.getAllMeetings().getValue();
//        final Meeting expectedMeeting = new Meeting(
//            0,
//            "topic",
//            "place",
//            LocalDateTime.of(2020, 10, 1, 23, 59),
//            Collections.singletonList("email")
//        );
//        assertEquals(1, meetingList.size());
//        assertEquals(expectedMeeting, meetingList.get(0));
//    }
//
//    @Test
//    public void onAddMeeting_allFieldsWrong() {
//        // When: add meeting while all fields aren't filled
//        viewModel.onAddMeeting();
//
//        // Then: the Snackbar shows a specific message
//        assertEquals(R.string.mandatory_fields, viewModel.getShowSnack().getValue().intValue());
//    }
//
//    @Test
//    public void onAddMeeting_meetingAlreadyExisting() {
//        // Given: fill the required fields with the same place and date/time as the meeting which is contained in repository
//        viewModel.onTopicChanged("new topic");
//        viewModel.onDateChanged(2020, 10, 1);
//        viewModel.onStartTimeValidated(23, 59);
//        viewModel.onPlaceChanged("place");
//        viewModel.onUpdateMember(0, "new email");
//        repository.addMeeting(new Meeting(
//            "topic",
//            "place",
//            LocalDateTime.of(2020, 10, 1, 23, 59),
//            Collections.singletonList("email")
//        ));
//
//        // When: add the meeting
//        viewModel.onAddMeeting();
//
//        // Then: the Snackbar shows a specific message
//        assertEquals(R.string.meeting_already_exist, viewModel.getShowSnack().getValue().intValue());
//    }
// */
}