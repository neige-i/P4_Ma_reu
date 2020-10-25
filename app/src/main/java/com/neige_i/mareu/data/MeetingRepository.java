package com.neige_i.mareu.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.neige_i.mareu.data.model.Meeting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MeetingRepository {

    // -------------------------------------- GENERAL METHODS --------------------------------------

    @NonNull
    LiveData<List<Meeting>> getFilteredList();

    // ----------------------------------- MEETING LIST METHODS ------------------------------------

    @NonNull
    LiveData<List<Meeting>> getAllMeetings();

    void addMeeting(@NonNull Meeting meetingToAdd);

    void addDummyList(@NonNull List<Meeting> meetingsToAdd);

    void deleteMeeting(int meetingId);

    // -------------------------------------- FILTER METHODS ---------------------------------------

    void setFrom(@Nullable LocalDate fromDate);

    void setUntil(@Nullable LocalDate untilDate);

    void setFrom(@Nullable LocalTime fromTime);

    void setUntil(@Nullable LocalTime untilTime);

    void addPlace(@NonNull String place);

    void removePlace(@NonNull String place);

    void addMember(@NonNull String member);

    void removeMember(@NonNull String member);

    // --------------------------------- AVAILABLE MEMBERS METHODS ---------------------------------

    @NonNull
    List<String> getAvailableMembers();

    void addAvailableMembers(@NonNull String memberEmail);

    void removeAvailableMembers(@NonNull String memberEmail);

    void resetAvailableMembers();
}
