package com.neige_i.mareu.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.neige_i.mareu.data.model.Filters;
import com.neige_i.mareu.data.model.Meeting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Handles all the operations about the meeting list.
 */
public interface ListingRepository {

    // -------------------------------------- GENERAL METHODS --------------------------------------

    void initRepository();

    @NonNull
    LiveData<List<Meeting>> getFilteredList();

    // ----------------------------------- MEETING LIST METHODS ------------------------------------

    @NonNull
    LiveData<List<Meeting>> getAllMeetings();

    void addMeeting(@NonNull Meeting meetingToAdd);

    void addMeetings(@NonNull List<Meeting> meetingsToAdd);

    void removeMeeting(int position);

    // -------------------------------------- FILTER METHODS ---------------------------------------

    @NonNull
    LiveData<Filters> getFilters();

    void setFrom(@Nullable LocalDate fromDate);

    void setUntil(@Nullable LocalDate untilDate);

    void setFrom(@Nullable LocalTime fromTime);

    void setUntil(@Nullable LocalTime untilTime);

    void addPlace(@NonNull String place);

    void removePlace(@NonNull String place);

    void addEmail(@NonNull String email);

    void removeEmail(@NonNull String email);
}
