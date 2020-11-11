package com.neige_i.mareu.view.list;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.view.add.AddActivity;
import com.neige_i.mareu.view.list.view_model.ListViewModel;
import com.neige_i.mareu.view.list.view_model.ListViewModelFactory;

import static com.neige_i.mareu.Util.getNames;

public class ListFragment extends Fragment {

    // -------------------------------- INTENT EXTRA KEY VARIABLES ---------------------------------

    public static final String MEETING = "MEETING_EXTRA";

    // -------------------------------------- LOCAL VARIABLES --------------------------------------

    private ListViewModel viewModel;

    // -------------------------------------- FACTORY METHODS --------------------------------------

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    // ------------------------------------- FRAGMENT METHODS --------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        // Init ViewModel
        viewModel = new ViewModelProvider(
            requireActivity(),
            ListViewModelFactory.getInstance()
        ).get(ListViewModel.class);

        // Init ActivityResultLauncher
        final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                final Intent data = result.getData();
                // If AddActivity is finished by clicking the up or back button, data is null
                if (data != null)
                    viewModel.addMeeting(data.getParcelableExtra(MEETING));
            });

        // Init views
        final MeetingUiAdapter adapter = configRecyclerView();
        final TextView noMeetingText = requireView().findViewById(R.id.no_meeting);
        final TextInputEditText startDateInput = configStartDateInput();
        final TextInputEditText endDateInput = configEndDateInput();
        final TextInputEditText startTimeInput = configStartTimeInput();
        final TextInputEditText endTimeInput = configEndTimeInput();
        configDateAndTimeLayouts();
        configPlace();
        configMember();
        configFab(launcher);
        final MotionLayout motionLayout = requireView().findViewById(R.id.motion_layout);

        // Update UI when 'state' LiveData is changed
        viewModel.getUiState().observe(getViewLifecycleOwner(), listUiModel -> {
            adapter.submitList(listUiModel.getMeetingList());
            noMeetingText.setVisibility(listUiModel.getTextVisibility());
            startDateInput.setText(listUiModel.getFromDate());
            endDateInput.setText(listUiModel.getUntilDate());
            startTimeInput.setText(listUiModel.getFromTime());
            endTimeInput.setText(listUiModel.getUntilTime());
        });
        viewModel.getDrawerLayoutState().observe(getViewLifecycleOwner(), motionLayout::transitionToState);

        // Update UI when 'event' LiveData is triggered
        showDatePicker();
        showTimePicker();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter) {
            viewModel.toggleDrawerLayoutVisibility();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ----------------------------------- RECYCLER VIEW METHODS -----------------------------------

    @NonNull
    private MeetingUiAdapter configRecyclerView() {
        final MeetingUiAdapter meetingUiAdapter = new MeetingUiAdapter(viewModel::removeMeeting);
        final RecyclerView recyclerView = requireView().findViewById(R.id.list_meeting);
        recyclerView.setItemAnimator(null); // remove RecyclerView animations for UI tests
        recyclerView.setAdapter(meetingUiAdapter);
        return meetingUiAdapter;
    }

    // -------------------------------- DATE & TIME FILTER METHODS ---------------------------------

    private TextInputEditText configStartDateInput() {
        final TextInputEditText startDateInput = requireView().findViewById(R.id.start_date_filter_input);
        startDateInput.setOnClickListener(v -> viewModel.showDatePickerDialog(startDateInput.getId()));
        return startDateInput;
    }

    private TextInputEditText configEndDateInput() {
        final TextInputEditText endDateInput = requireView().findViewById(R.id.end_date_filter_input);
        endDateInput.setOnClickListener(v -> viewModel.showDatePickerDialog(endDateInput.getId()));
        return endDateInput;
    }

    private TextInputEditText configStartTimeInput() {
        final TextInputEditText startTimeInput = requireView().findViewById(R.id.start_time_filter_input);
        startTimeInput.setOnClickListener(v -> viewModel.showTimePickerDialog(startTimeInput.getId()));
        return startTimeInput;
    }

    private TextInputEditText configEndTimeInput() {
        final TextInputEditText endTimeInput = requireView().findViewById(R.id.end_time_filter_input);
        endTimeInput.setOnClickListener(v -> viewModel.showTimePickerDialog(endTimeInput.getId()));
        return endTimeInput;
    }

    private void configDateAndTimeLayouts() {
        final TextInputLayout startDateLayout = requireView().findViewById(R.id.start_date_filter_layout);
        final TextInputLayout endDateLayout = requireView().findViewById(R.id.end_date_filter_layout);
        final TextInputLayout startTimeLayout = requireView().findViewById(R.id.start_time_filter_layout);
        final TextInputLayout endTimeLayout = requireView().findViewById(R.id.end_time_filter_layout);
        setEndIconOnClickListener(startDateLayout, viewModel);
        setEndIconOnClickListener(endDateLayout, viewModel);
        setEndIconOnClickListener(startTimeLayout, viewModel);
        setEndIconOnClickListener(endTimeLayout, viewModel);
    }

    private void setEndIconOnClickListener(@NonNull TextInputLayout layout, @NonNull ListViewModel viewModel) {
        layout.setEndIconOnClickListener(v -> viewModel.clearDateTimeField(layout.getId()));
    }

    // ------------------------------- PLACE & MEMBER FILTER METHODS -------------------------------

    private void configPlace() {
        final RecyclerView placeList = requireView().findViewById(R.id.place_list);
        placeList.setLayoutManager(new GridLayoutManager(requireContext(), 4));
        placeList.setAdapter(new PlaceFilterAdapter(DummyGenerator.CHECKABLE_LOGOS, viewModel::setPlaceFilter));
    }

    private void configMember() {
        ((RecyclerView) requireView().findViewById(R.id.member_list))
            .setAdapter(new MemberFilterAdapter(getNames(DummyGenerator.EMAILS),viewModel::setEmailFilter));
    }

    // ---------------------------------------- FAB METHODS ----------------------------------------

    private void configFab(@NonNull ActivityResultLauncher<Intent> launcher) {
        final View fab = requireView().findViewById(R.id.add_meeting);
        fab.setOnClickListener(v -> launcher.launch(new Intent(requireActivity(), AddActivity.class)));
        fab.setOnLongClickListener(v -> {
            viewModel.generateDummyMeetings();
            return true;
        });
    }

    // ------------------------------------- UI EVENT METHODS --------------------------------------

    private void showDatePicker() {
        // TIPS: Month value range is 1..12 with LocalDate and 0..11 with Calendar
        viewModel.getDatePickerEvent().observe(
            getViewLifecycleOwner(),
            localDate -> new DatePickerDialog(
                requireContext(),
                (view1, year, month, dayOfMonth) -> viewModel.setDateFilter(year, month + 1, dayOfMonth),
                localDate.getYear(),
                localDate.getMonthValue() - 1,
                localDate.getDayOfMonth()
            ).show()
        );
    }

    private void showTimePicker() {
        viewModel.getTimePickerEvent().observe(
            getViewLifecycleOwner(),
            localTime -> new TimePickerDialog(
                requireContext(),
                (view1, hourOfDay, minute) -> viewModel.setTimeFilter(hourOfDay, minute),
                localTime.getHour(),
                localTime.getMinute(),
                true
            ).show()
        );
    }
}