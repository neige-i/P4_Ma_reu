package com.neige_i.mareu.data.model;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Meeting {

    private static int meetingId = 0;
    private final int id;
    private final String topic;
    private final String place;
    private final Calendar date;
    private final List<String> emailList;

    public Meeting(String topic, String place, Calendar date, List<String> emailList) {
        this.id = meetingId++;
        this.topic = topic;
        this.place = place;
        this.date = date;
        this.emailList = emailList;
    }

    public int getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getPlace() {
        return place;
    }

    public Calendar getDate() {
        return date;
    }

    public List<String> getEmailList() {
        return emailList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return id == meeting.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @NonNull
    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", place='" + place + '\'' +
                ", date=" + date +
                ", memberList=" + emailList +
                '}';
    }
}
