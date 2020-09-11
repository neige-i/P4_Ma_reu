package com.neige_i.mareu.view.add;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.MeetingRepository;
import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.view.model.MemberUi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddViewModel extends ViewModel {

    private final MeetingRepository meetingRepository;

    private final MutableLiveData<List<MemberUi>> memberList = new MutableLiveData<>(
        new ArrayList<>(Collections.singletonList(new MemberUi("", null, View.INVISIBLE))));
    private final MutableLiveData<String> topicError = new MutableLiveData<>();
    private final MutableLiveData<String> timeError = new MutableLiveData<>();
    private final MutableLiveData<String> dateError = new MutableLiveData<>();
    private final MutableLiveData<String> placeError = new MutableLiveData<>();
    // TODO: replace two following LiveData by SingleLiveEvent
    private final MutableLiveData<Integer> showSnack = new MutableLiveData<>(-1);
    private final MutableLiveData<Boolean> endActivity = new MutableLiveData<>(false);

    private final String ERROR_MESSAGE = " "; // Only show the end icon without the message beneath the TextInputLayout
    private final Calendar calendar = Calendar.getInstance();
    private int hour = calendar.get(Calendar.HOUR_OF_DAY);
    private int minute = calendar.get(Calendar.MINUTE);
    private int day = calendar.get(Calendar.DAY_OF_MONTH);
    private int month = calendar.get(Calendar.MONTH);
    private int year = calendar.get(Calendar.YEAR);
    private String topic = "";
    private String time = "";
    private String date = "";
    private String place = "";

    public AddViewModel(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    // ------------
    // Topic of the meeting
    // ------------

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
        topicError.setValue(topic.isEmpty() ? ERROR_MESSAGE : null);
    }

    public LiveData<String> getTopicError() {
        return topicError;
    }

    // ------------
    // Time of the meeting
    // ------------

    public String getTime() {
        return time;
    }

    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        time = String.format(Locale.FRANCE, "%02d:%02d", hour, minute);
        timeError.setValue(null); // Remove error
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public LiveData<String> getTimeError() {
        return timeError;
    }

    // ------------
    // Date of the meeting
    // ------------

    public String getDate() {
        return date;
    }

    public void setDate(int year, int month, int day) {
        this.day = day;
        this.month = month;
        this.year = year;
        date = String.format(Locale.FRANCE, "%02d/%02d/%d", day, month, year);
        dateError.setValue(null); // Remove error
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public LiveData<String> getDateError() {
        return dateError;
    }

    // ------------
    // Place of the meeting
    // ------------

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
        placeError.setValue(null); // Remove error
    }

    public LiveData<String> getPlaceError() {
        return placeError;
    }

    // ------------
    // Members of the meeting
    // ------------

    public LiveData<List<MemberUi>> getMemberList() {
        return memberList;
    }

    public void addMember(int position) {
        // TODO: auto scroll to newly added item
        List<MemberUi> oldValue = memberList.getValue();
        List<MemberUi> newList = new ArrayList<>();

        if (memberList.getValue() != null) {
            List<MemberUi> value = memberList.getValue();
            for (int i = 0; i < value.size(); i++) {
                MemberUi memberUi = value.get(i);
                final int visibility;
                if (i == 0) {
                    visibility = View.VISIBLE;
                } else {
                    visibility = memberUi.getRemoveButtonVisibility();
                }

                newList.add(new MemberUi(memberUi.getEmail(), memberUi.getErrorMessage(), visibility));
            }

            newList.add(position, new MemberUi("", null, View.VISIBLE));
        }

        // TODO FIX: need to rotate device to see visibility update
        //  newList's first item is changed, but memberList is also updated
        //  so, when setValue() is called, there is no diff for the first item between old and new value
        memberList.setValue(newList);
    }

    public void updateMember(int position, String email) {
        List<MemberUi> newList = new ArrayList<>();
        if (memberList.getValue() != null) {
            List<MemberUi> value = memberList.getValue();
            for (int i = 0; i < value.size(); i++) {
                MemberUi memberUi = value.get(i);
                final String resolvedEmail;
                if (i == position) {
                    resolvedEmail = email;
                } else {
                    resolvedEmail = memberUi.getEmail();
                }

                newList.add(new MemberUi(resolvedEmail, memberUi.getErrorMessage(), memberUi.getRemoveButtonVisibility()));
            }
        }
        memberList.setValue(newList);
    }

    public void removeMember(int position) {
        List<MemberUi> oldValue = memberList.getValue();
        List<MemberUi> newList = new ArrayList<>();

        if (memberList.getValue() != null) {
            List<MemberUi> value = memberList.getValue();
            for (int i = 0; i < value.size(); i++) {
                MemberUi memberUi = value.get(i);
                final int visibility;
                if (i == 0) {
                    if (oldValue != null && oldValue.size() == 1) {
                        visibility = View.INVISIBLE;
                    } else {
                        visibility = View.VISIBLE;
                    }
                } else {
                    visibility = memberUi.getRemoveButtonVisibility();
                }

                if (position != i) {
                    newList.add(new MemberUi(memberUi.getEmail(), memberUi.getErrorMessage(), visibility));
                }
            }
        }

        // TODO FIX: need to rotate device to see visibility update
        //  newList's first item is changed, but memberList is also updated
        //  so, when setValue() is called, there is no diff for the first item between old and new value
        memberList.setValue(newList);
    }

    // ------------
    // Handle 'add meeting' event
    // ------------

    public void onAddMeeting() {
        if (areFieldsCorrect())
            showSnack.setValue(R.string.mandatory_fields);
        else if (doesMeetingExist())
            showSnack.setValue(R.string.meeting_already_exist);
        else {
            List<String> emailList = new ArrayList<>();
            for (MemberUi member : Objects.requireNonNull(memberList.getValue())) {
                emailList.add(member.getEmail());
            }
            meetingRepository.addMeeting(new Meeting(topic, place, calendar, emailList));
            endActivity.setValue(true);
        }
    }

    /**
     * Checks if fields are empty
     */
    private boolean areFieldsCorrect() {
        boolean error = false;
        if (topic.isEmpty()) {
            topicError.setValue(ERROR_MESSAGE);
            error = true;
        }
        if (time.isEmpty()) {
            timeError.setValue(ERROR_MESSAGE);
            error = true;
        }
        if (date.isEmpty()) {
            dateError.setValue(ERROR_MESSAGE);
            error = true;
        }
        if (place.isEmpty()) {
            placeError.setValue(ERROR_MESSAGE);
            error = true;
        }/* TODO IBRAHIM to fix (remap deep copy)
        for (MemberUi memberUi : memberList.getValue()) {
            if (memberUi.getEmail().isEmpty()) {
                memberUi.setErrorMessage(ERROR_MESSAGE);
                error = true;
            }
            // TODO FIX: need to rotate device to see error update
            memberList.setValue(memberList.getValue());
        }*/
        return error;
    }

    /**
     * Checks if repository contains another meeting with the same time, date and place
     */
    private boolean doesMeetingExist() {
        calendar.set(year, month, day, hour, minute);
        boolean alreadyExists = false;
        for (Meeting meeting : Objects.requireNonNull(meetingRepository.getAllMeetings().getValue())) {
            Calendar calendar = meeting.getDate();
            if (calendar.get(Calendar.YEAR) == this.calendar.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == this.calendar.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_MONTH) == this.calendar.get(Calendar.DAY_OF_MONTH)
                && calendar.get(Calendar.HOUR_OF_DAY) == this.calendar.get(Calendar.HOUR_OF_DAY)
                && calendar.get(Calendar.MINUTE) == this.calendar.get(Calendar.MINUTE)
                && meeting.getPlace().equals(place)
            ) {
                timeError.setValue(ERROR_MESSAGE);
                dateError.setValue(ERROR_MESSAGE);
                placeError.setValue(ERROR_MESSAGE);
                alreadyExists = true;
                break;
            }
        }
        return alreadyExists;
    }

    public LiveData<Integer> getShowSnack() {
        return showSnack;
    }

    public void cancelShowSnack() {
        showSnack.setValue(-1);
    }

    public LiveData<Boolean> getEndActivity() {
        return endActivity;
    }

    public void cancelEndActivity() {
        endActivity.setValue(false);
    }
}
