package com.neige_i.mareu.view.list;

import com.neige_i.mareu.data.model.Meeting;

import java.util.List;

class ListUiModel {
    final boolean isEmptyTextVisible;
    final List<Meeting> meetings;

    public ListUiModel(boolean isEmptyTextVisible,
                       List<Meeting> meetings
    ) {
        this.isEmptyTextVisible = isEmptyTextVisible;
        this.meetings = meetings;
    }

    public boolean isEmptyTextVisible() {
        return isEmptyTextVisible;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }
}
