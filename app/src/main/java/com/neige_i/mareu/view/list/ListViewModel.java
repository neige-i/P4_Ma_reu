package com.neige_i.mareu.view.list;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.data.MeetingRepository;
import com.neige_i.mareu.view.util.SingleLiveEvent;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.neige_i.mareu.view.util.Util.DATE_FORMAT;
import static com.neige_i.mareu.view.util.Util.TIME_FORMAT;

public class ListViewModel extends ViewModel {

    // ----------------------------------------- VARIABLES -----------------------------------------

    // ---------- INSTANCE VARIABLE
    @NonNull
    private final MeetingRepository meetingRepository;
    // ---------- STATE LIVE DATA

    @NonNull
    private final MutableLiveData<ListUi> listUi = new MutableLiveData<>(new ListUi());

    @NonNull
    private final SingleLiveEvent<LocalDate> datePicker = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<LocalTime> timePicker = new SingleLiveEvent<>();

    @NonNull
    private final Clock clock;

    private boolean isStartDate;
    private boolean isStartTime;

    // ----------------------------------- CONSTRUCTOR & GETTERS -----------------------------------

    public ListViewModel(@NonNull MeetingRepository meetingRepository, @NonNull Clock clock) {
        this.meetingRepository = meetingRepository;
        this.clock = clock;
    }

    public LiveData<ListUi> getListUi() {
        return Transformations.switchMap(
            meetingRepository.getFilteredList(),
            meetingList -> Transformations.map(
                listUi,
                ui -> new ListUi.Builder(listUi.getValue())
                    .setMeetingList(meetingList)
                    .build()
            )
        );
    }

    @NonNull
    public LiveData<LocalDate> getDatePicker() {
        return datePicker;
    }

    @NonNull
    public LiveData<LocalTime> getTimePicker() {
        return timePicker;
    }

    // ------------------------------------------ METHODS ------------------------------------------

    // ---------- RECYCLER VIEW
    public void onRemoveMeeting(int meetingId) {
        meetingRepository.deleteMeeting(meetingId);
    }

    public void onGenerateDummyList() {
        meetingRepository.addMeetingList(DummyGenerator.generateMeetings());
    }

    // TODO: duplicate code

    // ---------- DATE METHOD

    /**
     * Called when the user clicks to choose the date of the meeting.
     */
    public void onDateClicked(@IdRes int dateInputId) {
        isStartDate = dateInputId == R.id.start_date_filter_input;

        final LocalDate localDate = isStartDate ? getStartDate() : getEndDate();
        datePicker.setValue(localDate == null ? LocalDate.now(clock) : localDate);
    }

    @Nullable
    private LocalDate getStartDate() {
        final String startDate = listUi.getValue().getStartDate();
        return startDate.isEmpty() ? null : LocalDate.parse(startDate, DATE_FORMAT);
    }

    @Nullable
    private LocalDate getEndDate() {
        final String EndDate = listUi.getValue().getEndDate();
        return EndDate.isEmpty() ? null : LocalDate.parse(EndDate, DATE_FORMAT);
    }

    /**
     * Called when the user validates the date of the meeting.
     */
    public void onDateChanged(int year, int month, int dayOfMonth) {
        final String dateTime = LocalDate.of(year, month, dayOfMonth).format(DATE_FORMAT);
        final ListUi.Builder builder = new ListUi.Builder(listUi.getValue());
        if (isStartDate)
            builder.setStartDate(dateTime);
        else
            builder.setEndDate(dateTime);

        listUi.setValue(builder.build());

        if (isStartDate) {
            meetingRepository.setFrom(getStartDate());
        } else {
            meetingRepository.setUntil(getEndDate());
        }
    }

    // ---------- TIME METHOD

    /**
     * Called when the user clicks to choose the start time or end time of the meeting.
     */
    public void onTimeClicked(@IdRes int timeInputId) {
        isStartTime = timeInputId == R.id.start_time_filter_input;

        final LocalTime localTime = isStartTime ? getStartTime() : getEndTime();
        timePicker.setValue(localTime == null ? LocalTime.now(clock) : localTime);
    }

    /**
     * Returns the start time of the meeting or null if not set yet.
     */
    @Nullable
    private LocalTime getStartTime() {
        final String startTime = listUi.getValue().getStartTime();
        return startTime.isEmpty() ? null : LocalTime.parse(startTime, TIME_FORMAT);
    }

    /**
     * Returns the end time of the meeting or null if not set yet.
     */
    @Nullable
    private LocalTime getEndTime() {
        final String endTime = listUi.getValue().getEndTime();
        return endTime.isEmpty() ? null : LocalTime.parse(endTime, TIME_FORMAT);
    }

    /**
     * Called when the user validates the start time or end time of the meeting.
     */
    public void onTimeChanged(int hour, int minute) {
        final String stringTime = LocalTime.of(hour, minute).format(TIME_FORMAT);
        final ListUi.Builder builder = new ListUi.Builder(listUi.getValue());
        if (isStartTime)
            builder.setStartTime(stringTime);
        else
            builder.setEndTime(stringTime);
        listUi.setValue(builder.build());

        if (isStartTime) {
            meetingRepository.setFrom(getStartTime());
        } else {
            meetingRepository.setUntil(getEndTime());
        }
    }

    public void onFieldCleared(@IdRes int inputId) {
        final ListUi.Builder builder = new ListUi.Builder(listUi.getValue());
        switch (inputId) {
            case R.id.start_date_filter_layout:
                builder.setStartDate("");
                meetingRepository.setFrom((LocalDate) null);
                break;
            case R.id.end_date_filter_layout:
                builder.setEndDate("");
                meetingRepository.setUntil((LocalDate) null);
                break;
            case R.id.start_time_filter_layout:
                builder.setStartTime("");
                meetingRepository.setFrom((LocalTime) null);
                break;
            case R.id.end_time_filter_layout:
                builder.setEndTime("");
                meetingRepository.setUntil((LocalTime) null);
                break;
        }
        listUi.setValue(builder.build());
    }

    public void onFilterClicked() {
        listUi.setValue(new ListUi.Builder(listUi.getValue()).toggleDrawerState().build());
    }

    public void onPlaceFilterChecked(@NonNull String place, boolean isChecked) {
        if (isChecked)
            meetingRepository.addPlace(place);
        else
            meetingRepository.removePlace(place);
    }

    public void onMemberFilterChecked(@NonNull String email, boolean isChecked) {
        if (isChecked)
            meetingRepository.addMember(email);
        else
            meetingRepository.removeMember(email);
    }
}
