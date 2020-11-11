package com.neige_i.mareu.view.add.ui_model;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.data.model.Member;
import com.neige_i.mareu.data.repository.MeetingRepository;
import com.neige_i.mareu.view.add.AddFragment;
import com.neige_i.mareu.view.add.view_model.AddViewModel;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.neige_i.mareu.Util.NO_ERROR;
import static com.neige_i.mareu.Util.formatDateTime;

/**
 * This class maps, inside {@link AddViewModel}, the data from {@link MeetingRepository}
 * into values that will be used in {@link AddFragment}.
 */
public class NewMeetingUiModel {

    // ------------------------------------ INSTANCE VARIABLES -------------------------------------

    @NonNull
    private final String topic;
    @NonNull
    private final String date;
    @NonNull
    private final String startTime;
    @NonNull
    private final String endTime;
    @NonNull
    private final String place;

    @NonNull
    private final List<MemberUiModel> memberList;

    @StringRes
    private final int topicError;
    @StringRes
    private final int dateError;
    @StringRes
    private final int startTimeError;
    @StringRes
    private final int endTimeError;
    @StringRes
    private final int placeError;

    private final LocalTime[] samePlaceTimes;

    // ------------------------------- CONSTRUCTOR & GETTERS/SETTERS -------------------------------

    public NewMeetingUiModel(@NonNull Meeting meeting) {
        this.topic = meeting.getTopic();
        this.date = formatDateTime(meeting.getDate());
        this.startTime = formatDateTime(meeting.getStartTime());
        this.endTime = formatDateTime(meeting.getEndTime());
        this.place = meeting.getPlace();
        this.memberList = new ArrayList<>();
        setMemberList(meeting.getMemberList());
        this.topicError = meeting.getTopicError();
        this.dateError = meeting.getDateError();
        this.startTimeError = meeting.getStartTimeError();
        this.endTimeError = meeting.getEndTimeError();
        this.placeError = meeting.getPlaceError();
        this.samePlaceTimes = meeting.getSamePlaceTimes();
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
    public String getPlace() {
        return place;
    }

    @NonNull
    public List<MemberUiModel> getMemberList() {
        // TIPS: instantiate new objects to correctly update ListAdapter
        final List<MemberUiModel> newMemberList = new ArrayList<>();
        for (MemberUiModel memberUiModel : memberList) {
            newMemberList.add(new MemberUiModel(
                memberUiModel.getId(),
                memberUiModel.getEmail(),
                memberUiModel.getEmailErrorId(),
                memberUiModel.getAddButtonVisibility(),
                memberUiModel.getRemoveButtonVisibility(),
                memberUiModel.getSameMemberTimes()
            ));
        }
        return newMemberList;
    }

    public String getTopicError(@NonNull Application application) {
        return getFieldError(topicError, application);
    }

    public String getDateError(@NonNull Application application) {
        return getFieldError(dateError, application);
    }

    public String getStartTimeError(@NonNull Application application) {
        return getFieldError(startTimeError, application);
    }

    public String getEndTimeError(@NonNull Application application) {
        return getFieldError(endTimeError, application);
    }

    public String getPlaceError(@NonNull Application application) {
        return getFieldError(placeError, application);
    }
    // --------------------------------------- LOCAL METHODS ---------------------------------------

    /**
     * Returns the appropriate error message or null if there isn't any.
     */
    @Nullable
    private String getFieldError(@StringRes int errorMessage, @NonNull Application application) {
        if (errorMessage == NO_ERROR)
            return null;
        else if (errorMessage == R.string.incorrect_start_error)
            return application.getString(errorMessage, endTime);
        else if (errorMessage == R.string.incorrect_end_error)
            return application.getString(errorMessage, startTime);
        else if (errorMessage == R.string.time_place_error)
            return application.getString(errorMessage, samePlaceTimes[0], samePlaceTimes[1]);
        else
            return application.getString(errorMessage);
    }

    private void setMemberList(@NonNull List<Member> memberList) {
        for (int i = 0; i < memberList.size(); i++) {
            // TIPS: change button visibility according to the list size
            int addButtonVisibility = memberList.size() < DummyGenerator.EMAILS.size() ? View.VISIBLE : View.INVISIBLE;
            int removeButtonVisibility = memberList.size() == 1 ? View.INVISIBLE : View.VISIBLE;
            final Member member = memberList.get(i);
            this.memberList.add(new MemberUiModel(
                member.getId(),
                member.getEmail(),
                member.getError(),
                addButtonVisibility,
                removeButtonVisibility,
                member.getSameMemberTimes()
            ));
        }
    }
}
