package com.neige_i.mareu.data.model;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.List;

public class Meeting {

    // ---------- CLASS VARIABLE

    private static int meetingId = 0;

    // ---------- INSTANCE VARIABLE

    private final int id;
    @NonNull private final String topic;
    @NonNull private final String place;
    @NonNull private final LocalDateTime startDateTime; // TODO: add endDateTime
    @NonNull private final List<String> emailList;

    // ---------- CONSTRUCTOR

    public Meeting(int id, @NonNull String topic, @NonNull String place,
                   @NonNull LocalDateTime startDateTime,
                   @NonNull List<String> emailList
    ) {
        this.id = id;
        this.topic = topic;
        this.place = place;
        this.startDateTime = startDateTime;
        this.emailList = emailList;
    }

    public Meeting(@NonNull String topic, @NonNull String place, @NonNull LocalDateTime startDateTime, @NonNull List<String> emailList) {
        this(meetingId++, topic, place, startDateTime, emailList);
//        this.id = meetingId++;
//        this.topic = topic;
//        this.place = place;
//        this.startDateTime = startDateTime;
//        this.emailList = emailList;
    }

    // ---------- GETTER

    public int getId() {
        return id;
    }

    @NonNull
    public String getTopic() {
        return topic;
    }

    @NonNull
    public String getPlace() {
        return place;
    }

    @NonNull
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    @NonNull
    public List<String> getEmailList() {
        return emailList;
    }

    // ---------- OVERRIDDEN OBJECT's METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return id == meeting.id &&
                topic.equals(meeting.topic) &&
                place.equals(meeting.place) &&
                startDateTime.equals(meeting.startDateTime) &&
                emailList.equals(meeting.emailList);
    }

    @NonNull
    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", place='" + place + '\'' +
                ", date=" + startDateTime +
                ", memberList=" + emailList +
                '}';
    }
}
