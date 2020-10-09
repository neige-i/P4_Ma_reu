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
import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.view.model.MemberUi;

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
        final TextInputEditText timeInput = configTime(viewModel);
        final TextInputEditText dateInput = configDate(viewModel);
        final AutoCompleteTextView placeInput = configPlace(viewModel);
        final MemberAdapter memberAdapter = configMemberList(viewModel);
        configButton(viewModel);
        final TextInputLayout topicLayout = requireView().findViewById(R.id.topic_layout);
        final TextInputLayout timeLayout = requireView().findViewById(R.id.time_layout);
        final TextInputLayout dateLayout = requireView().findViewById(R.id.date_layout);
        final TextInputLayout placeLayout = requireView().findViewById(R.id.place_layout);

        // Update UI when 'state' LiveData is changed
        viewModel.getMeetingUiLiveData().observe(getViewLifecycleOwner(), meetingUi -> {
            setTextIfDifferent(topicInput, meetingUi.getTopic());
            timeInput.setText(meetingUi.getTimeStart());
            dateInput.setText(meetingUi.getDate());
            placeInput.setText(meetingUi.getPlace(), false);
            memberAdapter.submitList(meetingUi.getMemberList());
            topicLayout.setError(meetingUi.getTopicError());
            timeLayout.setError(meetingUi.getTimeStartError());
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
    private void setTextIfDifferent(TextInputEditText input, String newText) {
        if (input.getText() != null && !input.getText().toString().equals(newText)) {
            input.setText(newText);
        }
    }

    // ------------
    // Retrieve views from layout
    // Initialize them with ViewModel
    // Let ViewModel do computations on click events
    // ------------

    private TextInputEditText configTopic(AddViewModel viewModel) {
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

    private TextInputEditText configTime(AddViewModel viewModel) {
        final TextInputEditText timeInput = requireView().findViewById(R.id.time_input);
        timeInput.setOnClickListener(input -> viewModel.onTimeClicked());
        return timeInput;
    }

    private TextInputEditText configDate(AddViewModel viewModel) {
        final TextInputEditText dateInput = requireView().findViewById(R.id.date_input);
        dateInput.setOnClickListener(input -> viewModel.onDateClicked());
        return dateInput;
    }

    private AutoCompleteTextView configPlace(AddViewModel viewModel) {
        final AutoCompleteTextView placeInput = requireView().findViewById(R.id.place_input);
        placeInput.setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                DummyGenerator.generateMeetingPlaces()
        ));
        placeInput.setOnItemClickListener((parent, view1, position, id) ->
                viewModel.onPlaceSelected(parent.getItemAtPosition(position).toString()));
        return placeInput;
    }

    private MemberAdapter configMemberList(final AddViewModel viewModel) {
        // ASKME: get LiveData value without observing
        final MemberAdapter memberAdapter = new MemberAdapter(viewModel.getMeetingUiLiveData().getValue().getAvailableMembers(), new MemberAdapter.OnMemberChangedListener() {
            @Override
            public void onAddMember(int position) {
                viewModel.onAddMember(position);
            }

            @Override
            public void onRemoveMember(MemberUi memberUi) {
                viewModel.onRemoveMember(memberUi);
            }

            @Override
            public void onEmailChosen(int position, String email) {
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

    private void configButton(AddViewModel viewModel) {
        requireView().findViewById(R.id.add_button).setOnClickListener(button -> viewModel.onAddMeeting());
    }

    private void showDatePicker(AddViewModel viewModel) {
        viewModel.getDatePicker().observe(getViewLifecycleOwner(), localDate -> new DatePickerDialog(
            requireContext(),
            (view1, year, month, dayOfMonth) -> viewModel.onDateValidated(year, month, dayOfMonth),
            localDate.getYear(),
            localDate.getMonthValue(),
            localDate.getDayOfMonth()
        ).show());
    }

    private void showTimePicker(AddViewModel viewModel) {
        viewModel.getTimePicker().observe(getViewLifecycleOwner(), localTime -> new TimePickerDialog(
            requireContext(),
            (view1, hourOfDay, minute) -> viewModel.onTimeValidated(hourOfDay, minute),
            localTime.getHour(),
            localTime.getMinute(),
            true
        ).show());
    }

    private void showSnackbar(AddViewModel viewModel) {
        viewModel.getShowSnack().observe(getViewLifecycleOwner(), errorMessageId ->
                Snackbar.make(requireView(), errorMessageId, Snackbar.LENGTH_LONG).show());
    }

    private void endActivity(AddViewModel viewModel) {
        viewModel.getEndActivity().observe(getViewLifecycleOwner(), aVoid -> requireActivity().finish());
    }
}