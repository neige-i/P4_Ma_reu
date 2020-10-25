package com.neige_i.mareu.view.add;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model UI for {@link com.neige_i.mareu.R.layout#fragment_add fragment_add} layout.
 */
public class MeetingUiModel {

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
    private final List<MemberUiModel> memberList;

    // ASKME: why not just using non-final variable with setters

    // ---------- CONSTRUCTOR

    public MeetingUiModel() {
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
        memberList = new ArrayList<>(Collections.singletonList(new MemberUiModel("", null, View.INVISIBLE)));
    }

    public MeetingUiModel(@NonNull String topic, @NonNull String date, @NonNull String startTime,
                          @NonNull String endTime, @NonNull String place, @Nullable String topicError,
                          @Nullable String dateError, @Nullable String startTimeError,
                          @Nullable String endTimeError, @Nullable String placeError,
                          @NonNull List<MemberUiModel> memberList) {
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
    public List<MemberUiModel> getMemberList() {
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

    public static class Builder {

        private String topic;
        private String date;
        private String startTime;
        private String endTime;
        private String place;
        private String topicError;
        private String dateError;
        private String startTimeError;
        private String endTimeError;
        private String placeError;
        private List<MemberUiModel> memberList;

        public Builder(@NonNull MeetingUiModel meetingUiModel) {
            topic = meetingUiModel.topic;
            date = meetingUiModel.date;
            startTime = meetingUiModel.startTime;
            endTime = meetingUiModel.endTime;
            place = meetingUiModel.place;
            topicError = meetingUiModel.topicError;
            dateError = meetingUiModel.dateError;
            startTimeError = meetingUiModel.startTimeError;
            endTimeError = meetingUiModel.endTimeError;
            placeError = meetingUiModel.placeError;
            memberList = meetingUiModel.memberList;
        }

        public Builder setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public Builder setDate(String date) {
            this.date = date;
            return this;
        }

        public Builder setStartTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder setPlace(String place) {
            this.place = place;
            return this;
        }

        public Builder setTopicError(String topicError) {
            this.topicError = topicError;
            return this;
        }

        public Builder setDateError(String dateError) {
            this.dateError = dateError;
            return this;
        }

        public Builder setStartTimeError(String startTimeError) {
            this.startTimeError = startTimeError;
            return this;
        }

        public Builder setEndTimeError(String endTimeError) {
            this.endTimeError = endTimeError;
            return this;
        }

        public Builder setPlaceError(String placeError) {
            this.placeError = placeError;
            return this;
        }

        public Builder setMemberList(List<MemberUiModel> memberList) {
            this.memberList = memberList;
            return this;
        }

        public MeetingUiModel build() {
            return new MeetingUiModel(topic, date, startTime, endTime, place, topicError, dateError,
                                      startTimeError, endTimeError, placeError, memberList
            );
        }
    }
}