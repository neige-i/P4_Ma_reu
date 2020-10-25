package com.neige_i.mareu.data.model;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.List;

public class Meeting {

    // -------------------------------------- CLASS VARIABLES --------------------------------------

    private static int meetingId = 0;

    // ------------------------------------ INSTANCE VARIABLES -------------------------------------

    private final int id;
    @NonNull
    private final String topic;
    @NonNull
    private final String place;
    @NonNull
    private final LocalDateTime startDateTime;
    @NonNull
    private final LocalDateTime endDateTime;
    @NonNull
    private final List<String> emailList;

    // ----------------------------------- CONSTRUCTOR & GETTERS -----------------------------------

    public Meeting(int id, @NonNull String topic, @NonNull String place, @NonNull LocalDateTime startDateTime,
                   @NonNull LocalDateTime endDateTime, @NonNull List<String> emailList
    ) {
        this.id = id;
        this.topic = topic;
        this.place = place;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.emailList = emailList;
    }

    public Meeting(@NonNull String topic, @NonNull String place, @NonNull LocalDateTime startDateTime,
                   @NonNull LocalDateTime endDateTime, @NonNull List<String> emailList
    ) {
        this(meetingId++, topic, place, startDateTime, endDateTime, emailList);
    }

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
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    @NonNull
    public List<String> getEmailList() {
        return emailList;
    }

    // --------------------------------- OVERRIDDEN OBJECT METHODS ---------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Meeting meeting = (Meeting) o;
        return id == meeting.id &&
            topic.equals(meeting.topic) &&
            place.equals(meeting.place) &&
            startDateTime.equals(meeting.startDateTime) &&
            endDateTime.equals(meeting.endDateTime) &&
            emailList.equals(meeting.emailList);
    }

    @NonNull
    @Override
    public String toString() {
        return "Meeting{" +
            "id=" + id +
            ", topic='" + topic + '\'' +
            ", place='" + place + '\'' +
            ", startDateTime=" + startDateTime +
            ", endDateTime=" + endDateTime +
            ", emailList=" + emailList +
            '}';
    }
}
