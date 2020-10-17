package com.neige_i.mareu.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.neige_i.mareu.data.model.Meeting;

import java.util.ArrayList;
import java.util.List;

public class MeetingRepositoryImpl implements MeetingRepository {

    @NonNull
    private final MutableLiveData<List<Meeting>> meetingList = new MutableLiveData<>(new ArrayList<>());

    @NonNull
    @Override
    public LiveData<List<Meeting>> getAllMeetings() {
        return meetingList;
    }

    @Override
    public void addMeeting(@NonNull Meeting meetingToAdd) {
        final List<Meeting> meetings = new ArrayList<>(meetingList.getValue());
        meetings.add(meetingToAdd);
        meetingList.setValue(meetings);
    }

    @Override
    public void deleteMeeting(int meetingId) {
        final List<Meeting> meetings = new ArrayList<>(meetingList.getValue());
        // ASKME: use lambda to filter collections in Java
        meetings.removeIf(meeting -> meeting.getId() == meetingId);
        meetingList.setValue(meetings);
    }
}
