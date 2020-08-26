package com.neige_i.mareu.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.neige_i.mareu.data.model.Meeting;

import java.util.ArrayList;
import java.util.List;

public class MeetingRepositoryImpl implements MeetingRepository {

    private final MutableLiveData<List<Meeting>> meetingList = new MutableLiveData<>(new ArrayList<>());

    @Override
    public LiveData<List<Meeting>> getAllMeetings() {
        return meetingList;
    }

    @Override
    public void addMeeting(Meeting meetingToAdd) {
        List<Meeting> meetings = meetingList.getValue();
        meetings.add(meetingToAdd);
        meetingList.setValue(meetings);
    }

    @Override
    public void deleteMeeting(int meetingId) {
        List<Meeting> meetings = meetingList.getValue();
        for (Meeting meeting : meetings) {
            if (meeting.getId() == meetingId) {
                meetings.remove(meeting);
                break;
            }
        }
        meetingList.setValue(meetings);
    }
}
