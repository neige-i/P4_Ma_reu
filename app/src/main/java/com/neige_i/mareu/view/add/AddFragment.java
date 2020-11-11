package com.neige_i.mareu.view.add;

import android.app.Activity;
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

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DI;
import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.view.add.view_model.AddViewModel;
import com.neige_i.mareu.view.add.view_model.AddViewModelFactory;
import com.neige_i.mareu.view.list.ListFragment;

import static com.neige_i.mareu.Util.setTextWithoutFilter;

public class AddFragment extends Fragment {

    // -------------------------------------- FACTORY METHODS --------------------------------------

    public static AddFragment newInstance() {
        return new AddFragment();
    }

    // ------------------------------------- FRAGMENT METHODS --------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init ViewModel
        final AddViewModel viewModel = new ViewModelProvider(
            requireActivity(),
            AddViewModelFactory.getInstance()
        ).get(AddViewModel.class);

        // Init views
        final TextInputEditText topicInput = configTopic(viewModel);
        final TextInputEditText dateInput = configDate(viewModel);
        final TextInputEditText startTimeInput = configStartTime(viewModel);
        final TextInputEditText endTimeInput = configEndTime(viewModel);
        final AutoCompleteTextView placeInput = configPlace(viewModel);
        final MemberUiAdapter memberUiAdapter = configMemberList(viewModel);
        configButton(viewModel);
        final TextInputLayout topicLayout = requireView().findViewById(R.id.topic_layout);
        final TextInputLayout dateLayout = requireView().findViewById(R.id.date_layout);
        final TextInputLayout startTimeLayout = requireView().findViewById(R.id.start_time_layout);
        final TextInputLayout endTimeLayout = requireView().findViewById(R.id.end_time_layout);
        final TextInputLayout placeLayout = requireView().findViewById(R.id.place_layout);

        // Update UI when 'state' LiveData is changed
        viewModel.getMeetingUiModel().observe(getViewLifecycleOwner(), meetingUiModel -> {
            if (topicInput.getText() != null && !topicInput.getText().toString().equals(meetingUiModel.getTopic()))
                topicInput.setText(meetingUiModel.getTopic());
            dateInput.setText(meetingUiModel.getDate());
            startTimeInput.setText(meetingUiModel.getStartTime());
            endTimeInput.setText(meetingUiModel.getEndTime());
            setTextWithoutFilter(placeInput, meetingUiModel.getPlace());
            memberUiAdapter.submitList(meetingUiModel.getMemberList());
            topicLayout.setError(meetingUiModel.getTopicError(requireActivity().getApplication()));
            dateLayout.setError(meetingUiModel.getDateError(requireActivity().getApplication()));
            startTimeLayout.setError(meetingUiModel.getStartTimeError(requireActivity().getApplication()));
            endTimeLayout.setError(meetingUiModel.getEndTimeError(requireActivity().getApplication()));
            placeLayout.setError(meetingUiModel.getPlaceError(requireActivity().getApplication()));
        });

        // Update UI when 'event' LiveData is triggered
        showDatePicker(viewModel);
        showTimePicker(viewModel);
        endActivity(viewModel);
    }

    // --------------------------------------- TOPIC METHODS ---------------------------------------

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
                viewModel.setTopic(s.toString());
            }
        });
        return topic;
    }

    // ------------------------------------- DATE/TIME METHODS -------------------------------------

    @NonNull
    private TextInputEditText configDate(@NonNull AddViewModel viewModel) {
        final TextInputEditText dateInput = requireView().findViewById(R.id.date_input);
        dateInput.setOnClickListener(input -> viewModel.showDatePickerDialog());
        return dateInput;
    }

    @NonNull
    private TextInputEditText configStartTime(@NonNull AddViewModel viewModel) {
        return configTime(viewModel, R.id.start_time_input);
    }

    @NonNull
    private TextInputEditText configEndTime(@NonNull AddViewModel viewModel) {
        return configTime(viewModel, R.id.end_time_input);
    }

    private TextInputEditText configTime(@NonNull AddViewModel viewModel, @IdRes int inputId) {
        final TextInputEditText timeInput = requireView().findViewById(inputId);
        timeInput.setOnClickListener(input -> viewModel.showTimePickerDialog(inputId));
        return timeInput;
    }

    // --------------------------------------- PLACE METHODS ---------------------------------------

    @NonNull
    private AutoCompleteTextView configPlace(@NonNull AddViewModel viewModel) {
        final AutoCompleteTextView placeInput = requireView().findViewById(R.id.place_input);
        placeInput.setAdapter(new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            DummyGenerator.PLACES
        ));
        placeInput.setOnItemClickListener((parent, view1, position, id) ->
                                              viewModel.setPlace(parent.getItemAtPosition(position).toString()));
        return placeInput;
    }

    // ------------------------------------ MEMBER LIST METHODS ------------------------------------

    @NonNull
    private MemberUiAdapter configMemberList(@NonNull AddViewModel viewModel) {
        final MemberUiAdapter memberUiAdapter = new MemberUiAdapter(new MemberUiAdapter.OnMemberChangedListener() {
            @Override
            public void onAddMember(int position) {
                viewModel.addMember(position);
            }

            @Override
            public void onEmailChosen(int position, @NonNull String email) {
                viewModel.updateMember(position, email);
            }

            @Override
            public void onRemoveMember(int position) {
                viewModel.removeMember(position);
            }
        });
        RecyclerView recyclerView = requireView().findViewById(R.id.list_member);
        recyclerView.setItemAnimator(null); // remove RecyclerView animations for UI tests
        recyclerView.setAdapter(memberUiAdapter);
        return memberUiAdapter;
    }

    // ---------------------------------------- FAB METHODS ----------------------------------------

    private void configButton(@NonNull AddViewModel viewModel) {
        requireView().findViewById(R.id.add_button).setOnClickListener(button -> viewModel.onAddMeeting());
    }

    // ------------------------------------- UI EVENT METHODS --------------------------------------

    private void showDatePicker(@NonNull AddViewModel viewModel) {
        viewModel.getDatePickerEvent().observe(getViewLifecycleOwner(), localDate -> {
            final DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> viewModel.setDate(year, month + 1, dayOfMonth),
                localDate.getYear(),
                localDate.getMonthValue() - 1,
                localDate.getDayOfMonth()
            );
            datePickerDialog.getDatePicker().setMinDate(DI.getClock().instant().toEpochMilli());
            datePickerDialog.show();
        });
    }

    private void showTimePicker(@NonNull AddViewModel viewModel) {
        viewModel.getTimePickerEvent().observe(
            getViewLifecycleOwner(),
            localTime -> new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> viewModel.setTime(hourOfDay, minute),
                localTime.getHour(),
                localTime.getMinute(),
                true
            ).show()
        );
    }

    private void endActivity(@NonNull AddViewModel viewModel) {
        viewModel.getEndActivityEvent().observe(getViewLifecycleOwner(), meeting -> {
            requireActivity().setResult(
                Activity.RESULT_OK,
                requireActivity().getIntent().putExtra(ListFragment.MEETING, meeting)
            );
            requireActivity().finish();
        });
    }
}