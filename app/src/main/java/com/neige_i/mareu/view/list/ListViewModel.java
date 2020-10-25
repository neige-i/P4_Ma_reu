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

    // ------------------------------------  INSTANCE VARIABLES ------------------------------------

    @NonNull
    private final MeetingRepository meetingRepository;
    @NonNull
    private final Clock clock; // Handy for testing

    // -------------------------------------  STATE LIVE DATA --------------------------------------

    @NonNull
    private final MutableLiveData<ListUiModel> listUiModel = new MutableLiveData<>(new ListUiModel());

    // -------------------------------------  EVENT LIVE DATA --------------------------------------

    @NonNull
    private final SingleLiveEvent<LocalDate> datePickerEvent = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<LocalTime> timePickerEvent = new SingleLiveEvent<>();

    // -------------------------------------  LOCAL VARIABLES --------------------------------------

    private boolean isStartDate;
    private boolean isStartTime;

    // ----------------------------------- CONSTRUCTOR & GETTERS -----------------------------------

    public ListViewModel(@NonNull MeetingRepository meetingRepository, @NonNull Clock clock) {
        this.meetingRepository = meetingRepository;
        this.clock = clock;
    }

    public LiveData<ListUiModel> getListUiModel() {
        return Transformations.switchMap(
            meetingRepository.getFilteredList(),
            meetingList -> Transformations.map(
                listUiModel,
                ui -> new ListUiModel.Builder(ui).setMeetingList(meetingList).build()
            )
        );
    }

    @NonNull
    public LiveData<LocalDate> getDatePickerEvent() {
        return datePickerEvent;
    }

    @NonNull
    public LiveData<LocalTime> getTimePickerEvent() {
        return timePickerEvent;
    }

    // ----------------------------------- RECYCLER VIEW METHODS -----------------------------------

    public void removeMeeting(int meetingId) {
        meetingRepository.deleteMeeting(meetingId);
    }

    public void generateDummyMeetings() {
        meetingRepository.addDummyList(DummyGenerator.generateMeetings());
    }

    // ----------------------------------- FILTER LAYOUT METHODS -----------------------------------

    public void toggleFilterLayoutVisibility() {
        listUiModel.setValue(new ListUiModel.Builder(listUiModel.getValue()).toggleDrawerState().build());
    }

    // --------------------------------------- DATE METHODS ----------------------------------------

    // TODO: duplicate code

    public void showDatePickerDialog(@IdRes int dateInputId) {
        isStartDate = dateInputId == R.id.start_date_filter_input;

        final LocalDate currentDate = isStartDate ? getStartDate() : getEndDate();
        datePickerEvent.setValue(currentDate == null ? LocalDate.now(clock) : currentDate);
    }

    @Nullable
    private LocalDate getStartDate() {
        final String startDate = listUiModel.getValue().getStartDate();
        return startDate.isEmpty() ? null : LocalDate.parse(startDate, DATE_FORMAT);
    }

    @Nullable
    private LocalDate getEndDate() {
        final String EndDate = listUiModel.getValue().getEndDate();
        return EndDate.isEmpty() ? null : LocalDate.parse(EndDate, DATE_FORMAT);
    }

    public void setDateFilter(int year, int month, int dayOfMonth) {
        final String dateTime = LocalDate.of(year, month, dayOfMonth).format(DATE_FORMAT);
        final ListUiModel.Builder builder = new ListUiModel.Builder(listUiModel.getValue());
        if (isStartDate)
            builder.setStartDate(dateTime);
        else
            builder.setEndDate(dateTime);
        listUiModel.setValue(builder.build());

        if (isStartDate) {
            meetingRepository.setFrom(getStartDate());
        } else {
            meetingRepository.setUntil(getEndDate());
        }
    }

    // --------------------------------------- TIME METHODS ----------------------------------------

    /**
     * Called when the user clicks to choose the start time or end time of the meeting.
     */
    public void showTimePickerDialog(@IdRes int timeInputId) {
        isStartTime = timeInputId == R.id.start_time_filter_input;

        final LocalTime localTime = isStartTime ? getStartTime() : getEndTime();
        timePickerEvent.setValue(localTime == null ? LocalTime.now(clock) : localTime);
    }

    /**
     * Returns the start time of the meeting or null if not set yet.
     */
    @Nullable
    private LocalTime getStartTime() {
        final String startTime = listUiModel.getValue().getStartTime();
        return startTime.isEmpty() ? null : LocalTime.parse(startTime, TIME_FORMAT);
    }

    /**
     * Returns the end time of the meeting or null if not set yet.
     */
    @Nullable
    private LocalTime getEndTime() {
        final String endTime = listUiModel.getValue().getEndTime();
        return endTime.isEmpty() ? null : LocalTime.parse(endTime, TIME_FORMAT);
    }

    /**
     * Called when the user validates the start time or end time of the meeting.
     */
    public void setTimeFilter(int hour, int minute) {
        final String stringTime = LocalTime.of(hour, minute).format(TIME_FORMAT);
        final ListUiModel.Builder builder = new ListUiModel.Builder(listUiModel.getValue());
        if (isStartTime)
            builder.setStartTime(stringTime);
        else
            builder.setEndTime(stringTime);
        listUiModel.setValue(builder.build());

        if (isStartTime) {
            meetingRepository.setFrom(getStartTime());
        } else {
            meetingRepository.setUntil(getEndTime());
        }
    }

    // ------------------------------------ DATE & TIME METHODS ------------------------------------

    public void clearDateTimeField(@IdRes int inputId) {
        final ListUiModel.Builder builder = new ListUiModel.Builder(listUiModel.getValue());
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
        listUiModel.setValue(builder.build());
    }

    // ---------------------------------- PLACE & MEMBER METHODS -----------------------------------

    public void setPlaceFilter(@NonNull String place, boolean isChecked) {
        if (isChecked)
            meetingRepository.addPlace(place);
        else
            meetingRepository.removePlace(place);
    }

    public void setMemberFilter(@NonNull String email, boolean isChecked) {
        if (isChecked)
            meetingRepository.addMember(email);
        else
            meetingRepository.removeMember(email);
    }
}
