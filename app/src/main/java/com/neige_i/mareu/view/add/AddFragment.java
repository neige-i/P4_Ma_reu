package com.neige_i.mareu.view.add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DummyGenerator;

import java.util.Calendar;

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

        // Set calendar
        Calendar calendar = Calendar.getInstance();

        // Config topic
        requireView().findViewById(R.id.topic_input);

        // Config time
        requireView().findViewById(R.id.time_input).setOnClickListener(input -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
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
        requireView().findViewById(R.id.date_input).setOnClickListener(input -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
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
        ((AutoCompleteTextView) requireView().findViewById(R.id.place_input)).setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                DummyGenerator.generateMeetingPlaces()
        ));

        // Config button
        requireView().findViewById(R.id.add_button).setOnClickListener(button -> {
        });
    }
}