package com.neige_i.mareu.data.repository;

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

import static com.neige_i.mareu.Util.updateLiveData;

public class ListingRepositoryImpl implements ListingRepository {

    // ------------------------------------ INSTANCE VARIABLES -------------------------------------

    /**
     * Represents the list of all the meetings.
     */
    @NonNull
    private final MutableLiveData<List<Meeting>> meetingList = new MutableLiveData<>();
    /**
     * Represents the object that is used to filter the meeting list.
     */
    @NonNull
    private final MutableLiveData<Filters> filters = new MutableLiveData<>();

    // -------------------------------------- GENERAL METHODS --------------------------------------

    @Override
    public void initRepository() {
        meetingList.setValue(new ArrayList<>());
        filters.setValue(new Filters());
    }

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
                // TIPS: return a new list to keep the original one unchanged
                final List<Meeting> filteredMeetings = new ArrayList<>(meetingList);

                if (filters.getFromDate() != null) {
                    filteredMeetings.removeIf(meeting -> meeting.getDate().isBefore(filters.getFromDate()));
                }
                if (filters.getUntilDate() != null) {
                    filteredMeetings.removeIf(meeting -> meeting.getDate().isAfter(filters.getUntilDate()));
                }
                if (filters.getFromTime() != null) {
                    filteredMeetings.removeIf(meeting -> meeting.getEndTime().isBefore(filters.getFromTime()));
                }
                if (filters.getUntilTime() != null) {
                    filteredMeetings.removeIf(meeting -> meeting.getStartTime().isAfter(filters.getUntilTime()));
                }
                if (!filters.getPlaces().isEmpty()) {
                    filteredMeetings.removeIf(meeting -> !filters.getPlaces().contains(meeting.getPlace()));
                }
                if (!filters.getEmails().isEmpty()) {
                    filteredMeetings.removeIf(meeting -> Collections.disjoint(meeting.getEmails(), filters.getEmails()));
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
        meetingList.getValue().add(meetingToAdd);
        updateLiveData(meetingList);
    }

    @Override
    public void addMeetings(@NonNull List<Meeting> meetingsToAdd) {
        meetingList.getValue().addAll(meetingsToAdd);
        updateLiveData(meetingList);
    }

    @Override
    public void removeMeeting(int position) {
        meetingList.getValue().remove(position);
        updateLiveData(meetingList);
    }

    // -------------------------------------- FILTER METHODS ---------------------------------------

    @NonNull
    @Override
    public LiveData<Filters> getFilters() {
        return filters;
    }

    @Override
    public void setFrom(@Nullable LocalDate fromDate) {
        filters.getValue().setFromDate(fromDate);
        updateLiveData(filters);
    }

    @Override
    public void setUntil(@Nullable LocalDate untilDate) {
        filters.getValue().setUntilDate(untilDate);
        updateLiveData(filters);
    }

    @Override
    public void setFrom(@Nullable LocalTime fromTime) {
        filters.getValue().setFromTime(fromTime);
        updateLiveData(filters);
    }

    @Override
    public void setUntil(@Nullable LocalTime untilTime) {
        filters.getValue().setUntilTime(untilTime);
        updateLiveData(filters);
    }

    @Override
    public void addPlace(@NonNull String place) {
        filters.getValue().getPlaces().add(place);
        updateLiveData(filters);
    }

    @Override
    public void removePlace(@NonNull String place) {
        filters.getValue().getPlaces().remove(place);
        updateLiveData(filters);
    }

    @Override
    public void addEmail(@NonNull String email) {
        filters.getValue().getEmails().add(email);
        updateLiveData(filters);
    }

    @Override
    public void removeEmail(@NonNull String email) {
        filters.getValue().getEmails().remove(email);
        updateLiveData(filters);
    }
}
