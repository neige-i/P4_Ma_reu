package com.neige_i.mareu.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.neige_i.mareu.data.model.Meeting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MeetingRepositoryImpl implements MeetingRepository {

    // ------------------------------------ INSTANCE VARIABLES -------------------------------------

    @NonNull
    private final MutableLiveData<Meeting> meeting = new MutableLiveData<>();
    @NonNull
    private final List<String> availableMembers = new ArrayList<>();

    @Override
    public void initRepository() {
        meeting.setValue(new Meeting());
        initAvailableMembers();
    }

    // -------------------------------------- MEETING METHODS --------------------------------------

    @NonNull
    @Override
    public LiveData<Meeting> getMeeting() {
        return meeting;
    }

    @Override
    public void setTopic(@NonNull String topic) {
        meeting.getValue().setTopic(topic);
        updateLiveData();
    }

    @Override
    public void setDate(@NonNull LocalDate date) {
        meeting.getValue().setDate(date);
        updateLiveData();
    }

    @Override
    public void setStartTime(@NonNull LocalTime startTime) {
        meeting.getValue().setStartTime(startTime);
        updateLiveData();
    }

    @Override
    public void setEndTime(@NonNull LocalTime endTime) {
        meeting.getValue().setEndTime(endTime);
        updateLiveData();
    }

    @Override
    public void setPlace(@NonNull String place) {
        meeting.getValue().setPlace(place);
        updateLiveData();
    }

    @Override
    public void addMember(int position) {
        meeting.getValue().getMemberList().add(position, "");
        meeting.getValue().setMemberIndex(position);
        updateLiveData();
    }

    @Override
    public void updateMember(int position, @NonNull String member) {
        meeting.getValue().getMemberList().set(position, member);
        meeting.getValue().setMemberIndex(position);
        updateLiveData();
        setAvailableMembers();
    }

    @Override
    public void removeMember(int position) {
        meeting.getValue().getMemberList().remove(position);
        meeting.getValue().setMemberIndex(position);
        updateLiveData();
        setAvailableMembers();
    }

    private void updateLiveData() {
        meeting.setValue(meeting.getValue());
    }

    // --------------------------------- AVAILABLE MEMBERS METHODS ---------------------------------

    @NonNull
    public List<String> getAvailableMembers() {
        return availableMembers;
    }

    private void initAvailableMembers() {
        availableMembers.clear();
        availableMembers.addAll(DummyGenerator.EMAILS);
    }

    private void setAvailableMembers() {
        initAvailableMembers();
        availableMembers.removeIf(email -> meeting.getValue().getMemberList().contains(email));
    }
}
