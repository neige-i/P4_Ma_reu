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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddFragment extends Fragment implements MemberAdapter.OnButtonClickedListener {

    private MemberAdapter memberAdapter;
    private final List<String> memberList = new ArrayList<>(Collections.singletonList(""));

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

        // Retrieve ViewModel
        final AddViewModel addViewModel = new ViewModelProvider(
                requireActivity(),
                AddViewModelFactory.getInstance()
        ).get(AddViewModel.class);

        // Config topic
        ((TextInputEditText) view.findViewById(R.id.topic_input)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                addViewModel.setTopic(s.toString());
            }
        });

        // Config time
        final TextInputEditText timeInput = view.findViewById(R.id.time_input);
        timeInput.setOnClickListener(input -> new TimePickerDialog(
                requireContext(),
                (view1, hourOfDay, minute) -> addViewModel.setTime(hourOfDay, minute),
                addViewModel.getHour(),
                addViewModel.getMinute(),
                true
        ).show());
        addViewModel.getTime().observe(getViewLifecycleOwner(), timeInput::setText);

        // Config date
        final TextInputEditText dateInput = view.findViewById(R.id.date_input);
        dateInput.setOnClickListener(input -> new DatePickerDialog(
                requireContext(),
                (view1, year, month, dayOfMonth) -> addViewModel.setDate(year, month, dayOfMonth),
                addViewModel.getYear(),
                addViewModel.getMonth(),
                addViewModel.getDay()
        ).show());
        addViewModel.getDate().observe(getViewLifecycleOwner(), dateInput::setText);

        // Config place
        final AutoCompleteTextView placeInput = view.findViewById(R.id.place_input);
        placeInput.setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                DummyGenerator.generateMeetingPlaces()
        ));
        placeInput.setOnItemClickListener((parent, view1, position, id) -> {
            addViewModel.setPlace(parent.getItemAtPosition(position).toString());
        });

        // Config RecyclerView
        memberAdapter = new MemberAdapter(this, memberList);
        ((RecyclerView) view.findViewById(R.id.list_member)).setAdapter(memberAdapter);

        // Config button
        view.findViewById(R.id.add_button).setOnClickListener(button -> addViewModel.onAddMeeting());

        // Config error
        final TextInputLayout topicLayout = view.findViewById(R.id.topic_layout);
        final TextInputLayout timeLayout = view.findViewById(R.id.time_layout);
        final TextInputLayout dateLayout = view.findViewById(R.id.date_layout);
        final TextInputLayout placeLayout = view.findViewById(R.id.place_layout);
        addViewModel.getTopicError().observe(getViewLifecycleOwner(), topicLayout::setError);
        addViewModel.getTimeError().observe(getViewLifecycleOwner(), timeLayout::setError);
        addViewModel.getDateError().observe(getViewLifecycleOwner(), dateLayout::setError);
        addViewModel.getPlaceError().observe(getViewLifecycleOwner(), placeLayout::setError);

        // ConfigSnackBar
        addViewModel.getShowSnack().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                Snackbar.make(view.findViewById(R.id.add_button), R.string.mandatory_fields, Snackbar.LENGTH_LONG).show();
                addViewModel.cancelShowSnack();
            }
        });

        // Config ending activity
        addViewModel.getEndActivity().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                requireActivity().finish();
                addViewModel.cancelEndActivity();
            }
        });
    }

    @Override
    public void onButtonClicked(int viewId, int position) {
        switch (viewId) {
            case R.id.add_member:
                memberList.add("");
                break;
            case R.id.remove_member:
                memberList.remove(position);
                break;
        }
        memberAdapter.notifyDataSetChanged();
    }
}