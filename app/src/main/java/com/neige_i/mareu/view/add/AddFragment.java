package com.neige_i.mareu.view.add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DummyGenerator;

import java.util.ArrayList;
import java.util.Calendar;
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

        // Config topic
        final TextInputEditText topicInput = requireView().findViewById(R.id.topic_input);

        // Set calendar
        final Calendar calendar = Calendar.getInstance();

        // Config time
        final TextInputEditText timeInput = requireView().findViewById(R.id.time_input);
        timeInput.setOnClickListener(input -> {
            final TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireContext(),
                    (view22, hourOfDay, minute) -> {
                        ((TextInputEditText) input).setText(hourOfDay + ":" + minute);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            );
            timePickerDialog.setTitle(R.string.time_title);
            timePickerDialog.show();
        });

        // Config date
        final TextInputEditText dateInput = requireView().findViewById(R.id.date_input);
        dateInput.setOnClickListener(input -> {
            final DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view1, year, month, dayOfMonth) -> {
                        ((TextInputEditText) input).setText(dayOfMonth + "/" + month + "/" + year);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.setTitle(R.string.time_title);
            datePickerDialog.show();
        });

        // Config place
        final AutoCompleteTextView placeInput = requireView().findViewById(R.id.place_input);
        placeInput.setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                DummyGenerator.generateMeetingPlaces()
        ));

        // Config RecyclerView
        memberAdapter = new MemberAdapter(this, memberList);
        ((RecyclerView) requireView().findViewById(R.id.list_member)).setAdapter(memberAdapter);

        // Config button
        requireView().findViewById(R.id.add_button).setOnClickListener(button -> {
            System.out.println("Topic=" + isEmpty(topicInput) + " time=" + isEmpty(timeInput) + " date=" + isEmpty(dateInput) + " place=" + isEmpty(placeInput));
//            requireActivity().finish();
        });
    }

    private String isEmpty(EditText v) {
        String text = v.getText().toString();
        return text.isEmpty() ? "empty" : text;
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