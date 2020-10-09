package com.neige_i.mareu.view.add;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.MeetingRepository;
import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.view.model.MeetingUi;
import com.neige_i.mareu.view.model.MemberUi;
import com.neige_i.mareu.view.util.SingleLiveEvent;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// FIXME: use final, @NonNull, @Nullable with method arguments
public class AddViewModel extends ViewModel {

    // ---------- STATE LIVE DATA

    private final MutableLiveData<MeetingUi> meetingUiLiveData = new MutableLiveData<>(new MeetingUi());

    // ---------- EVENT LIVE DATA

    private final SingleLiveEvent<LocalTime> timePicker = new SingleLiveEvent<>();
    private final SingleLiveEvent<LocalDate> datePicker = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> showSnack = new SingleLiveEvent<>(); // ASKME: @StringRes
    private final SingleLiveEvent<Void> endActivity = new SingleLiveEvent<>();

    // ---------- LOCAL VARIABLE

    private final MeetingRepository meetingRepository;
    private final Clock clock;
    private final String ERROR_MESSAGE = " "; // Only show the end icon without the message beneath the TextInputLayout
    private LocalTime localTime;
    private LocalDate localDate;

    // ---------- CONSTRUCTOR

    public AddViewModel(MeetingRepository meetingRepository, Clock clock) {
        this.meetingRepository = meetingRepository;
        this.clock = clock;
    }

    // ---------- LIVE DATA GETTER

    public LiveData<MeetingUi> getMeetingUiLiveData() {
        return meetingUiLiveData;
    }

    public LiveData<LocalTime> getTimePicker() {
        return timePicker;
    }

    public LiveData<LocalDate> getDatePicker() {
        return datePicker;
    }

    public LiveData<Integer> getShowSnack() {
        return showSnack;
    }

    public LiveData<Void> getEndActivity() {
        return endActivity;
    }

    // ---------- TOPIC METHOD

    /**
     * Changes topic value and updates its error message.
     */
    public void onTopicChanged(String topic) {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();
        assert meetingUi != null;

        meetingUiLiveData.setValue(new MeetingUi(
            topic,
            meetingUi.getDate(),
            meetingUi.getTimeStart(),
            meetingUi.getPlace(),
            !topic.trim().isEmpty() && meetingUi.getTopicError() != null ? null : meetingUi.getTopicError(),
            meetingUi.getDateError(),
            meetingUi.getTimeStartError(),
            meetingUi.getPlaceError(),
            meetingUi.getMemberList(),
            meetingUi.getAvailableMembers()
        ));
    }

    // ---------- DATE METHOD

    // TODO: disable selection date before current one

    public void onDateClicked() {
        datePicker.setValue(LocalDate.now(clock));
    }

    /**
     * Changes date value and removes its error message.
     */
    public void onDateValidated(int year, int month, int dayOfMonth) {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();
        assert meetingUi != null;

        meetingUiLiveData.setValue(new MeetingUi(
            meetingUi.getTopic(),
            String.format(Locale.FRANCE, "%02d/%02d/%d", dayOfMonth, month, year),
            meetingUi.getTimeStart(),
            meetingUi.getPlace(),
            meetingUi.getTopicError(),
            null, // ASKME: with(out) condition
            meetingUi.getTimeStartError(),
            meetingUi.getPlaceError(),
            meetingUi.getMemberList(),
            meetingUi.getAvailableMembers()
        ));
        localDate = LocalDate.of(year, month, dayOfMonth);
    }

    // ---------- TIME METHOD

    public void onTimeClicked() {
        timePicker.setValue(LocalTime.now(clock));
    }

    /**
     * Changes time value and removes its error message.
     */
    public void onTimeValidated(int hour, int minute) {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();
        assert meetingUi != null;

        meetingUiLiveData.setValue(new MeetingUi(
            meetingUi.getTopic(),
            meetingUi.getDate(),
            String.format(Locale.FRANCE, "%02d:%02d", hour, minute),
            meetingUi.getPlace(),
            meetingUi.getTopicError(),
            meetingUi.getDateError(),
            null,
            meetingUi.getPlaceError(),
            meetingUi.getMemberList(),
            meetingUi.getAvailableMembers()
        ));
        localTime = LocalTime.of(hour, minute);
    }

    // ---------- PLACE METHOD

    /**
     * Changes place value and removes its error message.
     */
    public void onPlaceSelected(String place) {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();
        assert meetingUi != null;

        meetingUiLiveData.setValue(new MeetingUi(
            meetingUi.getTopic(),
            meetingUi.getDate(),
            meetingUi.getTimeStart(),
            place,
            meetingUi.getTopicError(),
            meetingUi.getDateError(),
            meetingUi.getTimeStartError(),
            null,
            meetingUi.getMemberList(),
            meetingUi.getAvailableMembers()
        ));
    }

    // ---------- MEMBER LIST METHOD

    // TODO: member cannot be at 2 meetings at the same time
    //  cannot add more member than available (hide positive button)

    public void onAddMember(int position) {
        assert meetingUiLiveData.getValue() != null;

        position++; // Add the member AFTER the current position (hence the incrementation)

        // Init new list with LiveData content
        final List<MemberUi> newList = new ArrayList<>(meetingUiLiveData.getValue().getMemberList());

        // Apply appropriate changes
        changeFirstItemVisibility(newList);
        newList.add(position, new MemberUi("", null, View.VISIBLE));

        // Update LiveData with new value
        setMemberList(newList);
    }

    public void onRemoveMember(MemberUi memberUi) {
        assert meetingUiLiveData.getValue() != null;

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

    public void onUpdateMember(int position, String email) {
        // ASKME: handle if memberUi exists
        //  handle LiveData null value
        assert meetingUiLiveData.getValue() != null;

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

    // ASKME: should test private methods
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

    private void addAvailableMember(String memberEmail) {
        if (!memberEmail.isEmpty()) {
            final MeetingUi meetingUi = meetingUiLiveData.getValue();
            assert meetingUi != null;

            // TODO: put the member at its original position in the list
            meetingUi.getAvailableMembers().add(memberEmail);
            meetingUiLiveData.setValue(new MeetingUi(
                meetingUi.getTopic(),
                meetingUi.getDate(),
                meetingUi.getTimeStart(),
                meetingUi.getPlace(),
                meetingUi.getTopicError(),
                meetingUi.getDateError(),
                meetingUi.getTimeStartError(),
                meetingUi.getPlaceError(),
                meetingUi.getMemberList(),
                meetingUi.getAvailableMembers()
            ));
        }
    }

    private void removeAvailableMember(String memberEmail) {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();
        assert meetingUi != null;

        meetingUi.getAvailableMembers().remove(memberEmail);
        meetingUiLiveData.setValue(new MeetingUi(
            meetingUi.getTopic(),
            meetingUi.getDate(),
            meetingUi.getTimeStart(),
            meetingUi.getPlace(),
            meetingUi.getTopicError(),
            meetingUi.getDateError(),
            meetingUi.getTimeStartError(),
            meetingUi.getPlaceError(),
            meetingUi.getMemberList(),
            meetingUi.getAvailableMembers()
        ));
    }

    private void setMemberList(List<MemberUi> memberList) {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();
        assert meetingUi != null;

        meetingUiLiveData.setValue(new MeetingUi(
            meetingUi.getTopic(),
            meetingUi.getDate(),
            meetingUi.getTimeStart(),
            meetingUi.getPlace(),
            meetingUi.getTopicError(),
            meetingUi.getDateError(),
            meetingUi.getTimeStartError(),
            meetingUi.getPlaceError(),
            memberList,
            meetingUi.getAvailableMembers()
        ));
    }

    // ---------- ADD BUTTON METHOD

    public void onAddMeeting() {
        setErrorMessages();
        if (containsError())
            showSnack.setValue(R.string.mandatory_fields);
        else if (doesMeetingExist())
            showSnack.setValue(R.string.meeting_already_exist);
        else if (meetingUiLiveData.getValue() != null) {
            final List<String> emailList = new ArrayList<>();
            for (MemberUi member : meetingUiLiveData.getValue().getMemberList()) {
                emailList.add(member.getEmail());
            }
            meetingRepository.addMeeting(new Meeting(
                meetingUiLiveData.getValue().getTopic(),
                meetingUiLiveData.getValue().getPlace(),
                getLocalDateTimeTruncated(),
                emailList
            ));
            endActivity.call();
        }
    }

    /**
     * Checks if fields are empty
     */
    private void setErrorMessages() {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();
        assert meetingUi != null;

        final List<MemberUi> newList = new ArrayList<>(meetingUi.getMemberList());
        for (int i = 0; i < newList.size(); i++) {
            final MemberUi newMember = newList.get(i);
            if (newMember.getEmail().isEmpty()) {
                newList.set(i, new MemberUi(
                    newMember.getId(),
                    newMember.getEmail(),
                    ERROR_MESSAGE,
                    newMember.getRemoveButtonVisibility()
                ));
            }
        }
        meetingUiLiveData.setValue(new MeetingUi(
            meetingUi.getTopic(),
            meetingUi.getDate(),
            meetingUi.getTimeStart(),
            meetingUi.getPlace(),
            meetingUi.getTopic().trim().isEmpty() ? ERROR_MESSAGE : meetingUi.getTopicError(),
            meetingUi.getDate().isEmpty() ? ERROR_MESSAGE : meetingUi.getDateError(),
            meetingUi.getTimeStart().isEmpty() ? ERROR_MESSAGE : meetingUi.getTimeStartError(),
            meetingUi.getPlace().isEmpty() ? ERROR_MESSAGE : meetingUi.getPlaceError(),
            newList,
            meetingUi.getAvailableMembers()
        ));
    }

    private boolean containsError() {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();
        assert meetingUi != null;

        boolean listError = false;
        for (MemberUi memberUi : meetingUi.getMemberList())
            if (memberUi.getEmailError() != null) {
                listError = true;
                break;
            }
        return meetingUi.getTopicError() != null || meetingUi.getDateError() != null ||
            meetingUi.getTimeStartError() != null || meetingUi.getPlaceError() != null || listError;
    }

    /**
     * Checks if repository contains another meeting with the same time, date and place
     */
    private boolean doesMeetingExist() {
        final MeetingUi meetingUi = meetingUiLiveData.getValue();
        assert meetingUi != null;
        assert meetingRepository.getAllMeetings().getValue() != null;

        boolean alreadyExists = false;
        for (Meeting meeting : meetingRepository.getAllMeetings().getValue()) {
            if (meeting.getStartDateTime().isEqual(getLocalDateTimeTruncated())) {
                meetingUiLiveData.setValue(new MeetingUi(
                    meetingUi.getTopic(),
                    meetingUi.getDate(),
                    meetingUi.getTimeStart(),
                    meetingUi.getPlace(),
                    meetingUi.getTopicError(),
                    ERROR_MESSAGE,
                    ERROR_MESSAGE,
                    ERROR_MESSAGE,
                    meetingUi.getMemberList(),
                    meetingUi.getAvailableMembers()
                ));
                alreadyExists = true;
                break;
            }
        }
        return alreadyExists;
    }

    private LocalDateTime getLocalDateTimeTruncated() {
        return LocalDateTime.of(localDate, localTime).truncatedTo(ChronoUnit.MINUTES);
    }
}
