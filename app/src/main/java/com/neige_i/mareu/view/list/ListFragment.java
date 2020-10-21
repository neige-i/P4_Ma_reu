package com.neige_i.mareu.view.list;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class ListFragment extends Fragment {

    private ListViewModel viewModel;

    // -------------------------------------- FACTORY METHODS --------------------------------------

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    // ------------------------------------ OVERRIDDEN METHODS -------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        viewModel = new ViewModelProvider(
            requireActivity(),
            ListViewModelFactory.getInstance()
        ).get(ListViewModel.class);

        // Config views
        final MeetingAdapter adapter = configRecyclerView(viewModel);
        final TextView noMeetingTxt = requireView().findViewById(R.id.no_meeting);
        configFab(viewModel);

        final TextInputEditText startDateInput = configStartDateInput(viewModel);
        final TextInputEditText endDateInput = configEndDateInput(viewModel);
        final TextInputEditText startTimeInput = configStartTimeInput(viewModel);
        final TextInputEditText endTimeInput = configEndTimeInput(viewModel);
        configStartDateLayout(viewModel);
        configEndDateLayout(viewModel);
        configStartTimeLayout(viewModel);
        configEndTimeLayout(viewModel);
        configPlace(viewModel);
        configMember(viewModel);
        final MotionLayout motionLayout = requireView().findViewById(R.id.motion_layout);

        viewModel.getListUi().observe(getViewLifecycleOwner(), listUi -> {
            adapter.submitList(listUi.getMeetingList());
            noMeetingTxt.setVisibility(listUi.getTextViewVisibility());
            // FIXME: problem with TextView visibility
            startDateInput.setText(listUi.getStartDate());
            endDateInput.setText(listUi.getEndDate());
            startTimeInput.setText(listUi.getStartTime());
            endTimeInput.setText(listUi.getEndTime());
            motionLayout.transitionToState(listUi.getDrawerState());
        });

        showDatePicker(viewModel);
        showTimePicker(viewModel);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter) {
            viewModel.onFilterClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // -------------------------------------- PRIVATE METHODS --------------------------------------

    @NonNull
    private MeetingAdapter configRecyclerView(@NonNull ListViewModel viewModel) {
        final MeetingAdapter meetingAdapter = new MeetingAdapter(viewModel::onRemoveMeeting);
        ((RecyclerView) requireView().findViewById(R.id.list_meeting)).setAdapter(meetingAdapter);
        return meetingAdapter;
    }

    private void configFab(@NonNull ListViewModel viewModel) {
        final View fab = requireView().findViewById(R.id.add_meeting);
        fab.setOnClickListener(v -> startActivity(new Intent(requireActivity(), AddActivity.class)));
        fab.setOnLongClickListener(v -> {
            viewModel.onGenerateDummyList();
            return true;
        });
    }

    private TextInputEditText configStartDateInput(@NonNull ListViewModel viewModel) {
        final TextInputEditText startDateInput = requireView().findViewById(R.id.start_date_filter_input);
        startDateInput.setOnClickListener(v -> viewModel.onDateClicked(startDateInput.getId()));
        return startDateInput;
    }

    private void configStartDateLayout(@NonNull ListViewModel viewModel) {
        final TextInputLayout startDateLayout = requireView().findViewById(R.id.start_date_filter_layout);
        startDateLayout.setEndIconOnClickListener(v -> viewModel.onFieldCleared(startDateLayout.getId()));
    }

    private TextInputEditText configEndDateInput(@NonNull ListViewModel viewModel) {
        final TextInputEditText endDateInput = requireView().findViewById(R.id.end_date_filter_input);
        endDateInput.setOnClickListener(v -> viewModel.onDateClicked(endDateInput.getId()));
        return endDateInput;
    }

    private void configEndDateLayout(@NonNull ListViewModel viewModel) {
        final TextInputLayout endDateLayout = requireView().findViewById(R.id.end_date_filter_layout);
        endDateLayout.setEndIconOnClickListener(v -> viewModel.onFieldCleared(endDateLayout.getId()));
    }

    private TextInputEditText configStartTimeInput(@NonNull ListViewModel viewModel) {
        final TextInputEditText startTimeInput = requireView().findViewById(R.id.start_time_filter_input);
        startTimeInput.setOnClickListener(v -> viewModel.onTimeClicked(startTimeInput.getId()));
        return startTimeInput;
    }

    private void configStartTimeLayout(@NonNull ListViewModel viewModel) {
        final TextInputLayout startTimeLayout = requireView().findViewById(R.id.start_time_filter_layout);
        startTimeLayout.setEndIconOnClickListener(v -> viewModel.onFieldCleared(startTimeLayout.getId()));
    }

    private TextInputEditText configEndTimeInput(@NonNull ListViewModel viewModel) {
        final TextInputEditText endTimeInput = requireView().findViewById(R.id.end_time_filter_input);
        endTimeInput.setOnClickListener(v -> viewModel.onTimeClicked(endTimeInput.getId()));
        return endTimeInput;
    }

    private void configEndTimeLayout(@NonNull ListViewModel viewModel) {
        final TextInputLayout endTimeLayout = requireView().findViewById(R.id.end_time_filter_layout);
        endTimeLayout.setEndIconOnClickListener(v -> viewModel.onFieldCleared(endTimeLayout.getId()));
    }

    private void configPlace(@NonNull ListViewModel viewModel) {
        final PlaceFilterAdapter placeFilterAdapter = new PlaceFilterAdapter(
            DummyGenerator.CHECKABLE_LOGOS,
            viewModel::onPlaceFilterChecked
        );
        final RecyclerView placeList = requireView().findViewById(R.id.place_list);
        placeList.setAdapter(placeFilterAdapter);
        placeList.setLayoutManager(new GridLayoutManager(requireContext(), 4/*placeAdapter.getItemCount() / 2*/));
    }

    private void configMember(@NonNull ListViewModel viewModel) {
        final MemberFilterAdapter memberFilterAdapter = new MemberFilterAdapter(
            DummyGenerator.getNames(),
            viewModel::onMemberFilterChecked
        );
        final RecyclerView memberList = requireView().findViewById(R.id.member_list);
        memberList.setAdapter(memberFilterAdapter);
//        memberList.setLayoutManager(new GridLayoutManager(requireContext(), memberAdapter.getItemCount() / 2));
    }

    private void showDatePicker(@NonNull ListViewModel viewModel) {
        // Caution with month value range!
        // LocalDate (java.time) returns a month between 1 and 12 whereas Calendar between 0 and 11
        viewModel.getDatePicker().observe(getViewLifecycleOwner(), localDate -> {
            new DatePickerDialog(
                requireContext(),
                (view1, year, month, dayOfMonth) -> viewModel.onDateChanged(year, month + 1, dayOfMonth),
                localDate.getYear(),
                localDate.getMonthValue() - 1,
                localDate.getDayOfMonth()
            ).show();
        });
    }

    private void showTimePicker(@NonNull ListViewModel viewModel) {
        viewModel.getTimePicker().observe(getViewLifecycleOwner(), localTime -> new TimePickerDialog(
            requireContext(),
            (view1, hourOfDay, minute) -> viewModel.onTimeChanged(hourOfDay, minute),
            localTime.getHour(),
            localTime.getMinute(),
            true
        ).show());
        // ASKME: getTimePicker()
    }
}