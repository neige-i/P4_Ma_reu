package com.neige_i.mareu.view.add;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.MeetingRepository;
import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.view.util.SingleLiveEvent;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.neige_i.mareu.view.util.Util.DATE_FORMAT;
import static com.neige_i.mareu.view.util.Util.TIME_FORMAT;

public class AddViewModel extends ViewModel {

    // ------------------------------------  INSTANCE VARIABLES ------------------------------------

    @NonNull
    private final MeetingRepository meetingRepository;
    @NonNull
    private final Clock clock; // Handy for testing

    // -------------------------------------  STATE LIVE DATA --------------------------------------

    @NonNull
    private final MutableLiveData<MeetingUiModel> meetingUiModel = new MutableLiveData<>(new MeetingUiModel());

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

    @NonNull
    private final String EMPTY_ERROR_MESSAGE = " ";

    private boolean isStartTime;

    // ----------------------------------- CONSTRUCTOR & GETTERS -----------------------------------

    public AddViewModel(@NonNull MeetingRepository meetingRepository, @NonNull Clock clock) {
        meetingRepository.resetAvailableMembers();
        this.meetingRepository = meetingRepository;
        this.clock = clock;
    }

    @NonNull
    public LiveData<MeetingUiModel> getMeetingUiModel() {
        return meetingUiModel;
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
        final MeetingUiModel.Builder builder = new MeetingUiModel.Builder(meetingUiModel.getValue()) // Get current object
            .setTopic(topic); // Change appropriate field
        if (!topic.trim().isEmpty())
            builder.setTopicError(null); // Change appropriate field
        meetingUiModel.setValue(builder.build()); // Return modified object
    }

    // --------------------------------------- DATE METHODS ----------------------------------------

    /**
     * Called when the user clicks to choose the date of the meeting.
     */
    public void showDatePickerDialog() {
        datePickerEvent.setValue(getLocalDate() == null ? LocalDate.now(clock) : getLocalDate());
    }

    /**
     * Returns the date of the meeting or null if not set yet.
     */
    @Nullable
    private LocalDate getLocalDate() {
        final String date = meetingUiModel.getValue().getDate();
        return date.isEmpty() ? null : LocalDate.parse(date, DATE_FORMAT);
    }

    /**
     * Called when the user validates the date of the meeting.
     */
    public void setDate(int year, int month, int dayOfMonth) {
        meetingUiModel.setValue(new MeetingUiModel.Builder(meetingUiModel.getValue())
                                    .setDate(LocalDate.of(year, month, dayOfMonth).format(DATE_FORMAT))
                                    .setDateError(null)
                                    .build()
        );
    }

    // --------------------------------------- TIME METHODS ----------------------------------------

    /**
     * Called when the user clicks to choose the start time or end time of the meeting.
     */
    public void showTimePickerDialog(@IdRes int timeInputId) {
        isStartTime = timeInputId == R.id.start_time_input;

        final LocalTime localTime = isStartTime ? getStartTime() : getEndTime();
        timePickerEvent.setValue(localTime == null ? LocalTime.now(clock) : localTime);
    }

    /**
     * Returns the start time of the meeting or null if not set yet.
     */
    @Nullable
    private LocalTime getStartTime() {
        final String startTime = meetingUiModel.getValue().getStartTime();
        return startTime.isEmpty() ? null : LocalTime.parse(startTime, TIME_FORMAT);
    }

    /**
     * Returns the end time of the meeting or null if not set yet.
     */
    @Nullable
    private LocalTime getEndTime() {
        final String endTime = meetingUiModel.getValue().getEndTime();
        return endTime.isEmpty() ? null : LocalTime.parse(endTime, TIME_FORMAT);
    }

    /**
     * Called when the user validates the start time or end time of the meeting.
     */
    // FIXME: application context
    public void setTime(int hour, int minute, @NonNull Context context) {
        // Check if start time is strictly before end time (times can't be the same)
        final LocalTime selectedTime = LocalTime.of(hour, minute);
        final String errorMessage;
        if (isStartTime && getEndTime() != null && !selectedTime.isBefore(getEndTime())) {
            errorMessage = context.getString(R.string.start_time_error, getEndTime().toString());
        } else if (!isStartTime && getStartTime() != null && !selectedTime.isAfter(getStartTime())) {
            errorMessage = context.getString(R.string.end_time_error, getStartTime().toString());
        } else {
            errorMessage = null;
        }

        final String stringTime = selectedTime.format(TIME_FORMAT);
        final MeetingUiModel.Builder builder = new MeetingUiModel.Builder(meetingUiModel.getValue());
        if (isStartTime)
            builder.setStartTime(stringTime).setStartTimeError(errorMessage);
        else
            builder.setEndTime(stringTime).setEndTimeError(errorMessage);
        meetingUiModel.setValue(builder.build());
    }

    // --------------------------------------- PLACE METHODS ---------------------------------------

    /**
     * Called when the user selects the place of the meeting.
     */
    public void setPlace(@NonNull String place) {
        meetingUiModel.setValue(new MeetingUiModel.Builder(meetingUiModel.getValue())
                                    .setPlace(place)
                                    .setPlaceError(null)
                                    .build()
        );
    }

    // ------------------------------------ MEMBER LIST METHODS ------------------------------------

    // TODO: member cannot be at 2 meetings at the same time
    //  cannot add more member than available (hide positive button)

    /**
     * Called when the user clicks on the 'plus' button to add a member to the meeting.
     */
    public void addMember(int position) {
        position++; // Add the member AFTER the current position (hence the incrementation)

        // Init new list with LiveData content
        final List<MemberUiModel> newList = new ArrayList<>(meetingUiModel.getValue().getMemberList());

        // Apply appropriate changes
        changeFirstItemVisibility(newList);
        newList.add(position, new MemberUiModel("", null, View.VISIBLE));

        // Update LiveData with new value
        setMemberList(newList);
    }

    /**
     * Called when the user clicks on the 'minus' button to remove the member from the meeting.
     */
    public void removeMember(@NonNull MemberUiModel memberUiModel) {
        // Init new list with LiveData content
        final List<MemberUiModel> newList = new ArrayList<>(meetingUiModel.getValue().getMemberList());

        // Update available member list
        addAvailableMember(memberUiModel.getEmail());

        // Apply appropriate changes
        newList.remove(memberUiModel);
        changeFirstItemVisibility(newList);

        // Update LiveData with new value
        setMemberList(newList);
    }

    /**
     * Called when the user replaces the member at the specified position in the list.
     */
    public void updateMember(int position, @NonNull String email) {
        // Init new list with LiveData content
        final List<MemberUiModel> newList = new ArrayList<>(meetingUiModel.getValue().getMemberList());
        final MemberUiModel oldMember = newList.get(position);

        // Update available member list
        addAvailableMember(oldMember.getEmail());
        removeAvailableMember(email);

        // Apply appropriate changes
        newList.set(position, new MemberUiModel(
            oldMember.getId(),
            email,
            null,
            oldMember.getRemoveButtonVisibility()
        ));

        // Update LiveData with new value
        setMemberList(newList);
    }

    /**
     * Toggles the visibility of the 'minus' button of the first member of the specified list.
     */
    private void changeFirstItemVisibility(@NonNull final List<MemberUiModel> memberList) {
        if (memberList.size() == 1) {
            // Get first element
            final MemberUiModel firstMember = memberList.get(0);

            // Toggle first member's button visibility between VISIBLE and INVISIBLE
            memberList.set(0, new MemberUiModel(
                firstMember.getId(),
                firstMember.getEmail(),
                firstMember.getEmailError(),
                firstMember.getRemoveButtonVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE
            ));
        }
    }

    /**
     * Adds the specified email address to the available ones for the meeting.
     */
    private void addAvailableMember(@NonNull String memberEmail) {
        if (!memberEmail.isEmpty()) {
            // TODO: put the member at its original position in the list
            meetingRepository.addAvailableMembers(memberEmail);
        }
    }

    /**
     * Removes the specified email address from the available ones for the meeting.
     */
    private void removeAvailableMember(@NonNull String memberEmail) {
        meetingRepository.removeAvailableMembers(memberEmail);
    }

    /**
     * Updates the member list of the meeting.
     */
    private void setMemberList(@NonNull List<MemberUiModel> memberList) {
        meetingUiModel.setValue(new MeetingUiModel.Builder(meetingUiModel.getValue())
                                    .setMemberList(memberList)
                                    .build()
        );
    }

    // ---------------------------------------- FAB METHODS ----------------------------------------

    /**
     * Called when the user clicks on the 'add' button.
     */
    public void onAddMeeting(@NonNull Context context) {
        if (setErrorMessages())
            showSnackEvent.setValue(context.getString(R.string.mandatory_fields));
        else if (doesMeetingExist(context)) // TODO: remove snack and make doesMeetingExist() returns void
            showSnackEvent.setValue(context.getString(R.string.meeting_already_exist));
        else if (meetingUiModel.getValue() != null) {
            final List<String> emailList = new ArrayList<>();
            for (MemberUiModel member : meetingUiModel.getValue().getMemberList()) {
                emailList.add(member.getEmail());
            }
            meetingRepository.addMeeting(new Meeting(
                meetingUiModel.getValue().getTopic(),
                meetingUiModel.getValue().getPlace(),
                getLocalDate().atTime(getStartTime()),
                getLocalDate().atTime(getEndTime()),
                emailList
            ));
            endActivityEvent.call();
        }
    }

    /**
     * Checks if fields are corrects and updates errors accordingly.<br />
     * Returns true if there is at least 1 error, false otherwise.
     */
    private boolean setErrorMessages() {
        boolean containsError = false;
        final MeetingUiModel meetingUiModel = this.meetingUiModel.getValue();

        final List<MemberUiModel> newList = new ArrayList<>(meetingUiModel.getMemberList());
        for (MemberUiModel memberUiModel : newList) {
            if (memberUiModel.getEmail().isEmpty()) {
                containsError = true;
                newList.set(newList.indexOf(memberUiModel), new MemberUiModel(
                    memberUiModel.getId(),
                    memberUiModel.getEmail(),
                    EMPTY_ERROR_MESSAGE, // Update member error if email value is an empty String
                    memberUiModel.getRemoveButtonVisibility()
                ));
            }
        }
        final MeetingUiModel.Builder builder = new MeetingUiModel.Builder(meetingUiModel).setMemberList(newList);
        if (meetingUiModel.getTopic().trim().isEmpty()) {
            containsError = true;
            builder.setTopicError(EMPTY_ERROR_MESSAGE);
        }
        if (meetingUiModel.getDate().isEmpty()) {
            containsError = true;
            builder.setDateError(EMPTY_ERROR_MESSAGE);
        }
        if (meetingUiModel.getStartTime().isEmpty()) {
            containsError = true;
            builder.setStartTimeError(EMPTY_ERROR_MESSAGE);
        }
        if (meetingUiModel.getEndTime().isEmpty()) {
            containsError = true;
            builder.setEndTimeError(EMPTY_ERROR_MESSAGE);
        }
        if (meetingUiModel.getPlace().isEmpty()) {
            containsError = true;
            builder.setPlaceError(EMPTY_ERROR_MESSAGE);
        }
        this.meetingUiModel.setValue(builder.build());

        return containsError;
    }

    /**
     * Checks if repository contains another meeting with the same time, date and place
     */
    private boolean doesMeetingExist(@NonNull Context context) { // TODO: change name
        final MeetingUiModel meetingUiModel = this.meetingUiModel.getValue();

        boolean result = false;

        for (Meeting oldMeeting : meetingRepository.getAllMeetings().getValue()) {
            final LocalDateTime oldStart = oldMeeting.getStartDateTime();
            final LocalDateTime oldEnd = oldMeeting.getEndDateTime();
            final LocalDateTime newStart = getLocalDate().atTime(getStartTime());
            final LocalDateTime newEnd = getLocalDate().atTime(getEndTime());
            // Only conditions where this meeting doesn't overlap an existing one
            Log.d("Neige", "AddViewModel::doesMeetingExist: " + newStart.isBefore(oldStart) + " " +
                newEnd.isAfter(oldStart) + " " + newStart.isAfter(oldStart) + " " + newStart.isBefore(oldEnd));
            if ((newStart.isBefore(oldStart) && newEnd.isAfter(oldStart)) || newStart.isEqual(oldStart) ||
                (newStart.isAfter(oldStart) && newStart.isBefore(oldEnd))) {

                Log.d("Neige", "AddViewModel::doesMeetingExist: overlap meeting with");

                String placeError = null;
                if (meetingUiModel.getPlace().equals(oldMeeting.getPlace())) {
                    Log.d("Neige", "AddViewModel::doesMeetingExist: " + meetingUiModel.getPlace() + " already taken");
                    // TODO: there already is a meeting this time and place
                    placeError = context.getString(
                        R.string.time_place_error,
                        oldStart.toLocalTime().toString(),
                        oldEnd.toLocalTime().toString()
                    );
                }

                boolean timeMemberDuplicate = false;
                final List<MemberUiModel> newList = new ArrayList<>(meetingUiModel.getMemberList());
                for (MemberUiModel memberUiModel : newList) {
                    final String email = memberUiModel.getEmail();
                    if (oldMeeting.getEmailList().contains(email)) {
                        Log.d("Neige", "AddViewModel::doesMeetingExist: " + email + " is busy");
                        timeMemberDuplicate = true;
                        // TODO: this member is already in a meeting at this time
                        newList.set(newList.indexOf(memberUiModel), new MemberUiModel(
                            memberUiModel.getId(),
                            email,
                            context.getString(
                                R.string.time_member_error,
                                email.substring(0, email.indexOf("@")),
                                oldStart.toLocalTime().toString(),
                                oldEnd.toLocalTime().toString()
                            ),
                            memberUiModel.getRemoveButtonVisibility()
                        ));
                    }
                }

                if (placeError != null || timeMemberDuplicate) {
                    result = true;
                    // TODO: builder
                    final MeetingUiModel.Builder builder = new MeetingUiModel.Builder(meetingUiModel)
                        .setDateError(EMPTY_ERROR_MESSAGE)
                        .setStartTimeError(EMPTY_ERROR_MESSAGE)
                        .setEndTimeError(EMPTY_ERROR_MESSAGE);
                    if (placeError != null)
                        builder.setPlaceError(placeError);
                    if (timeMemberDuplicate)
                        builder.setMemberList(newList);
                    this.meetingUiModel.setValue(builder.build());
                    break;
                }
            }
        }
        return result;
    }
}