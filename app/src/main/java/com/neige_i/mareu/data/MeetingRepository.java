package com.neige_i.mareu.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.neige_i.mareu.data.model.Filters;
import com.neige_i.mareu.data.model.Meeting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public interface MeetingRepository {

    // ---------- MEETING LIST

    @NonNull
    LiveData<List<Meeting>> getAllMeetings();

    void addMeeting(@NonNull Meeting meetingToAdd);

    void addMeetingList(@NonNull List<Meeting> meetingsToAdd);

    void deleteMeeting(int meetingId);

    @NonNull
    LiveData<List<Meeting>> getFilteredList();

    // ---------- FILTERS

    void setFrom(@NonNull LocalDate fromDate);

    void setUntil(@NonNull LocalDate untilDate);

    void setFrom(@NonNull LocalTime fromTime);

    void setUntil(@NonNull LocalTime untilTime);

    void addPlace(@NonNull String place);

    void removePlace(@NonNull String place);

    void addMember(@NonNull String member);

    void removeMember(@NonNull String member);

    void reset();
}
