package com.neige_i.mareu.view.add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DI;
import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.view.add.model.MemberUi;

public class AddFragment extends Fragment {

    public static AddFragment newInstance() {
        return new AddFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final AddViewModel viewModel = new ViewModelProvider(
                requireActivity(),
                AddViewModelFactory.getInstance()
        ).get(AddViewModel.class);

        // Init views
        final TextInputEditText topicInput = configTopic(viewModel);
        final TextInputEditText startTimeInput = configStartTime(viewModel);
        final TextInputEditText endTimeInput = configEndTime(viewModel);
        final TextInputEditText dateInput = configDate(viewModel);
        final AutoCompleteTextView placeInput = configPlace(viewModel);
        final MemberAdapter memberAdapter = configMemberList(viewModel);
        configButton(viewModel);
        final TextInputLayout topicLayout = requireView().findViewById(R.id.topic_layout);
        final TextInputLayout startTimeLayout = requireView().findViewById(R.id.start_time_layout);
        final TextInputLayout endTimeLayout = requireView().findViewById(R.id.end_time_layout);
        final TextInputLayout dateLayout = requireView().findViewById(R.id.date_layout);
        final TextInputLayout placeLayout = requireView().findViewById(R.id.place_layout);

        // Update UI when 'state' LiveData is changed
        viewModel.getMeetingUiLiveData().observe(getViewLifecycleOwner(), meetingUi -> {
            setTextIfDifferent(topicInput, meetingUi.getTopic());
            startTimeInput.setText(meetingUi.getStartTime());
            endTimeInput.setText(meetingUi.getEndTime());
            dateInput.setText(meetingUi.getDate());
            placeInput.setText(meetingUi.getPlace(), false);
            memberAdapter.submitList(meetingUi.getMemberList());
            topicLayout.setError(meetingUi.getTopicError());
            startTimeLayout.setError(meetingUi.getStartTimeError());
            endTimeLayout.setError(meetingUi.getEndTimeError());
            dateLayout.setError(meetingUi.getDateError());
            placeLayout.setError(meetingUi.getPlaceError());
        });

        // Update UI when 'event' LiveData is triggered
        showDatePicker(viewModel);
        showTimePicker(viewModel);
        showSnackbar(viewModel);
        endActivity(viewModel);
    }

    // Avoid infinite loop
    private void setTextIfDifferent(@NonNull TextInputEditText input, @NonNull String newText) {
        if (!input.getText().toString().equals(newText)) {
            input.setText(newText);
        }
    }

    // ------------
    // Retrieve views from layout
    // Initialize them with ViewModel
    // Let ViewModel do computations on click events
    // ------------

    @NonNull
    private TextInputEditText configTopic(@NonNull AddViewModel viewModel) {
        final TextInputEditText topic = requireView().findViewById(R.id.topic_input);
        topic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.onTopicChanged(s.toString());
            }
        });
        return topic;
    }

    @NonNull
    private TextInputEditText configStartTime(@NonNull AddViewModel viewModel) {
        final TextInputEditText timeInput = requireView().findViewById(R.id.start_time_input);
        timeInput.setOnClickListener(input -> viewModel.onTimeClicked(timeInput.getId()));
        return timeInput;
    }

    @NonNull
    private TextInputEditText configEndTime(@NonNull AddViewModel viewModel) {
        final TextInputEditText timeInput = requireView().findViewById(R.id.end_time_input);
        timeInput.setOnClickListener(input -> viewModel.onTimeClicked(timeInput.getId()));
        return timeInput;
    }

    @NonNull
    private TextInputEditText configDate(@NonNull AddViewModel viewModel) {
        final TextInputEditText dateInput = requireView().findViewById(R.id.date_input);
        dateInput.setOnClickListener(input -> viewModel.onDateClicked());
        return dateInput;
    }

    @NonNull
    private AutoCompleteTextView configPlace(@NonNull AddViewModel viewModel) {
        final AutoCompleteTextView placeInput = requireView().findViewById(R.id.place_input);
        placeInput.setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                DummyGenerator.PLACES
        ));
        placeInput.setOnItemClickListener((parent, view1, position, id) ->
                viewModel.onPlaceChanged(parent.getItemAtPosition(position).toString()));
        return placeInput;
    }

    @NonNull
    private MemberAdapter configMemberList(@NonNull AddViewModel viewModel) {
        final MemberAdapter memberAdapter = new MemberAdapter(new MemberAdapter.OnMemberChangedListener() {
            @Override
            public void onAddMember(int position) {
                viewModel.onAddMember(position);
            }

            @Override
            public void onRemoveMember(@NonNull MemberUi memberUi) {
                viewModel.onRemoveMember(memberUi);
            }

            @Override
            public void onEmailChosen(int position, @NonNull String email) {
                viewModel.onUpdateMember(position, email);
            }
        });
        final RecyclerView recyclerView = requireView().findViewById(R.id.list_member);
        recyclerView.setAdapter(memberAdapter);
//        viewModel.getMemberToShow().observe(getViewLifecycleOwner(), position -> {
//            assert recyclerView.getLayoutManager() != null; // Defined in XML
//            recyclerView.getLayoutManager().scrollToPosition(position); // TODO: auto scroll
//        });
        return memberAdapter;
    }

    private void configButton(@NonNull AddViewModel viewModel) {
        requireView().findViewById(R.id.add_button).setOnClickListener(button -> viewModel.onAddMeeting(requireContext()));
    }

    private void showDatePicker(@NonNull AddViewModel viewModel) {
        // Caution with month value range!
        // LocalDate (java.time) returns a month between 1 and 12 whereas Calendar between 0 and 11
        viewModel.getDatePicker().observe(getViewLifecycleOwner(), localDate -> {
            final DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view1, year, month, dayOfMonth) -> viewModel.onDateChanged(year, month + 1, dayOfMonth),
                localDate.getYear(),
                localDate.getMonthValue() - 1,
                localDate.getDayOfMonth()
            );
            datePickerDialog.getDatePicker().setMinDate(DI.getClock().instant().toEpochMilli());
            datePickerDialog.show();
        });
    }

    private void showTimePicker(@NonNull AddViewModel viewModel) {
        viewModel.getTimePicker().observe(getViewLifecycleOwner(), localTime -> new TimePickerDialog(
            requireContext(),
            (view1, hourOfDay, minute) -> viewModel.onTimeChanged(hourOfDay, minute, requireContext()),
            localTime.getHour(),
            localTime.getMinute(),
            true
        ).show());
        // ASKME: getTimePicker()
    }

    private void showSnackbar(@NonNull AddViewModel viewModel) {
        viewModel.getShowSnack().observe(getViewLifecycleOwner(), errorMessageId ->
                Snackbar.make(requireView(), errorMessageId, Snackbar.LENGTH_LONG).show());
    }

    private void endActivity(@NonNull AddViewModel viewModel) {
        viewModel.getEndActivity().observe(getViewLifecycleOwner(), aVoid -> requireActivity().finish());
    }
}