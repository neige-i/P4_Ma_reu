package com.neige_i.mareu.view;

import androidx.lifecycle.ViewModel;

import com.neige_i.mareu.data.MeetingRepository;

public class BaseViewModel extends ViewModel {

    private final MeetingRepository meetingRepository;

    public BaseViewModel(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public void resetRepository() {
        meetingRepository.reset();
    }
}
