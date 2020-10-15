package com.neige_i.mareu.view.add.model;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model UI for {@link com.neige_i.mareu.R.layout#fragment_add fragment_add} layout.
 */
public class MeetingUi {

    // ---------- INSTANCE VARIABLE

    // TextInputEditText's content
    @NonNull
    private final String topic;
    @NonNull
    private final String date;
    @NonNull
    private final String startTime;
    @NonNull
    private final String endTime;
    @NonNull
    private final String place;

    // TextInputLayout's error message
    @Nullable
    private final String topicError;
    @Nullable
    private final String dateError;
    @Nullable
    private final String startTimeError;
    @Nullable
    private final String endTimeError;
    @Nullable
    private final String placeError;

    @NonNull
    private final List<MemberUi> memberList;

    // ---------- CONSTRUCTOR

    public MeetingUi() {
        topic = "";
        date = "";
        startTime = "";
        endTime = "";
        place = "";
        topicError = null;
        dateError = null;
        startTimeError = null;
        endTimeError = null;
        placeError = null;
        memberList = new ArrayList<>(Collections.singletonList(new MemberUi("", null, View.INVISIBLE)));
    }

    public MeetingUi(@NonNull String topic, @NonNull String date, @NonNull String startTime,
                     @NonNull String endTime, @NonNull String place, @Nullable String topicError,
                     @Nullable String dateError, @Nullable String startTimeError,
                     @Nullable String endTimeError, @Nullable String placeError,
                     @NonNull List<MemberUi> memberList) {
        this.topic = topic;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.topicError = topicError;
        this.dateError = dateError;
        this.startTimeError = startTimeError;
        this.endTimeError = endTimeError;
        this.placeError = placeError;
        this.memberList = memberList;
    }

    // ---------- GETTER

    @NonNull
    public String getTopic() {
        return topic;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    @NonNull
    public String getStartTime() {
        return startTime;
    }

    @NonNull
    public String getEndTime() {
        return endTime;
    }

    @NonNull
    public String getPlace() {
        return place;
    }

    @Nullable
    public String getTopicError() {
        return topicError;
    }

    @Nullable
    public String getDateError() {
        return dateError;
    }

    @Nullable
    public String getStartTimeError() {
        return startTimeError;
    }

    @Nullable
    public String getEndTimeError() {
        return endTimeError;
    }

    @Nullable
    public String getPlaceError() {
        return placeError;
    }

    @NonNull
    public List<MemberUi> getMemberList() {
        return memberList;
    }

    @NonNull
    @Override
    public String toString() {
        return "MeetingUi{" +
            "topic='" + topic + '\'' +
            ", date='" + date + '\'' +
            ", startTime='" + startTime + '\'' +
            ", endTime='" + endTime + '\'' +
            ", place='" + place + '\'' +
            ", errors[='" + topicError + '\'' +
            ", '" + dateError + '\'' +
            ", '" + startTimeError + '\'' +
            ", '" + endTimeError + '\'' +
            ", '" + placeError + '\'' +
            "], memberList=" + memberList.size() +
            '}';
    }
}
