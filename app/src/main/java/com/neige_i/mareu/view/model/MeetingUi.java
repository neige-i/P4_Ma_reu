// TODO: move package
package com.neige_i.mareu.view.model;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.neige_i.mareu.data.DummyGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model UI for {@link com.neige_i.mareu.R.layout#fragment_add fragment_add} layout.
 */
public class MeetingUi {

    // ---------- INSTANCE VARIABLE

    // TextInputEditText's content
    // ASKME: NonNull vs Nullable?
    @NonNull
    private final String topic;
    @NonNull
    private final String date;
    @NonNull
    private final String timeStart;
    @NonNull
    private final String place;

    // TextInputLayout's error message
    @Nullable
    private final String topicError;
    @Nullable
    private final String dateError;
    @Nullable
    private final String timeStartError;
    @Nullable
    private final String placeError;

    @NonNull
    private final List<MemberUi> memberList;
    @NonNull
    private final List<String> availableMembers;

    // ---------- CONSTRUCTOR

    public MeetingUi() {
        this.topic = "";
        this.date = "";
        this.timeStart = "";
        this.place = "";
        this.topicError = null;
        this.dateError = null;
        this.timeStartError = null;
        this.placeError = null;
        this.memberList = new ArrayList<>(Collections.singletonList(new MemberUi("", null, View.INVISIBLE)));
        availableMembers = new ArrayList<>(DummyGenerator.generateEmailAddresses());
    }

    public MeetingUi(@NonNull String topic, @NonNull String date, @NonNull String timeStart,
                     @NonNull String place, @Nullable String topicError, @Nullable String dateError,
                     @Nullable String timeStartError, @Nullable String placeError,
                     @NonNull List<MemberUi> memberList, @NonNull List<String> availableMembers) {
        this.topic = topic;
        this.date = date;
        this.timeStart = timeStart;
        this.place = place;
        this.topicError = topicError;
        this.dateError = dateError;
        this.timeStartError = timeStartError;
        this.placeError = placeError;
        this.memberList = memberList;
        this.availableMembers = availableMembers;
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
    public String getTimeStart() {
        return timeStart;
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
    public String getTimeStartError() {
        return timeStartError;
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
    public List<String> getAvailableMembers() {
        return availableMembers;
    }

    @Override
    public String toString() {
        return "MeetingUi{" +
            "topic='" + topic + '\'' +
            ", date='" + date + '\'' +
            ", timeStart='" + timeStart + '\'' +
            ", place='" + place + '\'' +
            ", errors[='" + topicError + '\'' +
            ", '" + dateError + '\'' +
            ", '" + timeStartError + '\'' +
            ", '" + placeError + '\'' +
            "], memberList=" + memberList.size() +
            ", availableMembers=" + availableMembers.size() +
            '}';
    }
}
