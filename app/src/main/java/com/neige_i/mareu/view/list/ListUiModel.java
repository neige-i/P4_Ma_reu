package com.neige_i.mareu.view.list;

import android.view.View;

import androidx.annotation.NonNull;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.model.Meeting;

import java.util.ArrayList;
import java.util.List;

public class ListUiModel {

    @NonNull
    private final List<Meeting> meetingList;
    @NonNull
    private final String startDate;
    @NonNull
    private final String endDate;
    @NonNull
    private final String startTime;
    @NonNull
    private final String endTime;
    private final int drawerState;

    public ListUiModel() {
        meetingList = new ArrayList<>();
        startDate = "";
        endDate = "";
        startTime = "";
        endTime = "";
        drawerState = R.id.start;
    }

    public ListUiModel(@NonNull List<Meeting> meetingList, @NonNull String startDate,
                       @NonNull String endDate, @NonNull String startTime,
                       @NonNull String endTime, int drawerState
    ) {
        this.meetingList = meetingList;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.drawerState = drawerState;
    }

    @NonNull
    public List<Meeting> getMeetingList() {
        return meetingList;
    }

    @NonNull
    public String getStartDate() {
        return startDate;
    }

    @NonNull
    public String getEndDate() {
        return endDate;
    }

    @NonNull
    public String getStartTime() {
        return startTime;
    }

    @NonNull
    public String getEndTime() {
        return endTime;
    }

    public int getDrawerState() {
        return drawerState;
    }

    public int getTextViewVisibility() {
        return meetingList.isEmpty() ? View.VISIBLE : View.GONE;
    }

    public static class Builder {

        private List<Meeting> meetingList;
        private String startDate;
        private String endDate;
        private String startTime;
        private String endTime;
        private int drawerState;

        public Builder(@NonNull ListUiModel listUiModel) {
            meetingList = listUiModel.meetingList;
            startDate = listUiModel.startDate;
            endDate = listUiModel.endDate;
            startTime = listUiModel.startTime;
            endTime = listUiModel.endTime;
            drawerState = listUiModel.drawerState;
        }

        public Builder setMeetingList(@NonNull List<Meeting> meetingList) {
            this.meetingList = meetingList;
            return this;
        }

        public Builder setStartDate(@NonNull String startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setEndDate(@NonNull String endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder setStartTime(@NonNull String startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(@NonNull String endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder toggleDrawerState() {
            drawerState = drawerState == R.id.start ? R.id.end : R.id.start;
            return this;
        }

        @NonNull
        public ListUiModel build() {
            return new ListUiModel(meetingList, startDate, endDate, startTime, endTime, drawerState);
        }
    }
}
