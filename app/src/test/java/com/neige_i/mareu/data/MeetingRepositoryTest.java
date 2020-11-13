package com.neige_i.mareu.data;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.data.model.Member;
import com.neige_i.mareu.data.repository.MeetingRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static com.neige_i.mareu.Util.NO_ERROR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(JUnitParamsRunner.class)
public class MeetingRepositoryTest {

    // ------------------------------------ TEST RULE VARIABLE -------------------------------------

    // Avoid error when MutableLiveData.setValue() is called in source code
    @Rule
    public final TestRule rule = new InstantTaskExecutorRule();

    // ------------------------------------- OBJECT UNDER TEST -------------------------------------

    @NonNull
    private final MeetingRepository meetingRepository = DI.getMeetingRepository();

    // ---------------------------------------- SETUP TEST -----------------------------------------

    @Before
    public void setUp() {
        DI.setMemberId(99);
        meetingRepository.initRepository();
    }

    // ----------------------------------------- INIT TEST -----------------------------------------

    @Test
    public void initRepository_checkMeetingAndAvailableMembers() {
        // Given: an expected Meeting with an 'empty' member
        final Meeting expectedMeeting = new Meeting();
        expectedMeeting.setMemberList(Collections.singletonList(new Member(99, "", NO_ERROR)));

        // When: initialize repository (done in setUp method)

        // Then: repository variables equal the expected ones
        assertThat(meetingRepository.getMeeting().getValue(), is(expectedMeeting));
        assertThat(meetingRepository.getAvailableMembers(), is(DummyGenerator.EMAILS));
    }

    // ---------------------------------------- TOPIC TEST -----------------------------------------

    @Test
    @Parameters(method = "topicValues")
    public void setTopic_updateValueAndError(int initialError, @NonNull String topicValue, int expectedError) {
        // Given: an initial topic error
        meetingRepository.getMeeting().getValue().setTopicError(initialError);

        // When: set the meeting's topic
        meetingRepository.setTopic(topicValue);

        // Then: meeting's topic value and error are updated
        final Meeting currentMeeting = meetingRepository.getMeeting().getValue();
        assertThat(currentMeeting.getTopic(), is(topicValue));
        assertThat(currentMeeting.getTopicError(), is(expectedError));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] topicValues() {
        return new Object[]{
            // Given: no topic error
            // Then: topic error stay unchanged whatever the topic value
            new Object[]{NO_ERROR, "", NO_ERROR},
            new Object[]{NO_ERROR, "    ", NO_ERROR},
            new Object[]{NO_ERROR, "A non-empty value", NO_ERROR},
            new Object[]{NO_ERROR, "Another topic value", NO_ERROR},

            // Given: a 'required' topic error
            // Then: remove the error only if topic value is a non-empty String
            new Object[]{R.string.required_field_error, "", R.string.required_field_error},
            new Object[]{R.string.required_field_error, "    ", R.string.required_field_error},
            new Object[]{R.string.required_field_error, "A non-empty value", NO_ERROR},
            new Object[]{R.string.required_field_error, "Another topic value", NO_ERROR},
        };
    }

    // ----------------------------------------- DATE TEST -----------------------------------------

    @Test
    @Parameters(method = "dateValues")
    public void setDate_updateValueAndError(int initialError, @NonNull LocalDate dateValue, int expectedError) {
        // Given: an initial date error
        meetingRepository.getMeeting().getValue().setTopicError(initialError);

        // When: set the meeting's date
        meetingRepository.setDate(dateValue);

        // Then: meeting's date value and error are updated
        final Meeting currentMeeting = meetingRepository.getMeeting().getValue();
        assertThat(currentMeeting.getDate(), is(dateValue));
        assertThat(currentMeeting.getDateError(), is(expectedError));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] dateValues() {
        return new Object[]{
            // Given: no date error
            // Then: date error stay unchanged
            new Object[]{NO_ERROR, LocalDate.of(2020, 11, 4), NO_ERROR},
            new Object[]{NO_ERROR, LocalDate.of(2020, 10, 21), NO_ERROR},

            // Given: a 'required' date error
            // Then: remove the error
            new Object[]{R.string.required_field_error, LocalDate.of(2020, 11, 4), NO_ERROR},
            new Object[]{R.string.required_field_error, LocalDate.of(2020, 10, 21), NO_ERROR},
        };
    }

    // ----------------------------------------- TIME TEST -----------------------------------------

    @Test
    @Parameters(method = "startTimeValues")
    public void setStartTime_updateValueAndError(LocalTime endTime, LocalTime startTime, int initialStartError,
                                                 int expectedStartError, int initialEndError, int expectedEndError
    ) {
        // Given: an initial endTime and both time errors
        meetingRepository.getMeeting().getValue().setEndTime(endTime);
        meetingRepository.getMeeting().getValue().setEndTimeError(initialEndError);
        meetingRepository.getMeeting().getValue().setStartTimeError(initialStartError);

        // When: set the meeting's startTime
        meetingRepository.setStartTime(startTime);

        // Then: meeting's startTime and both time errors are updated
        final Meeting currentMeeting = meetingRepository.getMeeting().getValue();
        assertThat(currentMeeting.getStartTime(), is(startTime));
        assertThat(currentMeeting.getStartTimeError(), is(expectedStartError));
        assertThat(currentMeeting.getEndTimeError(), is(expectedEndError));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] startTimeValues() {
        return new Object[]{
            // Given: a null endTime, ignoring its initial and expected error
            // Then: remove startTime error whatever its initial value
            new Object[]{null, LocalTime.of(12, 45), R.string.required_field_error, NO_ERROR, anyInt(), anyInt()},
            new Object[]{null, LocalTime.of(12, 45), NO_ERROR, NO_ERROR, anyInt(), anyInt()},

            // Given: a non-null endTime, ignoring its initial and expected error
            // When: startTime is not before endTime
            // Then: set its error to 'incorrect' whatever its initial value
            new Object[]{LocalTime.of(12, 30), LocalTime.of(12, 30), R.string.required_field_error, R.string.incorrect_start_error, anyInt(), anyInt()},
            new Object[]{LocalTime.of(12, 30), LocalTime.of(12, 45), NO_ERROR, R.string.incorrect_start_error, anyInt(), anyInt()},

            // Given: a non-null endTime
            // When: startTime is before endTime
            // Then: remove startTime and endTime errors it it were an 'incorrect' one
            new Object[]{LocalTime.of(13, 0), LocalTime.of(12, 45), R.string.incorrect_start_error, NO_ERROR, NO_ERROR, NO_ERROR},
            new Object[]{LocalTime.of(13, 0), LocalTime.of(12, 45), NO_ERROR, NO_ERROR, R.string.incorrect_end_error, NO_ERROR},
        };
    }

    @Test
    @Parameters(method = "endTimeValues")
    public void setEndTime_updateValueAndError(LocalTime startTime, LocalTime endTime, int initialEndError,
                                               int expectedEndError, int initialStartError, int expectedStartError
    ) {
        // Given: an initial startTime and both time errors
        meetingRepository.getMeeting().getValue().setStartTime(startTime);
        meetingRepository.getMeeting().getValue().setStartTimeError(initialStartError);
        meetingRepository.getMeeting().getValue().setEndTimeError(initialEndError);

        // When: set the meeting's endTime
        meetingRepository.setEndTime(endTime);

        // Then: meeting's endTime and both time errors are updated
        final Meeting currentMeeting = meetingRepository.getMeeting().getValue();
        assertThat(currentMeeting.getEndTime(), is(endTime));
        assertThat(currentMeeting.getEndTimeError(), is(expectedEndError));
        assertThat(currentMeeting.getStartTimeError(), is(expectedStartError));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] endTimeValues() {
        return new Object[]{
            // Given: a null startTime, ignoring its initial and expected error
            // Then: remove endTime error whatever its initial value
            new Object[]{null, LocalTime.of(14, 15), R.string.required_field_error, NO_ERROR, anyInt(), anyInt()},
            new Object[]{null, LocalTime.of(14, 15), NO_ERROR, NO_ERROR, anyInt(), anyInt()},

            // Given: a non-null startTime, ignoring its initial and expected error
            // When: endTime is not after startTime
            // Then: set its error to 'incorrect' whatever its initial value
            new Object[]{LocalTime.of(15, 0), LocalTime.of(15, 0), R.string.required_field_error, R.string.incorrect_end_error, anyInt(), anyInt()},
            new Object[]{LocalTime.of(15, 0), LocalTime.of(14, 15), NO_ERROR, R.string.incorrect_end_error, anyInt(), anyInt()},

            // Given: a non-null startTime
            // When: endTime is after startTime
            // Then: remove endTime and startTime errors it it were an 'incorrect' one
            new Object[]{LocalTime.of(13, 0), LocalTime.of(14, 15), R.string.incorrect_end_error, NO_ERROR, NO_ERROR, NO_ERROR},
            new Object[]{LocalTime.of(13, 0), LocalTime.of(14, 15), NO_ERROR, NO_ERROR, R.string.incorrect_start_error, NO_ERROR},
        };
    }

    // ---------------------------------------- PLACE TEST -----------------------------------------

    @Test
    @Parameters(method = "placeValues")
    public void setPlace_updateValueAndError(int initialError, @NonNull String placeValue, int expectedError) {
        // Given: an initial place error
        meetingRepository.getMeeting().getValue().setPlaceError(initialError);

        // When: set the meeting's place
        meetingRepository.setPlace(placeValue);

        // Then: meeting's place value and error are updated
        final Meeting currentMeeting = meetingRepository.getMeeting().getValue();
        assertThat(currentMeeting.getPlace(), is(placeValue));
        assertThat(currentMeeting.getPlaceError(), is(expectedError));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] placeValues() {
        return new Object[]{
            // Given: no place error
            // Then: place error stay unchanged
            new Object[]{NO_ERROR, "Mario", NO_ERROR},
            new Object[]{NO_ERROR, "Luigi", NO_ERROR},

            // Given: a 'required' place error
            // Then: remove the error
            new Object[]{R.string.required_field_error, "Peach", NO_ERROR},
            new Object[]{R.string.required_field_error, "Bowser", NO_ERROR},
        };
    }

    // ------------------------------------- MEMBER LIST TEST --------------------------------------

    @Test
    @Parameters(method = "newMemberValue")
    public void addMember_updateList(List<Member> initialList, int position, List<Member> expectedList) {
        // Given: an initial member list
        meetingRepository.getMeeting().getValue().setMemberList(initialList);
        DI.setMemberId(99); // ID for the next member to add

        // When: add a member at the specified position
        meetingRepository.addMember(position);

        // Then: the member list is updated
        assertThat(meetingRepository.getMeeting().getValue().getMemberList(), is(expectedList));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] newMemberValue() {
        return new Object[]{
            // Given: an empty member list
            // When: add a new member at the position #0
            // Then: the member list contains a unique item with the id 99
            new Object[]{
                new ArrayList<>(),
                0,
                Collections.singletonList(new Member(99, "", NO_ERROR))
            },

            // Given: a member list with 3 items
            // When: add a new member at the position 1
            // Then: the member list contains 4 items, and the member at position #1 has the id 99
            new Object[]{
                new ArrayList<>(Arrays.asList(
                    new Member(3, "", NO_ERROR),
                    new Member(4, "", NO_ERROR),
                    new Member(5, "", NO_ERROR)
                )),
                1,
                new ArrayList<>(Arrays.asList(
                    new Member(3, "", NO_ERROR),
                    new Member(99, "", NO_ERROR), // Newly added member
                    new Member(4, "", NO_ERROR),
                    new Member(5, "", NO_ERROR)
                ))
            }
        };
    }

    @Test
    @Parameters(method = "memberEmail")
    public void updateMember_updateListAndAvailableMembers(List<Member> initialList, int position, String email,
                                                           List<Member> expectedList, List<String> expectedAvailableMembers
    ) {
        // Given: an initial member list
        meetingRepository.getMeeting().getValue().setMemberList(initialList);

        // When: update the member at the specified position
        meetingRepository.updateMember(position, email);

        // Then: the member list and the available one are updated
        assertThat(meetingRepository.getMeeting().getValue().getMemberList(), is(expectedList));
        assertThat(meetingRepository.getAvailableMembers(), is(expectedAvailableMembers));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] memberEmail() {
        final List<String> availableMembers1 = new ArrayList<>(DummyGenerator.EMAILS);
        availableMembers1.remove("maxime@lamzone.com");
        final List<String> availableMembers2 = new ArrayList<>(DummyGenerator.EMAILS);
        availableMembers2.remove("alex@lamzone.com");
        availableMembers2.remove("viviane@lamzone.com");
        availableMembers2.remove("amandine@lamzone.com");

        return new Object[]{
            // Given: a list containing a unique member with a 'required' error
            // When: change the email of the member at position #0
            // Then: the member's email value and error are updated, and the available members too
            new Object[]{
                new ArrayList<>(Collections.singleton(new Member(0, "", R.string.required_field_error))),
                0,
                "maxime@lamzone.com",
                Collections.singletonList(new Member(0, "maxime@lamzone.com", NO_ERROR)),
                availableMembers1
            },

            // Given: a list with 3 items
            // When: change the email of the member at position #1
            // Then: the email of the member at position #1 is updated, and the available members too
            new Object[]{
                new ArrayList<>(Arrays.asList(
                    new Member(0, "viviane@lamzone.com", NO_ERROR),
                    new Member(1, "maxime@lamzone.com", NO_ERROR),
                    new Member(2, "amandine@lamzone.com", NO_ERROR)
                )),
                1,
                "alex@lamzone.com",
                new ArrayList<>(Arrays.asList(
                    new Member(0, "viviane@lamzone.com", NO_ERROR),
                    new Member(1, "alex@lamzone.com", NO_ERROR), // maxime is replaced with alex
                    new Member(2, "amandine@lamzone.com", NO_ERROR)
                )),
                availableMembers2
            },
        };
    }

    @Test
    @Parameters(method = "listValues")
    public void removeMember_updateListAndAvailableMembers(List<Member> initialList, int position,
                                                           List<Member> expectedList, List<String> expectedAvailableMembers
    ) {
        // Given: an initial member list
        meetingRepository.getMeeting().getValue().setMemberList(initialList);

        // When: update the member at the specified position
        meetingRepository.removeMember(position);

        // Then: the member list and the available one are updated
        assertThat(meetingRepository.getMeeting().getValue().getMemberList(), is(expectedList));
        assertThat(meetingRepository.getAvailableMembers(), is(expectedAvailableMembers));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] listValues() {
        final List<String> availableMembers1 = new ArrayList<>(DummyGenerator.EMAILS);
        availableMembers1.remove("maxime@lamzone.com");
        availableMembers1.remove("amandine@lamzone.com");

        return new Object[]{
            new Object[]{
                new ArrayList<>(Arrays.asList(
                    new Member(0, "viviane@lamzone.com", NO_ERROR),
                    new Member(1, "maxime@lamzone.com", NO_ERROR),
                    new Member(2, "amandine@lamzone.com", NO_ERROR)
                )),
                0,
                new ArrayList<>(Arrays.asList(
                    new Member(1, "maxime@lamzone.com", NO_ERROR),
                    new Member(2, "amandine@lamzone.com", NO_ERROR)
                )),
                availableMembers1
            },

            new Object[]{
                new ArrayList<>(Arrays.asList(
                    new Member(0, "", NO_ERROR),
                    new Member(1, "", NO_ERROR),
                    new Member(2, "maxime@lamzone.com", NO_ERROR)
                )),
                2,
                new ArrayList<>(Arrays.asList(
                    new Member(0, "", NO_ERROR),
                    new Member(1, "", NO_ERROR)
                )),
                DummyGenerator.EMAILS
            },
        };
    }

    @Test
    public void triggerRequiredErrors_updateErrors() {
        // Given: an expected meeting with a 'required' error for all fields
        final Meeting expectedMeeting = new Meeting();
        expectedMeeting.setTopicError(R.string.required_field_error);
        expectedMeeting.setDateError(R.string.required_field_error);
        expectedMeeting.setStartTimeError(R.string.required_field_error);
        expectedMeeting.setEndTimeError(R.string.required_field_error);
        expectedMeeting.setPlaceError(R.string.required_field_error);
        expectedMeeting.getMemberList().add(new Member(99, "", R.string.required_field_error));

        // When: trigger required errors
        meetingRepository.triggerRequiredErrors();

        // Then: the repository meeting equals the expected one
        assertThat(meetingRepository.getMeeting().getValue(), is(expectedMeeting));
    }
}