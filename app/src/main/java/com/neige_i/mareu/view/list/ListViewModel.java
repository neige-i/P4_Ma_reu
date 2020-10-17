package com.neige_i.mareu.view.list;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.data.MeetingRepository;
import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.view.util.SingleLiveEvent;

import java.util.List;

public class ListViewModel extends ViewModel {

    // ----------------------------------------- VARIABLES -----------------------------------------

    // ---------- INSTANCE VARIABLE
    @NonNull
    private final MeetingRepository meetingRepository;
    // ---------- STATE LIVE DATA
    @NonNull
    private final MediatorLiveData<Integer> textVisibilityState = new MediatorLiveData<>();

    @NonNull
    private final SingleLiveEvent<Boolean> dropDownVisibility = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<Void> showDialogEvent = new SingleLiveEvent<>();

    // ----------------------------------- CONSTRUCTOR & GETTERS -----------------------------------

    public ListViewModel(@NonNull MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
        setTextVisibility();
    }

//    @NonNull
//    public LiveData<ListUiModel> getMeetings() {
//        return Transformations.map(meetingRepository.getAllMeetings(), input -> new ListUiModel(
//            input.isEmpty(),
//            input
//        ));
//    }


    @NonNull
    public LiveData<List<Meeting>> getMeetings() {
        return meetingRepository.getAllMeetings();
    }

    @NonNull
    public LiveData<Integer> getTextVisibilityState() {
        return textVisibilityState;
    }

    @NonNull
    public LiveData<Boolean> getDropDownVisibility() {
        return dropDownVisibility;
    }

    @NonNull
    public LiveData<Void> getShowDialogEvent() {
        return showDialogEvent;
    }

    // ------------------------------------------ METHODS ------------------------------------------

    // ---------- RECYCLER VIEW
    public void onRemoveMeeting(int meetingId) {
        meetingRepository.deleteMeeting(meetingId);
    }

    // ---------- TEXT VIEW
    private void setTextVisibility() {

        textVisibilityState.addSource(meetingRepository.getAllMeetings(), meetings -> {
//            Log.d("Neige", "ListViewModel::setTextViewVisibility");
//            textViewVisibility.setValue(meetings.isEmpty() ? View.VISIBLE : View.GONE);

            // ASKME: brain rack
            final Integer visibility = textVisibilityState.getValue();
            final String logMessage;
            if (visibility == null) {
                logMessage = "null";
                this.textVisibilityState.setValue(meetings.isEmpty() ? View.VISIBLE : View.GONE);
            } else if (meetings.isEmpty() && this.textVisibilityState.getValue() == View.GONE) {
                logMessage = "from non-empty to empty";
                this.textVisibilityState.setValue(View.VISIBLE);
            } else if (!meetings.isEmpty() && this.textVisibilityState.getValue() == View.VISIBLE) {
                logMessage = "from empty to non-empty";
                this.textVisibilityState.setValue(View.GONE);
            } else {
                // ASKME: should test this case as well
                logMessage = "do not change visibility";
            }
            Log.d("Neige", "ListViewModel::setTextViewVisibility: " + logMessage);
        });
    }

    public void onFilterClicked(boolean isDropDownVisible) {
        final Boolean oldValue = dropDownVisibility.getValue();
        dropDownVisibility.setValue(oldValue == null || !oldValue); // Toggle value if non-null
    }

    public void onGenerateDummyList() {
        for (Meeting meeting : DummyGenerator.generateMeetings())
            meetingRepository.addMeeting(meeting);
    }
}
