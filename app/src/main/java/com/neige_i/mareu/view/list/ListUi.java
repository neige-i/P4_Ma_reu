package com.neige_i.mareu.view.list;

import android.view.View;

import androidx.annotation.NonNull;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.model.Meeting;

import java.util.ArrayList;
import java.util.List;

public class ListUi {

    private final List<Meeting> meetingList;
    private final String startDate;
    private final String endDate;
    private final String startTime;
    private final String endTime;
    private final int drawerState;
    private final boolean isMenuItemVisible;

    public ListUi() {
        meetingList = new ArrayList<>();
        startDate = "";
        endDate = "";
        startTime = "";
        endTime = "";
        drawerState = R.id.start;
        isMenuItemVisible = false;
    }

    public ListUi(List<Meeting> meetingList, String startDate, String endDate, String startTime,
                  String endTime, int drawerState, boolean isMenuItemVisible
    ) {
        this.meetingList = meetingList;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.drawerState = drawerState;
        this.isMenuItemVisible = isMenuItemVisible;
    }

    public List<Meeting> getMeetingList() {
        return meetingList;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getDrawerState() {
        return drawerState;
    }

    public int getTextViewVisibility() {
        return meetingList.isEmpty() ? View.VISIBLE : View.GONE;
    }

    public boolean isMenuItemVisible() {
        return isMenuItemVisible;
    }

    public static class Builder {

        private List<Meeting> meetingList;
        private String startDate;
        private String endDate;
        private String startTime;
        private String endTime;
        private int drawerState;
        private boolean isMenuItemVisible;

        public Builder(@NonNull ListUi listUi) {
            meetingList = listUi.meetingList;
            startDate = listUi.startDate;
            endDate = listUi.endDate;
            startTime = listUi.startTime;
            endTime = listUi.endTime;
            drawerState = listUi.drawerState;
            isMenuItemVisible = listUi.isMenuItemVisible;
        }

        public Builder setMeetingList(List<Meeting> meetingList) {
            this.meetingList = meetingList;
            return this;
        }

        public Builder setStartDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setEndDate(String endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder setStartTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder toggleDrawerState() {
            drawerState = drawerState == R.id.start ? R.id.end : R.id.start;
            return this;
        }

        public Builder setMenuItemVisibility(boolean isMenuItemVisible) {
            this.isMenuItemVisible = isMenuItemVisible;
            return this;
        }

        @NonNull
        public ListUi build() {
            return new ListUi(meetingList, startDate, endDate, startTime, endTime, drawerState, isMenuItemVisible);
        }
    }
}
