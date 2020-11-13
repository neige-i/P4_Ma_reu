package com.neige_i.mareu.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DI;
import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.data.model.Member;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.neige_i.mareu.Util.NO_ERROR;
import static com.neige_i.mareu.Util.updateLiveData;

public class MeetingRepositoryImpl implements MeetingRepository {

    // ------------------------------------ INSTANCE VARIABLES -------------------------------------

    /**
     * Represents the meeting to add.
     */
    @NonNull
    private final MutableLiveData<Meeting> meeting = new MutableLiveData<>();
    /**
     * Represents the available members to show in the drop-down menu.
     */
    @NonNull
    private final List<String> availableMembers = new ArrayList<>();

    // -------------------------------------- GENERAL METHODS --------------------------------------

    @Override
    public void initRepository() {
        meeting.setValue(new Meeting());
        addMember(0);
        initAvailableMembers();
    }

    // -------------------------------------- MEETING METHODS --------------------------------------

    @NonNull
    @Override
    public LiveData<Meeting> getMeeting() {
        return meeting;
    }

    @Override
    public void setTopic(@NonNull String topic) {
        meeting.getValue().setTopic(topic);
        updateLiveData(meeting);
    }

    @Override
    public void setDate(@NonNull LocalDate date) {
        meeting.getValue().setDate(date);
        setSameFieldsErrors();
        updateLiveData(meeting);
    }

    @Override
    public void setStartTime(@NonNull LocalTime startTime) {
        final Meeting currentMeeting = meeting.getValue();

        currentMeeting.setStartTime(startTime);

        // TIPS: handle errors if times are incorrect
        if (currentMeeting.getEndTime() != null) {
            if (!startTime.isBefore(currentMeeting.getEndTime())) {
                currentMeeting.setStartTimeError(R.string.incorrect_start_error);
            } else {
                if (currentMeeting.getStartTimeError() == R.string.incorrect_start_error)
                    currentMeeting.setStartTimeError(NO_ERROR);
                if (currentMeeting.getEndTimeError() == R.string.incorrect_end_error)
                    currentMeeting.setEndTimeError(NO_ERROR);
            }
        }

        setSameFieldsErrors();
        meeting.setValue(currentMeeting);
    }

    @Override
    public void setEndTime(@NonNull LocalTime endTime) {
        final Meeting currentMeeting = meeting.getValue();

        currentMeeting.setEndTime(endTime);

        if (currentMeeting.getStartTime() != null) {
            if (!endTime.isAfter(currentMeeting.getStartTime())) {
                currentMeeting.setEndTimeError(R.string.incorrect_end_error);
            } else {
                if (currentMeeting.getEndTimeError() == R.string.incorrect_end_error)
                    currentMeeting.setEndTimeError(NO_ERROR);
                if (currentMeeting.getStartTimeError() == R.string.incorrect_start_error)
                    currentMeeting.setStartTimeError(NO_ERROR);
            }
        }

        setSameFieldsErrors();
        meeting.setValue(currentMeeting);
    }

    @Override
    public void setPlace(@NonNull String place) {
        meeting.getValue().setPlace(place);
        setSameFieldsErrors();
        updateLiveData(meeting);
    }

    @Override
    public void addMember(int position) {
        meeting.getValue().getMemberList().add(position, new Member());
        updateLiveData(meeting);
    }

    @Override
    public void updateMember(int position, @NonNull String email) {
        meeting.getValue().getMemberList().get(position).setEmail(email);
        setSameFieldsErrors();
        updateLiveData(meeting);
        setAvailableMembers();
    }

    @Override
    public void removeMember(int position) {
        meeting.getValue().getMemberList().remove(position);
        setSameFieldsErrors();
        updateLiveData(meeting);
        setAvailableMembers();
    }

    @Override
    public void triggerRequiredErrors() {
        final Meeting currentMeeting = meeting.getValue();

        if (currentMeeting.getTopic().trim().isEmpty())
            currentMeeting.setTopicError(R.string.required_field_error);
        if (currentMeeting.getDate() == null)
            currentMeeting.setDateError(R.string.required_field_error);
        if (currentMeeting.getStartTime() == null)
            currentMeeting.setStartTimeError(R.string.required_field_error);
        if (currentMeeting.getEndTime() == null)
            currentMeeting.setEndTimeError(R.string.required_field_error);
        if (currentMeeting.getPlace().isEmpty())
            currentMeeting.setPlaceError(R.string.required_field_error);

        currentMeeting.getMemberList().stream()
            .filter(member -> member.getEmail().isEmpty())
            .forEach(member -> member.setError(R.string.required_field_error));

        meeting.setValue(currentMeeting);
    }

    private boolean areAllFieldsSet() {
        final Meeting currentMeeting = meeting.getValue();
        return !currentMeeting.getTopic().trim().isEmpty() &&
            currentMeeting.getDate() != null &&
            currentMeeting.getStartTime() != null &&
            currentMeeting.getEndTime() != null &&
            !currentMeeting.getPlace().isEmpty() &&
            currentMeeting.getMemberList().stream().noneMatch(member -> member.getEmail().isEmpty());
    }

    private void setSameFieldsErrors() {
        // Check if all fields are set
        if (!areAllFieldsSet())
            return;

        final Meeting currentMeeting = meeting.getValue();
        final LocalTime currentStart = currentMeeting.getStartTime();
        final LocalTime currentEnd = currentMeeting.getEndTime();

        // Check if times are correct
        if (!currentStart.isBefore(currentEnd))
            return;

        // Loop through existing meetings
        for (Meeting existingMeeting : DI.getListingRepository().getAllMeetings().getValue()) {
            final LocalTime existingStart = existingMeeting.getStartTime();
            final LocalTime existingEnd = existingMeeting.getEndTime();
            final List<String> existingEmails = existingMeeting.getMemberList()
                .stream()
                .map(Member::getEmail)
                .collect(Collectors.toList());
            boolean samePlace = false; // Is true if an existing meeting is held the same day, times and PLACE
            final int[] sameMember = {0}; // Is >0 if an existing meeting contains MEMBERS that are occupied the same day and times

            // An existing meeting is held the same day as the current one
            if (currentMeeting.getDate().isEqual(existingMeeting.getDate()) &&
                ((currentStart.isBefore(existingStart) && currentEnd.isAfter(existingStart)) ||
                currentStart.equals(existingStart) ||
                (currentStart.isAfter(existingStart) && currentStart.isBefore(existingEnd)))
            ) {

                // An existing meeting exists with times overlapping the current ont
                if (currentMeeting.getPlace().equals(existingMeeting.getPlace())) {
                    currentMeeting.setPlaceError(R.string.time_place_error, existingStart, existingEnd);
                    samePlace = true;
                }

                currentMeeting.getMemberList()
                    .stream()
                    .filter(member -> existingEmails.contains(member.getEmail()))
                    .forEach(member -> {
                        member.setError(R.string.time_member_error, existingStart, existingEnd);
                        sameMember[0]++;
                    });
            }

            // Check if the 'place' error can be removed
            if (!currentMeeting.getPlace().equals(existingMeeting.getPlace())) {
                currentMeeting.setPlaceError(NO_ERROR);
                samePlace = false;
            }

            // Check if the 'members' error can be removed
            currentMeeting.getMemberList()
                .stream()
                .filter(member -> !existingEmails.contains(member.getEmail()))
                .forEach(member -> member.setError(NO_ERROR));

            if (samePlace || sameMember[0] != 0) {
                currentMeeting.setDateError(R.string.occupied_error);
                currentMeeting.setStartTimeError(R.string.occupied_error);
                currentMeeting.setEndTimeError(R.string.occupied_error);
                return;
            } else {
                currentMeeting.setDateError(NO_ERROR);
                currentMeeting.setStartTimeError(NO_ERROR);
                currentMeeting.setEndTimeError(NO_ERROR);
            }
        }
        meeting.setValue(currentMeeting);
    }

    // --------------------------------- AVAILABLE MEMBERS METHODS ---------------------------------

    @NonNull
    public List<String> getAvailableMembers() {
        return availableMembers;
    }

    private void initAvailableMembers() {
        availableMembers.clear();
        availableMembers.addAll(DummyGenerator.EMAILS);
    }

    private void setAvailableMembers() {
        initAvailableMembers();
        availableMembers.removeAll(meeting.getValue().getEmails());
    }
}
