package com.neige_i.mareu.view.add;

import android.util.Log;
import android.view.View;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.MeetingRepository;
import com.neige_i.mareu.data.MeetingRepositoryImpl;
import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.view.model.MeetingUi;
import com.neige_i.mareu.view.model.MemberUi;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class AddViewModelTest {

    // Avoid error when MutableLiveData.setValue() is called in source code
    @Rule
    public final TestRule rule = new InstantTaskExecutorRule();

//    @Mock
    private final MeetingRepository repository = new MeetingRepositoryImpl();//Mockito.mock(MeetingRepository.class);

    private final AddViewModel viewModel = new AddViewModel(repository, Clock.fixed(
        ZonedDateTime.of(2020, 9, 25, 20, 10, 30, 150, ZoneId.systemDefault()).toInstant(),
        ZoneId.systemDefault()
    ));

    @Test
    public void onTopicChanged_nonEmptyString() {
        // Given: all errors being displayed
        // This happens when the 'add meeting' button is clicked while no entry has been set
        viewModel.onAddMeeting();

        // ASKME: no need to assert before changing values
        // When: the topic is changed to a non empty String
        viewModel.onTopicChanged("New topic value");

        // Then: the meetingUi's topic is updated and its error message is set to null
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertNotNull(meetingUi);
        assertEquals("New topic value", meetingUi.getTopic());
        assertNull(meetingUi.getTopicError());
    }

    @Test
    public void onTopicChanged_emptyString() {
        // ASKME: should test all 4 cases for this method
        // Given: all errors being displayed
        viewModel.onAddMeeting();

        // When: the topic is changed to an empty String (or similar)
        viewModel.onTopicChanged("   ");

        // Then: the meetingUi's topic is updated but its error message remains
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertNotNull(meetingUi);
        assertEquals("   ", meetingUi.getTopic());
        assertNotNull(meetingUi.getTopicError());
    }

    @Test
    public void onDateClicked() {
        // When: the date input is clicked
        viewModel.onDateClicked();
        // Then: the correct time is set
        assertEquals(LocalDate.of(2020, 9, 25), viewModel.getDatePicker().getValue());
    }

    @Test
    public void onDateValidated() {
        // Given: all errors being displayed
        viewModel.onAddMeeting();

        // When: the date is changed to October, 1st 2020
        viewModel.onDateValidated(2020, 10, 1);

        // Then: the meetingUi's date is updated and its error message is set to null
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertNotNull(meetingUi);
        assertEquals("01/10/2020", meetingUi.getDate());
        assertNull(meetingUi.getDateError());
    }

    @Test
    public void onTimeClicked() {
        // When: the time input is clicked
        viewModel.onTimeClicked();
        // Then: the correct date is set
        assertEquals(LocalTime.of(20, 10, 30, 150), viewModel.getTimePicker().getValue());
    }

    @Test
    public void onTimeValidated() {
        // Given: all errors being displayed
        viewModel.onAddMeeting();

        // When: the time is changed to 23h59
        viewModel.onTimeValidated(23, 59);

        // Then: the meetingUi's time is updated and its error message is set to null
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertNotNull(meetingUi);
        assertEquals("23:59", meetingUi.getTimeStart());
        assertNull(meetingUi.getTimeStartError());
    }

    @Test
    public void onPlaceSelected() {
        // Given: all errors being displayed
        viewModel.onAddMeeting();

        // When: the place is changed
        viewModel.onPlaceSelected("New room name");

        // Then: the meetingUi's place is updated and its error message is set to null
        final MeetingUi meetingUi = viewModel.getMeetingUiLiveData().getValue();
        assertNotNull(meetingUi);
        assertEquals("New room name", meetingUi.getPlace());
        assertNull(meetingUi.getPlaceError());
    }

    @Test
    public void onAddMember() {
        // Given: add 3 members after the current one
        for (int i = 0; i < 3; i++) {
            viewModel.onAddMember(0);
        }

        // When: add a last member just after the 2nd in the list
        viewModel.onAddMember(1);

        // Then: ...
        final List<MemberUi> memberUiList = viewModel.getMeetingUiLiveData().getValue().getMemberList();
        System.out.println(viewModel.getMeetingUiLiveData().getValue().getMemberList());
        int lastMemberPosition = -1;
        for (int position = 0; position < memberUiList.size(); position++) {
            if (memberUiList.get(position).getId() == 4) { // The last added member is the 5th one which ID is 4
                lastMemberPosition = position;
                break;
            }
        }
        assertEquals(View.VISIBLE, memberUiList.get(0).getRemoveButtonVisibility()); // ...the first member's 'remove button' is visible
        assertEquals(2, lastMemberPosition); // ...the last member is in the 3rd position
//        assertEquals(4, memberUiList.get(2).getId());
        assertEquals(5, memberUiList.size()); // ...the members' list has the correct size
    }

    @Test
    public void onRemoveMember() {
        // Given: add a member after the current one and change the first member's email
        viewModel.onAddMember(0);
        viewModel.onUpdateMember(0, "firstEmail");

        // When: remove the 1st member
        final MemberUi memberToRemove = new MemberUi(0, "firstEmail", null, View.VISIBLE);
        viewModel.onRemoveMember(memberToRemove);

        // Then: ...
        final List<MemberUi> memberUiList = viewModel.getMeetingUiLiveData().getValue().getMemberList();
        final List<String> availableMembers = viewModel.getMeetingUiLiveData().getValue().getAvailableMembers();
        assertTrue(availableMembers.contains("firstEmail")); // ...the available members contains the removed member's email
        assertFalse(memberUiList.contains(memberToRemove)); // ...the list doesn't contain the member anymore
        assertEquals(View.INVISIBLE, memberUiList.get(0).getRemoveButtonVisibility()); // ...the first member's 'remove button' is invisible
        assertEquals(1, memberUiList.size()); // ...the members' list has the correct size
    }

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
        viewModel.onDateValidated(2020, 10, 1);
        viewModel.onTimeValidated(23, 59);
        viewModel.onPlaceSelected("place");
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
        viewModel.onDateValidated(2020, 10, 1);
        viewModel.onTimeValidated(23, 59);
        viewModel.onPlaceSelected("place");
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
}