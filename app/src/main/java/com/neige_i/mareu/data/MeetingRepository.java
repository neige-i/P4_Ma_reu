package com.neige_i.mareu.data;

import androidx.lifecycle.LiveData;

import com.neige_i.mareu.data.model.Meeting;

import java.util.List;

public interface MeetingRepository {

    LiveData<List<Meeting>> getAllMeetings();

    void addMeeting(Meeting meeting);

    void deleteMeeting(int meetingId);
}