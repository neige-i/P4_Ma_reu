package com.neige_i.mareu.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.neige_i.mareu.data.model.Meeting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MeetingRepository {

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

//    void removeMember(@NonNull String member);
    void removeMember(int position);

    // --------------------------------- AVAILABLE MEMBERS METHODS ---------------------------------

    @NonNull
    List<String> getAvailableMembers();
}
