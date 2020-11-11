package com.neige_i.mareu.view.list.ui_model;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.data.repository.ListingRepository;
import com.neige_i.mareu.view.list.ListFragment;
import com.neige_i.mareu.view.list.view_model.ListViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.neige_i.mareu.Util.formatDateTime;

/**
 * This class maps, inside {@link ListViewModel}, the data from {@link ListingRepository}
 * into values that will be used in {@link ListFragment}.
 */
public class ListUiModel {

    @NonNull
    private final List<MeetingUiModel> meetingList = new ArrayList<>();
    @NonNull
    private final String fromDate;
    @NonNull
    private final String untilDate;
    @NonNull
    private final String fromTime;
    @NonNull
    private final String untilTime;
    private final int textVisibility;

    // ----------------------------------- CONSTRUCTOR & GETTERS -----------------------------------

    public ListUiModel(@NonNull List<Meeting> meetingList, @Nullable LocalDate fromDate, @Nullable LocalDate untilDate,
                       @Nullable LocalTime fromTime, @Nullable LocalTime untilTime) {
        for (Meeting meeting : meetingList)
            this.meetingList.add(new MeetingUiModel(meeting));
        this.fromDate = formatDateTime(fromDate);
        this.untilDate = formatDateTime(untilDate);
        this.fromTime = formatDateTime(fromTime);
        this.untilTime = formatDateTime(untilTime);
        textVisibility = meetingList.isEmpty() ? View.VISIBLE : View.GONE;
    }

    @NonNull
    public List<MeetingUiModel> getMeetingList() {
        return meetingList;
    }

    @NonNull
    public String getFromDate() {
        return fromDate;
    }

    @NonNull
    public String getUntilDate() {
        return untilDate;
    }

    @NonNull
    public String getFromTime() {
        return fromTime;
    }

    @NonNull
    public String getUntilTime() {
        return untilTime;
    }

    public int getTextVisibility() {
        return textVisibility;
    }
}
