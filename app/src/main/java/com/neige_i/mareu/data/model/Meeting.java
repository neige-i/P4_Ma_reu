package com.neige_i.mareu.data.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Meeting {

    // -------------------------------------- CLASS VARIABLES --------------------------------------

    private static int meetingId = 0;

    // ------------------------------------ INSTANCE VARIABLES -------------------------------------

    private final int id;
    @NonNull
    private String topic;
    @Nullable
    private LocalDate date;
    @Nullable
    private LocalTime startTime;
    @Nullable
    private LocalTime endTime;
    @NonNull
    private String place;
    @NonNull
    private List<String> memberList;

    private int memberIndex = -1;

    // ----------------------------------- CONSTRUCTOR & GETTERS -----------------------------------


    public Meeting() {
//        Log.d("Neige", "Meeting::constructor first");
        this("", null, null, null, "", new ArrayList<>());
//        topic = "";
//        date = null;
//        startTime = null;
//        endTime = null;
//        place = "";
//        memberList = new ArrayList<>(/*Collections.singletonList("")*/);
    }

//    public Meeting(int id, @NonNull String topic, @NonNull LocalDate date, @NonNull LocalTime startTime, @NonNull LocalTime endTime,
//                   @NonNull String place, @NonNull List<String> memberList
//    ) {
//        this.id = id;
//        this.topic = topic;
//        this.date = date;
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.place = place;
//        this.memberList = memberList;
//    }

    public Meeting(@NonNull String topic, @Nullable LocalDate date, @Nullable LocalTime startTime,
                   @Nullable LocalTime endTime, @NonNull String place, @NonNull List<String> memberList
    ) {
        this.id = meetingId++;
        this.topic = topic;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.memberList = memberList;
        Log.d("Neige", "Meeting::constructor bis");

        resetMemberIndex();
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getTopic() {
        return topic;
    }

    @Nullable
    public LocalDate getDate() {
        return date;
    }

    @Nullable
    public LocalTime getStartTime() {
        return startTime;
    }

    @Nullable
    public LocalTime getEndTime() {
        return endTime;
    }

    @NonNull
    public String getPlace() {
        return place;
    }

    @NonNull
    public List<String> getMemberList() {
        return memberList;
    }

    public void setTopic(@NonNull String topic) {
        this.topic = topic;
        resetMemberIndex();
    }

    public void setDate(@Nullable LocalDate date) {
        this.date = date;
        resetMemberIndex();
    }

    public void setStartTime(@Nullable LocalTime startTime) {
        this.startTime = startTime;
        resetMemberIndex();
    }

    public void setEndTime(@Nullable LocalTime endTime) {
        this.endTime = endTime;
        resetMemberIndex();
    }

    public void setPlace(@NonNull String place) {
        this.place = place;
        resetMemberIndex();
    }

    public void setMemberList(@NonNull List<String> memberList) {
        this.memberList = memberList;
    }

    public int getMemberIndex() {
        return memberIndex;
    }

    public void setMemberIndex(int memberIndex) {
        this.memberIndex = memberIndex;
    }

    private void resetMemberIndex() {
        memberIndex = -1;
    }

    // --------------------------------- OVERRIDDEN OBJECT METHODS ---------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Meeting meeting = (Meeting) o;
        return id == meeting.id &&
            topic.equals(meeting.topic) &&
            Objects.equals(date, meeting.date) &&
            Objects.equals(startTime, meeting.startTime) &&
            Objects.equals(endTime, meeting.endTime) &&
            place.equals(meeting.place) &&
            memberList.equals(meeting.memberList);
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        final Meeting meeting = (Meeting) o;
//        return id == meeting.id &&
//            topic.equals(meeting.topic) &&
//            place.equals(meeting.place) &&
//            startTime.equals(meeting.startTime) &&
//            endTime.equals(meeting.endTime) &&
//            memberList.equals(meeting.memberList);
//    }

    @NonNull
    @Override
    public String toString() {
        return "Meeting{" +
            "id=" + id +
            ", topic='" + topic + '\'' +
            ", date=" + date +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", place='" + place + '\'' +
            ", memberList=" + memberList +
            '}';
    }
}
