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
    public void addMeeting(Meeting meetingToAdd) {
        if (meetingList.getValue() != null) {
            meetingList.getValue().add(meetingToAdd);
            meetingList.setValue(meetingList.getValue());
        }
    }

    @Override
    public void deleteMeeting(int meetingId) {
        if (meetingList.getValue() != null) {
            for (Meeting meeting : meetingList.getValue()) {
                if (meeting.getId() == meetingId) {
                    meetingList.getValue().remove(meeting);
                    break;
                }
            }
            meetingList.setValue(meetingList.getValue());
        }
    }
}
