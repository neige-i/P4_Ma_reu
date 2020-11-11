package com.neige_i.mareu.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.neige_i.mareu.Util.removeRequiredError;

/**
 * Store all the information about a meeting.
 */
public class Meeting implements Parcelable {

    // ----------------------------------- PARCELABLE VARIABLES ------------------------------------

    public static final Creator<Meeting> CREATOR = new Creator<Meeting>() {
        @Override
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };

    // ------------------------------------ INSTANCE VARIABLES -------------------------------------

    /**
     * Name of the meeting
     */
    @NonNull
    private String topic = "";
    /**
     * Date when the meeting is held.
     */
    @Nullable
    private LocalDate date;
    /**
     * Time when the meeting begins.
     */
    @Nullable
    private LocalTime startTime;
    /**
     * Time when the meeting finishes.
     */
    @Nullable
    private LocalTime endTime;
    /**
     * Place where the meeting is held.
     */
    @NonNull
    private String place = "";
    /**
     * List of the members of the meeting.
     */
    @NonNull
    private List<Member> memberList = new ArrayList<>();

    /**
     * Id of the String resource for the topic error.
     */
    @StringRes
    private int topicError;
    /**
     * Id of the String resource for the date error.
     */
    @StringRes
    private int dateError;
    /**
     * Id of the String resource for the start time error.
     */
    @StringRes
    private int startTimeError;
    /**
     * Id of the String resource for the end time error.
     */
    @StringRes
    private int endTimeError;
    /**
     * Id of the String resource for the place error.
     */
    @StringRes
    private int placeError;

    /**
     * Start and end times of a meeting which may overlap with this meeting.
     */
    private LocalTime[] samePlaceTimes;

    // ------------------------------- CONSTRUCTOR & GETTERS/SETTERS -------------------------------

    public Meeting() {
    }

    public Meeting(@NonNull String topic, @Nullable LocalDate date, @Nullable LocalTime startTime,
                   @Nullable LocalTime endTime, @NonNull String place, @NonNull List<Member> memberList
    ) {
        this.topic = topic;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.memberList = memberList;
    }

    protected Meeting(Parcel in) {
        topic = in.readString();
        date = (LocalDate) in.readSerializable();
        startTime = (LocalTime) in.readSerializable();
        endTime = (LocalTime) in.readSerializable();
        place = in.readString();
        in.readList(memberList, Member.class.getClassLoader());
        topicError = in.readInt();
        dateError = in.readInt();
        startTimeError = in.readInt();
        endTimeError = in.readInt();
        placeError = in.readInt();
        samePlaceTimes = (LocalTime[]) in.readSerializable();
    }

    @NonNull
    public String getTopic() {
        return topic;
    }

    public void setTopic(@NonNull String topic) {
        this.topic = topic;
        topicError = removeRequiredError(topicError, topic);
    }

    @Nullable
    public LocalDate getDate() {
        return date;
    }

    public void setDate(@Nullable LocalDate date) {
        this.date = date;
        dateError = removeRequiredError(dateError, date);
    }

    @Nullable
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(@Nullable LocalTime startTime) {
        this.startTime = startTime;
        startTimeError = removeRequiredError(startTimeError, startTime);
    }

    @Nullable
    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(@Nullable LocalTime endTime) {
        this.endTime = endTime;
        endTimeError = removeRequiredError(endTimeError, endTime);
    }

    @NonNull
    public String getPlace() {
        return place;
    }

    public void setPlace(@NonNull String place) {
        this.place = place;
        placeError = removeRequiredError(placeError, place);
    }

    @NonNull
    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(@NonNull List<Member> memberList) {
        this.memberList = memberList;
    }

    @StringRes
    public int getTopicError() {
        return topicError;
    }

    public void setTopicError(@StringRes int topicError) {
        this.topicError = topicError;
    }

    @StringRes
    public int getDateError() {
        return dateError;
    }

    public void setDateError(@StringRes int dateError) {
        this.dateError = dateError;
    }

    @StringRes
    public int getStartTimeError() {
        return startTimeError;
    }

    public void setStartTimeError(@StringRes int startTimeError) {
        this.startTimeError = startTimeError;
    }

    @StringRes
    public int getEndTimeError() {
        return endTimeError;
    }

    public void setEndTimeError(@StringRes int endTimeError) {
        this.endTimeError = endTimeError;
    }

    @StringRes
    public int getPlaceError() {
        return placeError;
    }

    public void setPlaceError(@StringRes int placeError) {
        this.placeError = placeError;
    }

    public void setPlaceError(@StringRes int placeError, LocalTime... samePlaceTimes) {
        this.placeError = placeError;
        this.samePlaceTimes = samePlaceTimes;
    }

    public LocalTime[] getSamePlaceTimes() {
        return samePlaceTimes;
    }

    public List<String> getEmails() {
        return memberList.stream().map(Member::getEmail).collect(Collectors.toList());
    }

    // ------------------------------------ PARCELABLE METHODS -------------------------------------

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(topic);
        dest.writeSerializable(date);
        dest.writeSerializable(startTime);
        dest.writeSerializable(endTime);
        dest.writeString(place);
        dest.writeList(memberList);
        dest.writeInt(topicError);
        dest.writeInt(dateError);
        dest.writeInt(startTimeError);
        dest.writeInt(endTimeError);
        dest.writeInt(placeError);
        dest.writeSerializable(samePlaceTimes);
    }

    // -------------------------------------- OBJECT METHODS ---------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return topicError == meeting.topicError &&
            dateError == meeting.dateError &&
            startTimeError == meeting.startTimeError &&
            endTimeError == meeting.endTimeError &&
            placeError == meeting.placeError &&
            topic.equals(meeting.topic) &&
            Objects.equals(date, meeting.date) &&
            Objects.equals(startTime, meeting.startTime) &&
            Objects.equals(endTime, meeting.endTime) &&
            place.equals(meeting.place) &&
            memberList.equals(meeting.memberList) &&
            Arrays.equals(samePlaceTimes, meeting.samePlaceTimes);
    }

    @NonNull
    @Override
    public String toString() {
        return "Meeting{" +
            "topic='" + topic + '\'' +
            ", date=" + date +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", place='" + place + '\'' +
            ", memberList=" + memberList +
            '}';
    }
}
