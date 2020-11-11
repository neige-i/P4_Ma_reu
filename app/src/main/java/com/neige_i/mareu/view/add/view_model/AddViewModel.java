package com.neige_i.mareu.view.add.view_model;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.repository.MeetingRepository;
import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.view.SingleLiveEvent;
import com.neige_i.mareu.view.add.ui_model.NewMeetingUiModel;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.neige_i.mareu.Util.NO_ERROR;

public class AddViewModel extends ViewModel {

    // -------------------------------------  EVENT LIVE DATA --------------------------------------

    /**
     * Represents the event to display a {@link android.app.DatePickerDialog DatePickerDialog}.
     */
    @NonNull
    private final SingleLiveEvent<LocalDate> datePickerEvent = new SingleLiveEvent<>();
    /**
     * Represents the event to display a {@link android.app.TimePickerDialog TimePickerDialog}.
     */
    @NonNull
    private final SingleLiveEvent<LocalTime> timePickerEvent = new SingleLiveEvent<>();
    /**
     * Represents the event to end the current activity.
     */
    @NonNull
    private final SingleLiveEvent<Meeting> endActivityEvent = new SingleLiveEvent<>();

    // -------------------------------------  LOCAL VARIABLES --------------------------------------

    /**
     * Repository to get and set the meeting to add.
     */
    @NonNull
    private final MeetingRepository meetingRepository;
    /**
     * Handy for testing.
     */
    @NonNull
    private final Clock clock;

    /**
     * Determines which time to set.
     */
    private boolean isStartTimeSelected;

    // ----------------------------------- CONSTRUCTOR & GETTERS -----------------------------------

    public AddViewModel(@NonNull MeetingRepository meetingRepository, @NonNull Clock clock) {
        this.meetingRepository = meetingRepository;
        this.clock = clock;

        // Initialize repository and add an empty member
        meetingRepository.initRepository();
        meetingRepository.addMember(0);
    }

    @NonNull
    public LiveData<NewMeetingUiModel> getMeetingUiModel() {
        return Transformations.map(meetingRepository.getMeeting(), NewMeetingUiModel::new);
    }

    @NonNull
    public LiveData<LocalDate> getDatePickerEvent() {
        return datePickerEvent;
    }

    @NonNull
    public LiveData<LocalTime> getTimePickerEvent() {
        return timePickerEvent;
    }

    @NonNull
    public LiveData<Meeting> getEndActivityEvent() {
        return endActivityEvent;
    }

    // --------------------------------------- TOPIC METHODS ---------------------------------------

    public void setTopic(@NonNull String topic) {
        meetingRepository.setTopic(topic);
    }

    // ------------------------------------- DATE/TIME METHODS -------------------------------------

    public void showDatePickerDialog() {
        final LocalDate currentDate = getMeeting().getDate();
        datePickerEvent.setValue(currentDate == null ? LocalDate.now(clock) : currentDate);
    }

    public void setDate(int year, int month, int dayOfMonth) {
        meetingRepository.setDate(LocalDate.of(year, month, dayOfMonth));
    }

    public void showTimePickerDialog(@IdRes int timeInputId) {
        isStartTimeSelected = timeInputId == R.id.start_time_input;

        final LocalTime currentTime = isStartTimeSelected ? getMeeting().getStartTime() : getMeeting().getEndTime();
        timePickerEvent.setValue(currentTime == null ? LocalTime.now(clock) : currentTime);
    }

    public void setTime(int hour, int minute) {
        final LocalTime selectedTime = LocalTime.of(hour, minute);
        if (isStartTimeSelected)
            meetingRepository.setStartTime(selectedTime);
        else
            meetingRepository.setEndTime(selectedTime);
    }

    // --------------------------------------- PLACE METHODS ---------------------------------------

    public void setPlace(@NonNull String place) {
        meetingRepository.setPlace(place);
    }

    // ------------------------------------ MEMBER LIST METHODS ------------------------------------

    // TIPS: add the member right AFTER the current position
    public void addMember(int position) {
        meetingRepository.addMember(position + 1);
    }

    public void updateMember(int position, @NonNull String email) {
        meetingRepository.updateMember(position, email);
    }

    public void removeMember(int position) {
        meetingRepository.removeMember(position);
    }

    // ---------------------------------------- FAB METHODS ----------------------------------------

    public void onAddMeeting() {
        meetingRepository.triggerRequiredErrors();

        final Meeting currentMeeting = getMeeting();
        if (areAllFieldsWithoutError(currentMeeting))
            endActivityEvent.setValue(currentMeeting);
    }

    // --------------------------------------- LOCAL METHODS ---------------------------------------

    @NonNull
    private Meeting getMeeting() {
        return meetingRepository.getMeeting().getValue();
    }

    private boolean areAllFieldsWithoutError(@NonNull Meeting currentMeeting) {
        return currentMeeting.getTopicError() == NO_ERROR &&
            currentMeeting.getDateError() == NO_ERROR &&
            currentMeeting.getStartTimeError() == NO_ERROR &&
            currentMeeting.getEndTimeError() == NO_ERROR &&
            currentMeeting.getPlaceError() == NO_ERROR &&
            currentMeeting.getMemberList().stream().allMatch(member -> member.getError() == NO_ERROR);
    }
}