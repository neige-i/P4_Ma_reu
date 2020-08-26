package com.neige_i.mareu.view.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.neige_i.mareu.data.MeetingRepository;
import com.neige_i.mareu.data.model.Meeting;

import java.util.List;

public class ListViewModel extends ViewModel {

    private final MeetingRepository meetingRepository;

    public ListViewModel(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public LiveData<List<Meeting>> getMeetings() {
        return meetingRepository.getAllMeetings();
    }
}
