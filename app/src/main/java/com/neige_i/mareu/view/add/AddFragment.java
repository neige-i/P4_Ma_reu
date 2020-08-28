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

public class AddFragment extends Fragment implements MemberAdapter.OnMemberChangedListener {

    private AddViewModel viewModel;

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

        viewModel = new ViewModelProvider(
                requireActivity(),
                AddViewModelFactory.getInstance()
        ).get(AddViewModel.class);

        configTopic();
        configTime();
        configDate();
        configPlace();
        configMemberList();
        configButton();
        configInputLayouts();
        configSnackbar();
        configEndActivity();
    }

    // ------------
    // Retrieve views from layout
    // Initialize them with ViewModel
    // Let ViewModel do computations on click events
    // ------------

    private void configTopic() {
        final TextInputEditText topic = requireView().findViewById(R.id.topic_input);
        topic.setText(viewModel.getTopic());
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
    }

    private void configTime() {
        final TextInputEditText timeInput = requireView().findViewById(R.id.time_input);
        timeInput.setText(viewModel.getTime());
        timeInput.setOnClickListener(input -> new TimePickerDialog(
                requireContext(),
                (view1, hourOfDay, minute) -> {
                    viewModel.setTime(hourOfDay, minute);
                    timeInput.setText(viewModel.getTime());
                },
                viewModel.getHour(),
                viewModel.getMinute(),
                true
        ).show());
//        viewModel.getTime().observe(getViewLifecycleOwner(), timeInput::setText);
    }

    private void configDate() {
        final TextInputEditText dateInput = requireView().findViewById(R.id.date_input);
        dateInput.setText(viewModel.getDate());
        dateInput.setOnClickListener(input -> new DatePickerDialog(
                requireContext(),
                (view1, year, month, dayOfMonth) -> {
                    viewModel.setDate(year, month, dayOfMonth);
                    dateInput.setText(viewModel.getDate());
                },
                viewModel.getYear(),
                viewModel.getMonth(),
                viewModel.getDay()
        ).show());
//        viewModel.getDate().observe(getViewLifecycleOwner(), dateInput::setText);
    }

    private void configPlace() {
        final AutoCompleteTextView placeInput = requireView().findViewById(R.id.place_input);
        placeInput.setText(viewModel.getPlace());
        placeInput.setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                DummyGenerator.generateMeetingPlaces()
        ));
        placeInput.setOnItemClickListener((parent, view1, position, id) ->
                viewModel.setPlace(parent.getItemAtPosition(position).toString()));
    }

    private void configMemberList() {
        final MemberAdapter memberAdapter = new MemberAdapter(this);
        ((RecyclerView) requireView().findViewById(R.id.list_member)).setAdapter(memberAdapter);
        viewModel.getMemberList().observe(getViewLifecycleOwner(), memberAdapter::submitList);
    }

    private void configButton() {
        requireView().findViewById(R.id.add_button).setOnClickListener(button -> viewModel.onAddMeeting());
    }

    private void configInputLayouts() {
        final TextInputLayout topicLayout = requireView().findViewById(R.id.topic_layout);
        final TextInputLayout timeLayout = requireView().findViewById(R.id.time_layout);
        final TextInputLayout dateLayout = requireView().findViewById(R.id.date_layout);
        final TextInputLayout placeLayout = requireView().findViewById(R.id.place_layout);
        viewModel.getTopicError().observe(getViewLifecycleOwner(), topicLayout::setError);
        viewModel.getTimeError().observe(getViewLifecycleOwner(), timeLayout::setError);
        viewModel.getDateError().observe(getViewLifecycleOwner(), dateLayout::setError);
        viewModel.getPlaceError().observe(getViewLifecycleOwner(), placeLayout::setError);
    }

    private void configSnackbar() {
        viewModel.getShowSnack().observe(getViewLifecycleOwner(), errorMessageId -> {
            // TODO: use SingleLiveEvent to remove if condition
            if (errorMessageId != -1) {
                Snackbar.make(requireView(), errorMessageId, Snackbar.LENGTH_LONG).show();
                viewModel.cancelShowSnack();
            }
        });
    }

    private void configEndActivity() {
        viewModel.getEndActivity().observe(getViewLifecycleOwner(), aBoolean -> {
            // TODO: use SingleLiveEvent to remove if condition
            if (aBoolean) {
                requireActivity().finish();
                viewModel.cancelEndActivity();
            }
        });
    }

    // ------------
    // Let ViewModel do computations on ViewHolder's click events
    // ------------

    @Override
    // Add the member after the current one
    public void onAddMember(int position) {
        viewModel.addMember(position + 1);
    }

    @Override
    public void onRemoveMember(int position) {
        viewModel.removeMember(position);
    }

    @Override
    public void onEmailChosen(int position, String email) {
        viewModel.updateMember(position, email);
    }
}