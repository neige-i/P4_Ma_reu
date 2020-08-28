package com.neige_i.mareu.view.list;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.neige_i.mareu.data.MeetingRepository;
import com.neige_i.mareu.data.model.Meeting;

import java.util.List;

public class ListViewModel extends ViewModel {

    private final MeetingRepository meetingRepository;
    private final MutableLiveData<Integer> textViewVisibility = new MutableLiveData<>(View.VISIBLE);

    public ListViewModel(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public LiveData<List<Meeting>> getMeetings() {
        return meetingRepository.getAllMeetings();
    }

    // ------------
    // TextView visibility
    // ------------

    public LiveData<Integer> getTextViewVisibility() {
        return textViewVisibility;
    }

    public void setTextViewVisibility(boolean isVisible) {
        textViewVisibility.setValue(isVisible ? View.VISIBLE : View.GONE);
    }
}
