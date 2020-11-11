package com.neige_i.mareu.view.list.ui_model;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.data.model.Meeting;

import static com.neige_i.mareu.Util.formatDateTime;
import static com.neige_i.mareu.Util.getNames;

public class MeetingUiModel {

    @DrawableRes
    private final int image;
    @NonNull
    private final String topic;
    @NonNull
    private final String date;
    @NonNull
    private final String startTime;
    @NonNull
    private final String endTime;
    @NonNull
    private final String members;

    public MeetingUiModel(@NonNull Meeting meeting) {
        final int placeIndex = DummyGenerator.PLACES.indexOf(meeting.getPlace());
        image = DummyGenerator.LOGOS.get(placeIndex);
        topic = meeting.getTopic();
        date = formatDateTime(meeting.getDate());
        startTime = formatDateTime(meeting.getStartTime());
        endTime = formatDateTime(meeting.getEndTime());
        members = getNames(meeting.getEmails()).toString();
    }

    @DrawableRes
    public int getImage() {
        return image;
    }

    @NonNull
    public String getTopic() {
        return topic;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    @NonNull
    public String getStartTime() {
        return startTime;
    }

    @NonNull
    public String getEndTime() {
        return endTime;
    }

    @NonNull
    public String getMembers() {
        return members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingUiModel that = (MeetingUiModel) o;
        return image == that.image &&
            topic.equals(that.topic) &&
            date.equals(that.date) &&
            startTime.equals(that.startTime) &&
            endTime.equals(that.endTime) &&
            members.equals(that.members);
    }
}
