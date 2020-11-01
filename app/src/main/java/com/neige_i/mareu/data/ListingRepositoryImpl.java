package com.neige_i.mareu.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class ListingRepositoryImpl implements ListingRepository {

    // ------------------------------------ INSTANCE VARIABLES -------------------------------------

    @NonNull
    private final MutableLiveData<List<Meeting>> meetingList = new MutableLiveData<>(new ArrayList<>());
    @NonNull
    private final MutableLiveData<Filters> filters = new MutableLiveData<>(new Filters());

    // -------------------------------------- GENERAL METHODS --------------------------------------

    /**
     * Returns a filtered meeting list.<br />
     * Keep tracking the last updated value of {@link #meetingList} with
     * {@link Transformations#switchMap switchMap()} and of {@link #filters} with
     * {@link Transformations#map map()}.<br />
     * Then, for each filter, remove the meeting from the list if the condition is met.
     */
    @NonNull
    @Override
    public LiveData<List<Meeting>> getFilteredList() {
        return Transformations.switchMap(
            meetingList,
            meetingList -> Transformations.map(filters, filters -> {
                // Return a new list to keep the original one unchanged
                final List<Meeting> filteredMeetings = new ArrayList<>(meetingList);

//                if (filters.getFromDate() != null) {
//                    filteredMeetings.removeIf(meeting -> meeting.getEndTime().toLocalDate().isBefore(
//                        filters.getFromDate()));
//                }
//                if (filters.getUntilDate() != null) {
//                    filteredMeetings.removeIf(meeting -> meeting.getStartTime().toLocalDate().isAfter(
//                        filters.getUntilDate()));
//                }
//                if (filters.getFromTime() != null) {
//                    filteredMeetings.removeIf(meeting -> meeting.getEndTime().toLocalTime().isBefore(
//                        filters.getFromTime()));
//                }
//                if (filters.getUntilTime() != null) {
//                    filteredMeetings.removeIf(meeting -> meeting.getStartTime().toLocalTime().isAfter(
//                        filters.getUntilTime()));
//                }
                if (!filters.getPlaces().isEmpty()) {
                    filteredMeetings.removeIf(meeting -> !filters.getPlaces().contains(meeting.getPlace()));
                }
                if (!filters.getMembers().isEmpty()) {
                    filteredMeetings.removeIf(meeting -> Collections.disjoint(
                        meeting.getMemberList(),
                        filters.getMembers()
                    ));
                }
                return filteredMeetings;
            })
        );
    }

    // ----------------------------------- MEETING LIST METHODS ------------------------------------

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
    public void addMeetings(@NonNull List<Meeting> meetingsToAdd) {
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

    // -------------------------------------- FILTER METHODS ---------------------------------------

    @Override
    public void setFrom(@Nullable LocalDate fromDate) {
        filters.setValue(new Filters.Builder(filters.getValue()).setFromDate(fromDate).build());
    }

    @Override
    public void setUntil(@Nullable LocalDate untilDate) {
        filters.setValue(new Filters.Builder(filters.getValue()).setUntilDate(untilDate).build());
    }

    @Override
    public void setFrom(@Nullable LocalTime fromTime) {
        filters.setValue(new Filters.Builder(filters.getValue()).setFromTime(fromTime).build());
    }

    @Override
    public void setUntil(@Nullable LocalTime untilTime) {
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
}
