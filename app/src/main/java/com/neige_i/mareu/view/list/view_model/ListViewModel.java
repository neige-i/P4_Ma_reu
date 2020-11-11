package com.neige_i.mareu.view.list.view_model;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.data.repository.ListingRepository;
import com.neige_i.mareu.data.model.Filters;
import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.view.SingleLiveEvent;
import com.neige_i.mareu.view.list.ui_model.ListUiModel;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;

public class ListViewModel extends ViewModel {

    // -------------------------------------  EVENT LIVE DATA --------------------------------------

    /**
     * Represents the event to display a {@link android.app.DatePickerDialog DatePickerDialog}.
     */
    @NonNull
    private final SingleLiveEvent<LocalDate> datePickerEvent = new SingleLiveEvent<>();
    /**
     * Represents the event to display a {@link android.app.TimePickerDialog TimePickerDialog}.
     */
    @NonNull
    private final SingleLiveEvent<LocalTime> timePickerEvent = new SingleLiveEvent<>();
    /**
     * Represents the state of the drawer layout.
     */
    @NonNull
    private final MutableLiveData<Integer> drawerLayoutState = new MutableLiveData<>(R.id.start);

    // -------------------------------------  LOCAL VARIABLES --------------------------------------

    /**
     * Repository to get and set the filters.
     */
    @NonNull
    private final ListingRepository listingRepository;
    /**
     * Handy for testing.
     */
    @NonNull
    private final Clock clock; // Handy for testing

    /**
     * Determines which date to set.
     */
    private boolean isStartDateSelected;
    /**
     * Determines which time to set.
     */
    private boolean isStartTimeSelected;

    // ----------------------------------- CONSTRUCTOR & GETTERS -----------------------------------

    public ListViewModel(@NonNull ListingRepository listingRepository, @NonNull Clock clock) {
        this.listingRepository = listingRepository;
        this.clock = clock;

        listingRepository.initRepository();
    }

    public LiveData<ListUiModel> getUiState() {
        return Transformations.switchMap(
            listingRepository.getFilteredList(),
            meetingList -> Transformations.map(
                listingRepository.getFilters(),
                filters -> new ListUiModel(
                    meetingList,
                    filters.getFromDate(),
                    filters.getUntilDate(),
                    filters.getFromTime(),
                    filters.getUntilTime()
                )
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

    @NonNull
    public LiveData<Integer> getDrawerLayoutState() {
        return drawerLayoutState;
    }

    // ----------------------------------- RECYCLER VIEW METHODS -----------------------------------

    public void addMeeting(@NonNull Meeting meetingToAdd) {
        listingRepository.addMeeting(meetingToAdd);
    }

    public void removeMeeting(int position) {
        listingRepository.removeMeeting(position);
    }

    public void generateDummyMeetings() {
        listingRepository.addMeetings(DummyGenerator.generateMeetings());
    }

    // ----------------------------------- DRAWER LAYOUT METHODS -----------------------------------

    public void toggleDrawerLayoutVisibility() {
        drawerLayoutState.setValue(drawerLayoutState.getValue() == R.id.start ? R.id.end : R.id.start);
    }

    // ------------------------------------ DATE & TIME METHODS ------------------------------------

    public void showDatePickerDialog(@IdRes int dateInputId) {
        isStartDateSelected = dateInputId == R.id.start_date_filter_input;

        final LocalDate currentDate = isStartDateSelected ? getFilters().getFromDate() : getFilters().getUntilDate();
        datePickerEvent.setValue(currentDate == null ? LocalDate.now(clock) : currentDate);
    }

    public void setDateFilter(int year, int month, int dayOfMonth) {
        final LocalDate selectedDate = LocalDate.of(year, month, dayOfMonth);
        if (isStartDateSelected)
            listingRepository.setFrom(selectedDate);
        else
            listingRepository.setUntil(selectedDate);
    }

    public void showTimePickerDialog(@IdRes int timeInputId) {
        isStartTimeSelected = timeInputId == R.id.start_time_filter_input;

        final LocalTime currentTime = isStartTimeSelected ? getFilters().getFromTime() : getFilters().getUntilTime();
        timePickerEvent.setValue(currentTime == null ? LocalTime.now(clock) : currentTime);
    }

    public void setTimeFilter(int hour, int minute) {
        final LocalTime selectedTime = LocalTime.of(hour, minute);
        if (isStartTimeSelected)
            listingRepository.setFrom(selectedTime);
        else
            listingRepository.setUntil(selectedTime);
    }

    public void clearDateTimeField(@IdRes int inputId) {
        if (inputId == R.id.start_date_filter_layout)
            listingRepository.setFrom((LocalDate) null);
        else if (inputId == R.id.end_date_filter_layout)
            listingRepository.setUntil((LocalDate) null);
        else if (inputId == R.id.start_time_filter_layout)
            listingRepository.setFrom((LocalTime) null);
        else if (inputId == R.id.end_time_filter_layout)
            listingRepository.setUntil((LocalTime) null);
    }

    // ---------------------------------- PLACE & MEMBER METHODS -----------------------------------

    public void setPlaceFilter(@NonNull String place, boolean isChecked) {
        if (isChecked)
            listingRepository.addPlace(place);
        else
            listingRepository.removePlace(place);
    }

    public void setEmailFilter(@NonNull String email, boolean isChecked) {
        if (isChecked)
            listingRepository.addEmail(email);
        else
            listingRepository.removeEmail(email);
    }

    // --------------------------------------  LOCAL METHODS ---------------------------------------

    @NonNull
    private Filters getFilters() {
        return listingRepository.getFilters().getValue();
    }
}
