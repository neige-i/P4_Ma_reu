package com.neige_i.mareu.view.add;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DI;
import com.neige_i.mareu.data.MeetingRepository;
import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.view.add.model.MeetingUi;
import com.neige_i.mareu.view.add.model.MemberUi;
import com.neige_i.mareu.view.util.SingleLiveEvent;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.neige_i.mareu.view.util.Util.DATE_FORMAT;
import static com.neige_i.mareu.view.util.Util.TIME_FORMAT;

// FIXME: use final, @NonNull, @Nullable with method arguments
public class AddViewModel extends ViewModel {

    // ---------- CLASS VARIABLE

    @NonNull
    private static final String EMPTY_ERROR_MESSAGE = " ";

    // ---------- STATE LIVE DATA

//    @NonNull
//    private final MutableLiveData<MeetingUi> meetingUiLiveData = DI.getMeetingUi();// = new MutableLiveData<>(new MeetingUi());

    // ---------- EVENT LIVE DATA

    @NonNull
    private final SingleLiveEvent<LocalTime> timePicker = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<LocalDate> datePicker = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<String> showSnack = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<Void> endActivity = new SingleLiveEvent<>();

    // ---------- LOCAL VARIABLE

    @NonNull
    private final MeetingRepository meetingRepository;
    @NonNull
    private final Clock clock;
    @NonNull
    private final MutableLiveData<MeetingUi> meetingUiLiveData;
    private boolean isStartTime;

    // ---------- CONSTRUCTOR

    public AddViewModel(@NonNull MeetingRepository meetingRepository,
                        @NonNull MutableLiveData<MeetingUi> meetingUiLiveData, @NonNull Clock clock) {
        this.meetingRepository = meetingRepository;
        this.meetingUiLiveData = meetingUiLiveData;
        this.clock = clock;
    }

    // ---------- LIVE DATA GETTER

    @NonNull
    public LiveData<MeetingUi> getMeetingUiLiveData() {
        return meetingUiLiveData;
    }

    @NonNull
    public LiveData<LocalTime> getTimePicker() {
        return timePicker;
    }

    @NonNull
    public LiveData<LocalDate> getDatePicker() {
        return datePicker;
    }

    @NonNull
    public LiveData<String> getShowSnack() {
        return showSnack;
    }

    @NonNull
    public LiveData<Void> getEndActivity() {
        return endActivity;
    }

    // ---------- TOPIC METHOD

    /**
     * Called when the user types the topic of the meeting.
     */
    public void onTopicChanged(@NonNull String topic) {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();
        meetingUiLiveData.setValue(new MeetingUi(
            topic, // Update topic value
            meetingUi.getDate(),
            meetingUi.getStartTime(),
            meetingUi.getEndTime(),
            meetingUi.getPlace(),
            // Remove topic error only if present and if the value is equivalent to an empty String
            meetingUi.getTopicError() != null && !topic.trim().isEmpty() ? null : meetingUi.getTopicError(),
            meetingUi.getDateError(),
            meetingUi.getStartTimeError(),
            meetingUi.getStartTimeError(),
            meetingUi.getPlaceError(),
            meetingUi.getMemberList()
        ));
    }

    // ---------- DATE METHOD

    /**
     * Called when the user clicks to choose the date of the meeting.
     */
    public void onDateClicked() {
        datePicker.setValue(getLocalDate() == null ? LocalDate.now(clock) : getLocalDate());
    }

    /**
     * Returns the date of the meeting or null if not set yet.
     */
    @Nullable
    private LocalDate getLocalDate() {
        final String date = meetingUiLiveData.getValue().getDate();
        return date.isEmpty() ? null : LocalDate.parse(date, DATE_FORMAT);
    }

    /**
     * Called when the user validates the date of the meeting.
     */
    public void onDateChanged(int year, int month, int dayOfMonth) {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();

        meetingUiLiveData.setValue(new MeetingUi(
            meetingUi.getTopic(),
            LocalDate.of(year, month, dayOfMonth).format(DATE_FORMAT), // Update date value
            meetingUi.getStartTime(),
            meetingUi.getEndTime(),
            meetingUi.getPlace(),
            meetingUi.getTopicError(),
            null, // Remove date error
            meetingUi.getStartTimeError(),
            meetingUi.getStartTimeError(),
            meetingUi.getPlaceError(),
            meetingUi.getMemberList()
        ));
    }

    // ---------- TIME METHOD

    /**
     * Called when the user clicks to choose the start time or end time of the meeting.
     */
    public void onTimeClicked(@IdRes int timeInputId) {
        isStartTime = timeInputId == R.id.start_time_input;

        final LocalTime localTime = isStartTime ? getStartTime() : getEndTime();
        timePicker.setValue(localTime == null ? LocalTime.now(clock) : localTime);
    }

    /**
     * Returns the start time of the meeting or null if not set yet.
     */
    @Nullable
    private LocalTime getStartTime() {
        final String startTime = meetingUiLiveData.getValue().getStartTime();
        return startTime.isEmpty() ? null : LocalTime.parse(startTime, TIME_FORMAT);
    }

    /**
     * Returns the end time of the meeting or null if not set yet.
     */
    @Nullable
    private LocalTime getEndTime() {
        final String endTime = meetingUiLiveData.getValue().getEndTime();
        return endTime.isEmpty() ? null : LocalTime.parse(endTime, TIME_FORMAT);
    }

    /**
     * Called when the user validates the start time or end time of the meeting.
     */
    public void onTimeChanged(int hour, int minute, @NonNull Context context) {
        // ASKME: move code to method
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

        final MeetingUi meetingUi = meetingUiLiveData.getValue();
        final String stringTime = selectedTime.format(TIME_FORMAT);
        meetingUiLiveData.setValue(new MeetingUi(
            meetingUi.getTopic(),
            meetingUi.getDate(),
            // Update the appropriate time value that has been clicked
            isStartTime ? stringTime : meetingUi.getStartTime(),
            !isStartTime ? stringTime : meetingUi.getEndTime(),
            meetingUi.getPlace(),
            meetingUi.getTopicError(),
            meetingUi.getDateError(),
            // Update the appropriate time error
            isStartTime ? errorMessage : meetingUi.getStartTimeError(),
            !isStartTime ? errorMessage : meetingUi.getEndTimeError(),
            meetingUi.getPlaceError(),
            meetingUi.getMemberList()
        ));
    }

    // ---------- PLACE METHOD

    /**
     * Called when the user selects the place of the meeting.
     */
    public void onPlaceChanged(@NonNull String place) {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();

        meetingUiLiveData.setValue(new MeetingUi(
            meetingUi.getTopic(),
            meetingUi.getDate(),
            meetingUi.getStartTime(),
            meetingUi.getEndTime(),
            place, // Update place value
            meetingUi.getTopicError(),
            meetingUi.getDateError(),
            meetingUi.getStartTimeError(),
            meetingUi.getStartTimeError(),
            null, // Remove place error
            meetingUi.getMemberList()
        ));
    }

    // ---------- MEMBER LIST METHOD

    // TODO: member cannot be at 2 meetings at the same time
    //  cannot add more member than available (hide positive button)

    /**
     * Called when the user clicks on the 'plus' button to add a member to the meeting.
     */
    public void onAddMember(int position) {
        position++; // Add the member AFTER the current position (hence the incrementation)

        // Init new list with LiveData content
        final List<MemberUi> newList = new ArrayList<>(meetingUiLiveData.getValue().getMemberList());

        // Apply appropriate changes
        changeFirstItemVisibility(newList);
        newList.add(position, new MemberUi("", null, View.VISIBLE));

        // Update LiveData with new value
        setMemberList(newList);
    }

    /**
     * Called when the user clicks on the 'minus' button to remove the member from the meeting.
     */
    public void onRemoveMember(@NonNull MemberUi memberUi) {
        // Init new list with LiveData content
        final List<MemberUi> newList = new ArrayList<>(meetingUiLiveData.getValue().getMemberList());

        // Update available member list
        addAvailableMember(memberUi.getEmail());

        // Apply appropriate changes
        newList.remove(memberUi);
        changeFirstItemVisibility(newList);

        // Update LiveData with new value
        setMemberList(newList);
    }

    /**
     * Called when the user replaces the member at the specified position in the list.
     */
    public void onUpdateMember(int position, @NonNull String email) {
        // Init new list with LiveData content
        final List<MemberUi> newList = new ArrayList<>(meetingUiLiveData.getValue().getMemberList());
        final MemberUi oldMember = newList.get(position);

        // Update available member list
        addAvailableMember(oldMember.getEmail());
        removeAvailableMember(email);

        // Apply appropriate changes
        newList.set(position, new MemberUi(
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
    private void changeFirstItemVisibility(@NonNull final List<MemberUi> memberList) {
        if (memberList.size() == 1) {
            // Get first element
            final MemberUi firstMember = memberList.get(0);

            // Toggle first member's button visibility between VISIBLE and INVISIBLE
            memberList.set(0, new MemberUi(
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
            DI.getAvailableMembers().add(memberEmail);

            final MeetingUi meetingUi = meetingUiLiveData.getValue();

            // TODO: put the member at its original position in the list
//            meetingUi.getAvailableMembers().add(memberEmail);
            meetingUiLiveData.setValue(new MeetingUi(
                meetingUi.getTopic(),
                meetingUi.getDate(),
                meetingUi.getStartTime(),
                meetingUi.getEndTime(),
                meetingUi.getPlace(),
                meetingUi.getTopicError(),
                meetingUi.getDateError(),
                meetingUi.getStartTimeError(),
                meetingUi.getStartTimeError(),
                meetingUi.getPlaceError(),
                meetingUi.getMemberList()
            ));
        }
    }

    /**
     * Removes the specified email address from the available ones for the meeting.
     */
    private void removeAvailableMember(@NonNull String memberEmail) {
        DI.getAvailableMembers().remove(memberEmail);
        final MeetingUi meetingUi = meetingUiLiveData.getValue();

//        meetingUi.getAvailableMembers().remove(memberEmail);
        meetingUiLiveData.setValue(new MeetingUi(
            meetingUi.getTopic(),
            meetingUi.getDate(),
            meetingUi.getStartTime(),
            meetingUi.getEndTime(),
            meetingUi.getPlace(),
            meetingUi.getTopicError(),
            meetingUi.getDateError(),
            meetingUi.getStartTimeError(),
            meetingUi.getStartTimeError(),
            meetingUi.getPlaceError(),
            meetingUi.getMemberList()
        ));
    }

    /**
     * Updates the member list of the meeting.
     */
    private void setMemberList(@NonNull List<MemberUi> memberList) {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();

        meetingUiLiveData.setValue(new MeetingUi(
            meetingUi.getTopic(),
            meetingUi.getDate(),
            meetingUi.getStartTime(),
            meetingUi.getEndTime(),
            meetingUi.getPlace(),
            meetingUi.getTopicError(),
            meetingUi.getDateError(),
            meetingUi.getStartTimeError(),
            meetingUi.getStartTimeError(),
            meetingUi.getPlaceError(),
            memberList
        ));
    }

    // ---------- ADD BUTTON METHOD

    /**
     * Called when the user clicks on the 'add' button.
     */
    public void onAddMeeting(@NonNull Context context) {
        setErrorMessages();
        if (containsError())
            showSnack.setValue(context.getString(R.string.mandatory_fields));
        else if (doesMeetingExist(context)) // TODO: remove snack and make doesMeetingExist() returns void
            showSnack.setValue(context.getString(R.string.meeting_already_exist));
        else if (meetingUiLiveData.getValue() != null) {
            final List<String> emailList = new ArrayList<>();
            for (MemberUi member : meetingUiLiveData.getValue().getMemberList()) {
                emailList.add(member.getEmail());
            }
            meetingRepository.addMeeting(new Meeting(
                meetingUiLiveData.getValue().getTopic(),
                meetingUiLiveData.getValue().getPlace(),
                getDateTimeTruncated(getStartTime()),
                getDateTimeTruncated(getEndTime()),
                emailList
            ));
            endActivity.call();
        }
    }

    /**
     * Checks if fields are corrects and updates errors accordingly.
     */
    private void setErrorMessages() {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();

        final List<MemberUi> newList = new ArrayList<>(meetingUi.getMemberList());
        for (MemberUi memberUi : newList) {
            if (memberUi.getEmail().isEmpty()) {
                newList.set(newList.indexOf(memberUi), new MemberUi(
                    memberUi.getId(),
                    memberUi.getEmail(),
                    EMPTY_ERROR_MESSAGE, // Update member error if email value is an empty String
                    memberUi.getRemoveButtonVisibility()
                ));
            }
        }
        meetingUiLiveData.setValue(new MeetingUi(
            meetingUi.getTopic(),
            meetingUi.getDate(),
            meetingUi.getStartTime(),
            meetingUi.getEndTime(),
            meetingUi.getPlace(),
            // Update errors if corresponding values are empty Strings
            meetingUi.getTopic().trim().isEmpty() ? EMPTY_ERROR_MESSAGE : meetingUi.getTopicError(),
            meetingUi.getDate().isEmpty() ? EMPTY_ERROR_MESSAGE : meetingUi.getDateError(),
            meetingUi.getStartTime().isEmpty() ? EMPTY_ERROR_MESSAGE : meetingUi.getStartTimeError(),
            meetingUi.getEndTime().isEmpty() ? EMPTY_ERROR_MESSAGE : meetingUi.getEndTimeError(),
            meetingUi.getPlace().isEmpty() ? EMPTY_ERROR_MESSAGE : meetingUi.getPlaceError(),
            newList
        ));
    }

    /**
     * Returns true if there is at least 1 error, false otherwise.
     */
    private boolean containsError() {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();

        boolean listError = false;
        for (MemberUi memberUi : meetingUi.getMemberList())
            if (memberUi.getEmailError() != null) {
                listError = true;
                break;
            }
        return meetingUi.getTopicError() != null || meetingUi.getDateError() != null ||
            meetingUi.getStartTimeError() != null || meetingUi.getEndTimeError() != null ||
            meetingUi.getPlaceError() != null || listError;
    }

    /**
     * Checks if repository contains another meeting with the same time, date and place
     */
    private boolean doesMeetingExist(@NonNull Context context) { // TODO: change name
        final MeetingUi meetingUi = meetingUiLiveData.getValue();

        boolean result = false;

        for (Meeting oldMeeting : meetingRepository.getAllMeetings().getValue()) {
            final LocalDateTime oldStart = oldMeeting.getStartDateTime();
            final LocalDateTime oldEnd = oldMeeting.getEndDateTime();
            final LocalDateTime newStart = getDateTimeTruncated(getStartTime());
            final LocalDateTime newEnd = getDateTimeTruncated(getEndTime());
            // Only conditions where this meeting doesn't overlap an existing one
            if ((!newStart.isAfter(oldStart) && newEnd.isAfter(oldStart)) ||
                (!newEnd.isBefore(oldEnd) && newStart.isBefore(oldEnd))) {

                Log.d("Neige", "AddViewModel::doesMeetingExist: overlap meeting with");

                String placeError = null;
                if (meetingUi.getPlace().equals(oldMeeting.getPlace())) {
                    Log.d("Neige", "AddViewModel::doesMeetingExist: " + meetingUi.getPlace() + " already taken");
                    // TODO: there already is a meeting this time and place
                    placeError = context.getString(
                        R.string.time_place_error,
                        oldStart.toLocalTime().toString(),
                        oldEnd.toLocalTime().toString()
                    );
                }

                boolean timeMemberDuplicate = false;
                final List<MemberUi> newList = new ArrayList<>(meetingUi.getMemberList());
                for (MemberUi memberUi : newList) {
                    final String email = memberUi.getEmail();
                    if (oldMeeting.getEmailList().contains(email)) {
                        Log.d("Neige", "AddViewModel::doesMeetingExist: " + email + " is busy");
                        timeMemberDuplicate = true;
                        // TODO: this member is already in a meeting at this time
                        newList.set(newList.indexOf(memberUi), new MemberUi(
                            memberUi.getId(),
                            email,
                            context.getString(
                                R.string.time_member_error,
                                email.substring(0, email.indexOf("@")),
                                oldStart.toLocalTime().toString(),
                                oldEnd.toLocalTime().toString()
                            ),
                            memberUi.getRemoveButtonVisibility()
                        ));
                    }
                }

                if (placeError != null || timeMemberDuplicate) {
                    result = true;
                    meetingUiLiveData.setValue(new MeetingUi(
                        meetingUi.getTopic(),
                        meetingUi.getDate(),
                        meetingUi.getStartTime(),
                        meetingUi.getEndTime(),
                        meetingUi.getPlace(),
                        meetingUi.getTopicError(),
                        // Update date, time, place and member list errors
                        EMPTY_ERROR_MESSAGE,
                        EMPTY_ERROR_MESSAGE,
                        EMPTY_ERROR_MESSAGE,
                        placeError != null ? placeError : meetingUi.getPlaceError(),
                        timeMemberDuplicate ? newList : meetingUi.getMemberList()
                    ));
                    break;
                }
            }
        }
        return result;
    }

    @Nullable
    private LocalDateTime getDateTimeTruncated(LocalTime localTime) {
        if (getLocalDate() == null || localTime == null) {
            return null;
        } else {
            return LocalDateTime.of(getLocalDate(), localTime).truncatedTo(ChronoUnit.MINUTES); // TODO: truncatedTo() might be unnecessary
        }
    }
}
// ASKME: class too long