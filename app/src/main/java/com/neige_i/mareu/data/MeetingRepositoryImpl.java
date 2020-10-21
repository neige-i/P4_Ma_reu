package com.neige_i.mareu.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.neige_i.mareu.data.model.Filters;
import com.neige_i.mareu.data.model.Meeting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeetingRepositoryImpl implements MeetingRepository {

    @NonNull
    private final MutableLiveData<List<Meeting>> meetingList = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Filters> filters = new MutableLiveData<>();

    public MeetingRepositoryImpl() {
        reset();
    }

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
    public void addMeetingList(@NonNull List<Meeting> meetingsToAdd) {
        final List<Meeting> meetings = new ArrayList<>(meetingList.getValue());
        meetings.addAll(meetingsToAdd);
        meetingList.setValue(meetings);
    }

    @Override
    public void deleteMeeting(int meetingId) {
        final List<Meeting> meetings = new ArrayList<>(meetingList.getValue());
        meetings.removeIf(meeting -> meeting.getId() == meetingId);
        meetingList.setValue(meetings);
    }

    @NonNull
    @Override
    public LiveData<List<Meeting>> getFilteredList() {
        return Transformations.switchMap(
            meetingList,
            meetingList -> Transformations.map(filters, filters -> {
                // Return a new list to keep the original one unchanged
                final List<Meeting> filteredMeetings = new ArrayList<>(meetingList);
                if (filters.getFromDate() != null) {
                    filteredMeetings.removeIf(meeting -> meeting.getEndDateTime().toLocalDate().isBefore(
                        filters.getFromDate()));
                }
                if (filters.getUntilDate() != null) {
                    filteredMeetings.removeIf(meeting -> meeting.getStartDateTime().toLocalDate().isAfter(
                        filters.getUntilDate()));
                }
                if (filters.getFromTime() != null) {
                    filteredMeetings.removeIf(meeting -> meeting.getEndDateTime().toLocalTime().isBefore(
                        filters.getFromTime()));
                }
                if (filters.getUntilTime() != null) {
                    filteredMeetings.removeIf(meeting -> meeting.getStartDateTime().toLocalTime().isBefore(
                        filters.getUntilTime()));
                }
                if (!filters.getPlaces().isEmpty()) { // ASKME: removing this condition won't throw Exception
                    filteredMeetings.removeIf(meeting -> !filters.getPlaces().contains(meeting.getPlace()));
                }
                if (!filters.getMembers().isEmpty()) {
                    final StringBuilder builder = new StringBuilder("\n");
                    for (Meeting meeting : filteredMeetings) {
                        builder.append(meeting.getEmailList()).append("\n");
                    }
                    filteredMeetings.removeIf(meeting -> Collections.disjoint(
                        meeting.getEmailList(),
                        filters.getMembers()
                    ));
                }
                return filteredMeetings;
            })
        );
    }

    @Override
    public void setFrom(@NonNull LocalDate fromDate) {
        filters.setValue(new Filters.Builder(filters.getValue()).setFromDate(fromDate).build());
    }

    @Override
    public void setUntil(@NonNull LocalDate untilDate) {
        filters.setValue(new Filters.Builder(filters.getValue()).setUntilDate(untilDate).build());
    }

    @Override
    public void setFrom(@NonNull LocalTime fromTime) {
        filters.setValue(new Filters.Builder(filters.getValue()).setFromTime(fromTime).build());
    }

    @Override
    public void setUntil(@NonNull LocalTime untilTime) {
        filters.setValue(new Filters.Builder(filters.getValue()).setUntilTime(untilTime).build());
    }

    @Override
    public void addPlace(@NonNull String place) {
        final List<String> places = new ArrayList<>(filters.getValue().getPlaces());
        places.add(place);
        filters.setValue(new Filters.Builder(filters.getValue()).setPlaces(places).build());
    }

    @Override
    public void removePlace(@NonNull String place) {
        final List<String> places = new ArrayList<>(filters.getValue().getPlaces());
        places.remove(place);
        filters.setValue(new Filters.Builder(filters.getValue()).setPlaces(places).build());
    }

    @Override
    public void addMember(@NonNull String member) {
        final List<String> members = new ArrayList<>(filters.getValue().getMembers());
        members.add(member);
        filters.setValue(new Filters.Builder(filters.getValue()).setMembers(members).build());
    }

    @Override
    public void removeMember(@NonNull String member) {
        final List<String> members = new ArrayList<>(filters.getValue().getMembers());
        members.remove(member);
        filters.setValue(new Filters.Builder(filters.getValue()).setMembers(members).build());
    }

    @Override
    public void reset() {
        meetingList.setValue(new ArrayList<>());
        filters.setValue(new Filters());
    }
}
