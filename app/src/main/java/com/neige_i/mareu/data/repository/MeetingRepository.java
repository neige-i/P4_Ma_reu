package com.neige_i.mareu.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.neige_i.mareu.data.model.Meeting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Handles all the operations about the meeting to add.
 */
public interface MeetingRepository {

    // -------------------------------------- GENERAL METHODS --------------------------------------

    void initRepository();

    // -------------------------------------- MEETING METHODS --------------------------------------

    @NonNull
    LiveData<Meeting> getMeeting();

    void setTopic(@NonNull String topic);

    void setDate(@NonNull LocalDate date);

    void setStartTime(@NonNull LocalTime startTime);

    void setEndTime(@NonNull LocalTime endTime);

    void setPlace(@NonNull String place);

    void addMember(int position);

    void updateMember(int position, @NonNull String member);

    void removeMember(int position);

    void triggerRequiredErrors();

    // --------------------------------- AVAILABLE MEMBERS METHODS ---------------------------------

    @NonNull
    List<String> getAvailableMembers();
}
