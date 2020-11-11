package com.neige_i.mareu.view.add;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DI;
import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.data.repository.MeetingRepository;
import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.data.model.Member;
import com.neige_i.mareu.view.add.view_model.AddViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static com.neige_i.mareu.Util.NO_ERROR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;

@RunWith(JUnitParamsRunner.class)
public class AddViewModelTest {

    // ------------------------------------ TEST RULE VARIABLE -------------------------------------

    // Avoid error when MutableLiveData.setValue() is called in source code
    @Rule
    public final TestRule rule = new InstantTaskExecutorRule();

    // -------------------------- VARIABLES USED TO INITIALIZE VIEW MODEL --------------------------

    @NonNull
    private final MeetingRepository meetingRepository = DI.getMeetingRepository();
    @NonNull
    private static final LocalDate defaultDate = LocalDate.of(2020, 9, 20);
    @NonNull
    private static final LocalTime defaultTime = LocalTime.of(20, 10);

    // ------------------------------------- OBJECT UNDER TEST -------------------------------------

    private AddViewModel viewModel;

    // ---------------------------------------- SETUP TEST -----------------------------------------

    @Before
    public void setUp() {
        // Given: an initialized meeting
        final Meeting expectedMeeting = new Meeting();
        expectedMeeting.getMemberList().add(new Member(99, "", NO_ERROR));
        DI.setMemberId(99);

        // When: instantiate the ViewModel
        viewModel = new AddViewModel(
            meetingRepository,
            Clock.fixed(
                ZonedDateTime.of(defaultDate, defaultTime, ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
            )
        );

        // Then: the meeting repository variables are updated
        assertThat(meetingRepository.getMeeting().getValue(), is(expectedMeeting));
        assertThat(meetingRepository.getAvailableMembers(), is(DummyGenerator.EMAILS));
    }

    // ----------------------------------------- DATE TEST -----------------------------------------

    @Test
    @Parameters(method = "clickDate")
    public void showDatePickerDialog_setLiveDataValue(LocalDate currentDate, LocalDate expectedDate) {
        // Given: a value for the repository's date
        meetingRepository.getMeeting().getValue().setDate(currentDate);

        // When: click on the date
        viewModel.showDatePickerDialog();

        // Then: the SingleLiveEvent is updated
        assertThat(viewModel.getDatePickerEvent().getValue(), is(expectedDate));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] clickDate() {
        return new Object[]{
            // Return the same date stored in the repository for the SingleLiveEvent
            // If it is null, return the default date
            new Object[]{LocalDate.of(2020, 10, 12), LocalDate.of(2020, 10, 12)},
            new Object[]{LocalDate.of(2020, 11, 10), LocalDate.of(2020, 11, 10)},
            new Object[]{null, defaultDate},
        };
    }

    // ----------------------------------------- TIME TEST -----------------------------------------

    @Test
    @Parameters(method = "clickTime")
    public void showTimePickerDialog_setLiveDataValue(int timeInputId, LocalTime currentStartTime,
                                                      LocalTime currentEndTime,LocalTime expectedTime) {
        // Given: a value for the repository's startTime or endTime
        meetingRepository.getMeeting().getValue().setStartTime(currentStartTime);
        meetingRepository.getMeeting().getValue().setEndTime(currentEndTime);

        // When: click on the time
        viewModel.showTimePickerDialog(timeInputId);

        // Then: the SingleLiveEvent is updated
        assertThat(viewModel.getTimePickerEvent().getValue(), is(expectedTime));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] clickTime() {
        return new Object[]{
            // Return the same time stored in the repository for the SingleLiveEvent
            // If it is null, return the default time
            new Object[]{R.id.start_time_input, LocalTime.of(14, 5), any(), LocalTime.of(14, 5)},
            new Object[]{R.id.start_time_input, null, any(), defaultTime},
            new Object[]{R.id.end_time_input, any(), LocalTime.of(15, 46), LocalTime.of(15, 46)},
            new Object[]{R.id.end_time_input, any(), null, defaultTime},
        };
    }

    // -------------------------------------- ADD MEMBER TEST --------------------------------------

    @Test
    @Parameters(method = "memberList")
    public void onAddMember_correctPosition(List<Member> initialList, int position, List<Member> expectedList) {
        // Given: initialize the repository's member list
        meetingRepository.getMeeting().getValue().setMemberList(initialList);
        DI.setMemberId(99);

        // When: add a member
        viewModel.addMember(position);

        // Then: the member list is updated
        assertThat(meetingRepository.getMeeting().getValue().getMemberList(), is(expectedList));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] memberList() {
        return new Object[]{
            // Given: an initial member list
            // When: add a member after the position #1 (here, #1 is the last position)
            // Then: the new member is at the the position #2
            new Object[]{
                new ArrayList<>(Arrays.asList(
                    new Member(0, "", NO_ERROR),
                    new Member(1, "", NO_ERROR)
                )),
                1,
                Arrays.asList(
                    new Member(0, "", NO_ERROR),
                    new Member(1, "", NO_ERROR),
                    new Member(99, "", NO_ERROR) // Newly added member
                ),
            },

            // Given: an initial member list
            // When: add a member after the position #0
            // Then: the new member is at the the position #1
            new Object[]{
                new ArrayList<>(Arrays.asList(
                    new Member(0, "", NO_ERROR),
                    new Member(1, "", NO_ERROR),
                    new Member(2, "", NO_ERROR),
                    new Member(3, "", NO_ERROR)
                )),
                0,
                Arrays.asList(
                    new Member(0, "", NO_ERROR),
                    new Member(99, "", NO_ERROR), // Newly added member
                    new Member(1, "", NO_ERROR),
                    new Member(2, "", NO_ERROR),
                    new Member(3, "", NO_ERROR)
                ),
            }
        };
    }

    // ------------------------------------- ADD MEETING TEST --------------------------------------

    @Test
    @Parameters(method = "meetingValues")
    public void onAddMeeting_updateLiveDataIfNoError(String topic, LocalDate date, LocalTime startTime, LocalTime endTime, String place,
                                                     List<Member> memberList, Meeting expectedMeeting
    ) {
        //Given: init meeting
        final Meeting currentMeeting = meetingRepository.getMeeting().getValue();
        currentMeeting.setTopic(topic);
        currentMeeting.setDate(date);
        currentMeeting.setStartTime(startTime);
        currentMeeting.setEndTime(endTime);
        currentMeeting.setPlace(place);
        currentMeeting.setMemberList(memberList);

        // When: add the meeting
        viewModel.onAddMeeting();

        // Then: the repository changes
        assertThat(viewModel.getEndActivityEvent().getValue(), is(expectedMeeting));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] meetingValues() {
        final List<Member> correctMemberList = Collections.singletonList(new Member(0, "maxime@lamzone.com", NO_ERROR));
        return new Object[]{
            new Object[]{
                "", null, null, null, "", Collections.singletonList(new Member(0, "", NO_ERROR)),
                null
            },

            new Object[]{
                "", LocalDate.of(2020, 11, 8), LocalTime.of(11, 30), LocalTime.of(12, 0), "Mario", correctMemberList,
                null
            },
            new Object[]{
                "topic", null, LocalTime.of(11, 30), LocalTime.of(12, 0), "Mario", correctMemberList,
                null
            },
            new Object[]{
                "topic", LocalDate.of(2020, 11, 8), null, LocalTime.of(12, 0), "Mario", correctMemberList,
                null
            },
            new Object[]{
                "topic", LocalDate.of(2020, 11, 8), LocalTime.of(11, 30), null, "Mario", correctMemberList,
                null
            },
            new Object[]{
                "topic", LocalDate.of(2020, 11, 8), LocalTime.of(11, 30), LocalTime.of(12, 0), "", correctMemberList,
                null
            },
            new Object[]{
                "topic", LocalDate.of(2020, 11, 8), LocalTime.of(11, 30), LocalTime.of(12, 0), "", Collections.singletonList(new Member(0, "", NO_ERROR)),
                null
            },

            new Object[]{
                "topic", LocalDate.of(2020, 11, 8), LocalTime.of(11, 30), LocalTime.of(12, 0), "Mario", correctMemberList,
                new Meeting("topic", LocalDate.of(2020, 11, 8), LocalTime.of(11, 30), LocalTime.of(12, 0), "Mario", correctMemberList)
            }
        };
    }
}