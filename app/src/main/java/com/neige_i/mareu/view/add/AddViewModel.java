package com.neige_i.mareu.view.add;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DI;
import com.neige_i.mareu.data.MeetingRepository;
import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.view.util.SingleLiveEvent;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.neige_i.mareu.view.util.Util.NO_ERROR;

public class AddViewModel extends AndroidViewModel {

    // ------------------------------------  INSTANCE VARIABLES ------------------------------------

    @NonNull
    private final MeetingRepository meetingRepository;
    @NonNull
    private final Clock clock; // Handy for testing

    // -------------------------------------  STATE LIVE DATA --------------------------------------

    @NonNull
    private final MutableLiveData<MeetingUiModel> meetingUiModel = new MutableLiveData<>();

    // -------------------------------------  EVENT LIVE DATA --------------------------------------

    @NonNull
    private final SingleLiveEvent<LocalTime> timePickerEvent = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<LocalDate> datePickerEvent = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<String> showSnackEvent = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<Void> endActivityEvent = new SingleLiveEvent<>();

    // -------------------------------------  LOCAL VARIABLES --------------------------------------

    private boolean isStartTimeSelected;

    // ----------------------------------- CONSTRUCTOR & GETTERS -----------------------------------

    public AddViewModel(@NonNull MeetingRepository meetingRepository, @NonNull Clock clock, @NonNull Application application) {
        super(application);
        this.meetingRepository = meetingRepository;
        this.clock = clock;

        // Initialize repository and add an empty member
        meetingRepository.initRepository();
        meetingRepository.addMember(0);

        // Initialize UI model
        meetingUiModel.setValue(new MeetingUiModel(getApplication()));
    }

    @NonNull
    public LiveData<MeetingUiModel> getMeetingUiModel() {
        return Transformations.switchMap(
            meetingRepository.getMeeting(),
            meeting -> {
                Log.d("Neige", "AddViewModel::getMeetingUiModel:switchMap");
                meetingUiModel.getValue().setTopic(meeting.getTopic());
                meetingUiModel.getValue().setDate(meeting.getDate());
                meetingUiModel.getValue().setStartTime(meeting.getStartTime());
                meetingUiModel.getValue().setEndTime(meeting.getEndTime());
                meetingUiModel.getValue().setPlace(meeting.getPlace());
                meetingUiModel.getValue().setMemberList(meeting.getMemberIndex(), meeting.getMemberList());

                Log.d("Neige", "AddViewModel::getMeetingUiModel: are fields: ok? " + meetingUiModel.getValue().areFieldsOk() +
                    " set? " + meetingUiModel.getValue().areAllFieldsSet() + " times? " +
                    Arrays.toString(meetingUiModel.getValue().getExistingTimes()));

                if (meetingUiModel.getValue().getExistingTimes() != null)
                    checkIfSameFields();

                meetingUiModel.setValue(meetingUiModel.getValue());
                return meetingUiModel;
            }
        );
    }

    @NonNull
    public LiveData<LocalTime> getTimePickerEvent() {
        return timePickerEvent;
    }

    @NonNull
    public LiveData<LocalDate> getDatePickerEvent() {
        return datePickerEvent;
    }

    @NonNull
    public LiveData<String> getShowSnackEvent() {
        return showSnackEvent;
    }

    @NonNull
    public LiveData<Void> getEndActivityEvent() {
        return endActivityEvent;
    }

    // --------------------------------------- TOPIC METHODS ---------------------------------------

    /**
     * Called when the user types the topic of the meeting.
     */
    public void setTopic(@NonNull String topic) {
        meetingRepository.setTopic(topic);
    }

    // --------------------------------------- DATE METHODS ----------------------------------------

    /**
     * Called when the user clicks to choose the date of the meeting.
     */
    public void showDatePickerDialog() {
        final LocalDate date = meetingRepository.getMeeting().getValue().getDate();
        datePickerEvent.setValue(date == null ? LocalDate.now(clock) : date);
    }

    /**
     * Called when the user validates the date of the meeting.
     */
    public void setDate(int year, int month, int dayOfMonth) {
        meetingRepository.setDate(LocalDate.of(year, month, dayOfMonth));
    }

    // --------------------------------------- TIME METHODS ----------------------------------------

    /**
     * Called when the user clicks to choose the start time or end time of the meeting.
     */
    public void showTimePickerDialog(@IdRes int timeInputId) {
        isStartTimeSelected = timeInputId == R.id.start_time_input;

        final Meeting meeting = meetingRepository.getMeeting().getValue();
        final LocalTime localTime = isStartTimeSelected ? meeting.getStartTime() : meeting.getEndTime();
        timePickerEvent.setValue(localTime == null ? LocalTime.now(clock) : localTime);
    }

    /**
     * Called when the user validates the start time or end time of the meeting.
     */
    public void setTime(int hour, int minute) {
        // Check if start time is strictly before end time (times can't be the same)
        final LocalTime selectedTime = LocalTime.of(hour, minute);
        final Meeting meeting = meetingRepository.getMeeting().getValue();
        int errorMessage = NO_ERROR;
        if (isStartTimeSelected && meeting.getEndTime() != null) {
            if (!selectedTime.isBefore(meeting.getEndTime())) {
                errorMessage = R.string.start_time_error;
            } else if (meetingUiModel.getValue().getEndTimeErrorId() == R.string.end_time_error) {
                meetingUiModel.getValue().setEndTimeError(NO_ERROR);
            }
        } else if (!isStartTimeSelected && meeting.getStartTime() != null) {
            if (!selectedTime.isAfter(meeting.getStartTime())) {
                errorMessage = R.string.end_time_error;
            } else if (meetingUiModel.getValue().getStartTimeErrorId() == R.string.start_time_error) {
                meetingUiModel.getValue().setStartTimeError(NO_ERROR);
            }
        }

        // Update UI THEN data
        if (isStartTimeSelected) {
            meetingUiModel.getValue().setStartTimeError(errorMessage);
            meetingRepository.setStartTime(selectedTime);
        } else {
            meetingUiModel.getValue().setEndTimeError(errorMessage);
            meetingRepository.setEndTime(selectedTime);
        }
    }

    // --------------------------------------- PLACE METHODS ---------------------------------------

    /**
     * Called when the user selects the place of the meeting.
     */
    public void setPlace(@NonNull String place) {
        meetingRepository.setPlace(place);
    }

    // ------------------------------------ MEMBER LIST METHODS ------------------------------------

    /**
     * Called when the user clicks on the 'plus' button to add a member to the meeting.
     */
    // Add a member right AFTER the current position
    public void addMember(int position) {
        meetingRepository.addMember(position + 1);
    }

    /**
     * Called when the user replaces the member at the specified position in the list.
     */
    public void updateMember(int position, @NonNull String email) {
        meetingRepository.updateMember(position, email);
    }

    /**
     * Called when the user clicks on the 'minus' button to remove the member from the meeting.
     */
    public void removeMember(int position) {
        meetingRepository.removeMember(position);
    }

    // ---------------------------------------- FAB METHODS ----------------------------------------

    /**
     * Called when the user clicks on the 'add' button.
     */
    public void onAddMeeting() {
        triggerEmptyErrors();

//        if (meetingUiModel.getValue().areFieldsOk())
        checkIfSameFields();

        if (meetingUiModel.getValue().areFieldsOk()) {
            DI.getListingRepository().addMeeting(meetingRepository.getMeeting().getValue());
            endActivityEvent.call();
        }
    }

    private void triggerEmptyErrors() {
        final MeetingUiModel uiModel = meetingUiModel.getValue();

        if (uiModel.getTopic().trim().isEmpty()) {
            uiModel.setTopicError(R.string.empty_field_error);
        }
        if (uiModel.getDate().isEmpty()) {
            uiModel.setDateError(R.string.empty_field_error);
        }
        if (uiModel.getStartTime().isEmpty()) {
            uiModel.setStartTimeError(R.string.empty_field_error);
        }
        if (uiModel.getEndTime().isEmpty()) {
            uiModel.setEndTimeError(R.string.empty_field_error);
        }
        if (uiModel.getPlace().isEmpty()) {
            uiModel.setPlaceError(R.string.empty_field_error);
        }

        final List<MemberUiModel> tempList = new ArrayList<>(uiModel.getMemberList());
        for (int i = 0; i < tempList.size(); i++) {
            final MemberUiModel memberUiModel = tempList.get(i);
            if (memberUiModel.getEmail().isEmpty()) {
                tempList.set(i, new MemberUiModel(
                    memberUiModel.getId(),
                    memberUiModel.getEmail(),
                    R.string.empty_field_error,
                    memberUiModel.getAddButtonVisibility(),
                    memberUiModel.getRemoveButtonVisibility(),
                    getApplication()
                ));
            }
        }
        uiModel.setMemberList(tempList);
        meetingUiModel.setValue(uiModel);
    }

    private void checkIfSameFields() {
        if (!meetingUiModel.getValue().areAllFieldsSet())
            return;

        final Meeting currentMeeting = meetingRepository.getMeeting().getValue();
        final LocalTime currentStart = currentMeeting.getStartTime();
        final LocalTime currentEnd = currentMeeting.getEndTime();

        final StringBuilder stringBuilder = new StringBuilder();

        // Loop through existing meetings
        for (Meeting existingMeeting : DI.getListingRepository().getAllMeetings().getValue()) {
            stringBuilder.setLength(0);

            // A meeting exists the same day as the current one
            if (currentMeeting.getDate().isEqual(existingMeeting.getDate())) {
                stringBuilder.append("same date, ");
                final LocalTime existingStart = existingMeeting.getStartTime();
                final LocalTime existingEnd = existingMeeting.getEndTime();

                // A meeting exists with times that overlap the current ont
                if ((currentStart.isBefore(existingStart) && currentEnd.isAfter(existingStart)) ||
                    currentStart.equals(existingStart) ||
                    (currentStart.isAfter(existingStart) && currentStart.isBefore(existingEnd))) {

                    stringBuilder.append("time overlap, ");

                    if (currentMeeting.getPlace().equals(existingMeeting.getPlace())) {
                        meetingUiModel.getValue().setDateError(R.string.occupied_error);
                        meetingUiModel.getValue().setStartTimeError(R.string.occupied_error);
                        meetingUiModel.getValue().setEndTimeError(R.string.occupied_error);
                        meetingUiModel.getValue().setPlaceError(R.string.time_place_error, existingStart, existingEnd);
                        stringBuilder.append("same place, ");
                        break;
                    } else {
                        meetingUiModel.getValue().setDateError(NO_ERROR);
                        meetingUiModel.getValue().setStartTimeError(NO_ERROR);
                        meetingUiModel.getValue().setEndTimeError(NO_ERROR);
                        meetingUiModel.getValue().setPlaceError(NO_ERROR);
                        meetingUiModel.getValue().resetExistingTimes();
                        stringBuilder.append("not same place, ");
                    }

                    boolean timeMemberDuplicate = false;
                    final List<MemberUiModel> newList = new ArrayList<>(meetingUiModel.getValue().getMemberList());
                    for (MemberUiModel memberUiModel : newList) {
                        final String email = memberUiModel.getEmail();
                        if (existingMeeting.getMemberList().contains(email)) {
                            Log.d("Neige", "AddViewModel::doesMeetingExist: " + email + " is busy");
                            timeMemberDuplicate = true;
                            final MemberUiModel newMemberUiModel = new MemberUiModel(
                                memberUiModel.getId(),
                                email,
                                R.string.time_member_error,
                                memberUiModel.getAddButtonVisibility(),
                                memberUiModel.getRemoveButtonVisibility(),
                                getApplication()
                            );
                            newMemberUiModel.setEmailError(R.string.time_member_error, existingStart, existingEnd);
                            newList.set(newList.indexOf(memberUiModel), newMemberUiModel);
                            meetingUiModel.getValue().setMemberList(newList);
                        } else {

                        }
                    }

//                    if (placeError != null || timeMemberDuplicate) {
//                        result = true;
//                        final MeetingUiModel.Builder builder = new MeetingUiModel.Builder(meetingUiModel)
//                            .setDateError(EMPTY_ERROR_MESSAGE)
//                            .setStartTimeError(EMPTY_ERROR_MESSAGE)
//                            .setEndTimeError(EMPTY_ERROR_MESSAGE);
//                        if (placeError != null)
//                            builder.setPlaceError(placeError);
//                        if (timeMemberDuplicate)
//                            builder.setMemberList(newList);
//                        this.meetingUiModel.setValue(builder.build());
//                        break;
//                    }
                }
            } else {
                stringBuilder.append("not the same");
            }
//            Log.d("Neige", "AddViewModel::checkIfSameFields: " + stringBuilder);
        }
        meetingUiModel.setValue(meetingUiModel.getValue());
    }
}