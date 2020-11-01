package com.neige_i.mareu.view.add;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.view.util.Util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.neige_i.mareu.view.util.Util.NO_ERROR;

/**
 * Model UI for {@link com.neige_i.mareu.R.layout#fragment_add fragment_add} layout.
 */
public class MeetingUiModel {

    // ------------------------------------ INSTANCE VARIABLES -------------------------------------

    @NonNull
    private String topic;
    @NonNull
    private String date;
    @NonNull
    private String startTime;
    @NonNull
    private String endTime;
    @NonNull
    private String place;

    @NonNull
    private List<MemberUiModel> memberList;

    @StringRes
    private int topicError;
    @StringRes
    private int dateError;
    @StringRes
    private int startTimeError;
    @StringRes
    private int endTimeError;
    @StringRes
    private int placeError;

    private final Application application;

    @Nullable
    private LocalTime[] existingTimes;
//    private final int NO_ERROR = 0;
//    private int memberIndex = -1;

    // ------------------------------- CONSTRUCTOR & GETTERS/SETTERS -------------------------------

    public MeetingUiModel(@NonNull Application application) {
        this.application = application;
        topic = "";
        date = "";
        startTime = "";
        endTime = "";
        place = "";
        memberList = new ArrayList<>();
//        memberList = new ArrayList<>(Collections.singletonList(new MemberUiModel("", null, View.INVISIBLE)));
        topicError = 0;
        dateError = 0;
        startTimeError = 0;
        endTimeError = 0;
        placeError = 0;
    }

    public MeetingUiModel(@NonNull String topic, @NonNull String date, @NonNull String startTime,
                          @NonNull String endTime, @NonNull String place, @NonNull List<MemberUiModel> memberList,
                          int topicError, int dateError, int startTimeError, int endTimeError, int placeError,
                          @NonNull Application application
    ) {
        this.application = application;
        this.topic = topic;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.topicError = topicError;
        this.dateError = dateError;
        this.startTimeError = startTimeError;
        this.endTimeError = endTimeError;
        this.placeError = placeError;
        this.memberList = memberList;
    }

    @NonNull
    public String getTopic() {
        return topic;
    }

    public void setTopic(@NonNull String topic) {
        this.topic = topic;
        topicError = setFieldError(topic, topicError);
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@Nullable LocalDate date) {
        this.date = date == null ? "" : date.format(Util.DATE_FORMAT);
        dateError = setFieldError(this.date, dateError);
    }

    @NonNull
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(@Nullable LocalTime startTime) {
        this.startTime = startTime == null ? "" : startTime.format(Util.TIME_FORMAT);
        startTimeError = setFieldError(this.startTime, startTimeError);
    }

    @NonNull
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(@Nullable LocalTime endTime) {
        this.endTime = endTime == null ? "" : endTime.format(Util.TIME_FORMAT);
        endTimeError = setFieldError(this.endTime, endTimeError);
    }

    @NonNull
    public String getPlace() {
        return place;
    }

    public void setPlace(@NonNull String place) {
        this.place = place;
        placeError = setFieldError(place, placeError);
    }

    @NonNull
    public List<MemberUiModel> getMemberList() {
        return memberList;
    }

    public void setMemberList(@NonNull List<MemberUiModel> memberList) {
        this.memberList = memberList;
    }

    public void setMemberList(int memberIndex, @NonNull List<String> dataList) {
        // Caution: must instantiate a new object in order to correctly update the UI

        // Update list (add, update or remove member)
        final List<MemberUiModel> tempMemberList = new ArrayList<>(memberList);
        int indexToUpdate = -1;
        String email = null;
        switch (tempMemberList.size() - dataList.size()) {
            case -1: // Add member
                tempMemberList.add(memberIndex, new MemberUiModel(dataList.get(memberIndex), application));
                break;
            case 0: // Update member
                indexToUpdate = memberIndex;
                if (memberIndex != -1)
                    email = dataList.get(memberIndex);
                break;
            case 1: // Remove member
                tempMemberList.remove(memberIndex);
                break;
        }

        // Set list
        for (int i = 0; i < tempMemberList.size(); i++) {
            final MemberUiModel oldMemberUiModel = tempMemberList.get(i);
            tempMemberList.set(i, new MemberUiModel(
                oldMemberUiModel.getId(),
                i == indexToUpdate ? email : oldMemberUiModel.getEmail(),
                i == indexToUpdate && !email.isEmpty() ? NO_ERROR : oldMemberUiModel.getEmailErrorId(),
                tempMemberList.size() < DummyGenerator.EMAILS.size() ? View.VISIBLE : View.INVISIBLE,
                i == 0 && tempMemberList.size() == 1 ? View.INVISIBLE : View.VISIBLE,
                application
            ));
        }

        memberList = tempMemberList;
    }

    public String getTopicError() {
        return getFieldError(topicError);
    }

    public void setTopicError(int topicError) {
        this.topicError = topicError;
    }

    public String getDateError() {
        return getFieldError(dateError);
    }

    public void setDateError(int dateError) {
        this.dateError = dateError;
    }

    public String getStartTimeError() {
        return getFieldError(startTimeError);
    }

    public int getStartTimeErrorId() {
        return startTimeError;
    }

    public void setStartTimeError(int startTimeError) {
        this.startTimeError = startTimeError;
//        this.startTimeError = setTimeError(startTimeError);
    }

    public String getEndTimeError() {
        return getFieldError(endTimeError);
    }

    public int getEndTimeErrorId() {
        return endTimeError;
    }

    public void setEndTimeError(int endTimeError) {
        this.endTimeError = endTimeError;
//        this.endTimeError = setTimeError(endTimeError);
    }

    private int setTimeError(int timeError) {
        if (timeError == NO_ERROR && existingTimes != null)
            return R.string.occupied_error;
        else
            return timeError;
    }

    public String getPlaceError() {
        return getFieldError(placeError);
    }

    public void setPlaceError(int placeError) {
        Log.d("Neige", "MeetingUiModel::setPlaceError: " + placeError);
        this.placeError = placeError;
    }

    public void setPlaceError(int placeError, LocalTime... localTimes) {
        setPlaceError(placeError);
        existingTimes = localTimes;
    }

    public boolean areFieldsOk() {
        for (MemberUiModel memberUiModel : memberList) {
            if (memberUiModel.getEmailErrorId() != NO_ERROR)
                return false;
        }
        return topicError == NO_ERROR && dateError == NO_ERROR && startTimeError == NO_ERROR &&
            endTimeError == NO_ERROR && placeError == NO_ERROR;
    }

    public boolean isDateTimeSet() {
        return !date.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty();
    }

    public boolean areAllFieldsSet() {
        for (MemberUiModel memberUiModel : memberList) {
            if (memberUiModel.getEmail().isEmpty())
                return false;
        }
        return !topic.trim().isEmpty() && !date.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty() && !place.isEmpty();
    }

    public LocalTime[] getExistingTimes() {
        return existingTimes;
    }

    public void resetExistingTimes() {
        existingTimes = null;
    }

    //    public void setMemberIndex(int memberIndex) {
//        this.memberIndex = memberIndex;
//    }

    // --------------------------------------- LOCAL METHODS ---------------------------------------

    /**
     * Returns the appropriate error message or null if there isn't any.
     */
    @Nullable
    private String getFieldError(@StringRes int errorMessage) {
        switch (errorMessage) {
            case NO_ERROR:
                return null;
            case R.string.start_time_error:
                return application.getString(errorMessage, endTime);
            case R.string.end_time_error:
                return application.getString(errorMessage, startTime);
            case R.string.time_place_error:
                return application.getString(errorMessage, existingTimes);
            default:
                return application.getString(errorMessage);
        }
//        return errorMessage == NO_ERROR ? null : application.getString(errorMessage);
    }

    /**
     * If the specified field has an 'empty' error while being updated with a non-empty value, then remove the error.
     */
    private int setFieldError(@NonNull String field, @StringRes int errorMessage) {
        if (errorMessage == R.string.empty_field_error && !field.isEmpty())
            return NO_ERROR; // Remove error
        else
            return errorMessage; // Stay unchanged
    }

    // --------------------------------- OVERRIDDEN OBJECT METHODS ---------------------------------

    @NonNull
    @Override
    public String toString() {
        return "MeetingUi{" +
            "topic='" + topic + '\'' +
            ", dateTime='" + date + " - " + startTime + " - " + endTime + '\'' +
            ", place='" + place + '\'' +
            ", memberList=" + memberList.size() +
            ", errors[='" + topicError + '\'' +
            ", '" + dateError + '\'' +
            ", '" + startTimeError + '\'' +
            ", '" + endTimeError + '\'' +
            ", '" + placeError + '\'' +
            "]}";
    }
}
