package com.neige_i.mareu.view.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.neige_i.mareu.R;
import com.neige_i.mareu.view.add.AddActivity;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve ViewModel
        final ListViewModel listViewModel = new ViewModelProvider(
                requireActivity(),
                ListViewModelFactory.getInstance()
        ).get(ListViewModel.class);

        // Config RecyclerView & TextView
        final MeetingAdapter meetingAdapter = new MeetingAdapter();
        ((RecyclerView) view.findViewById(R.id.list_meeting)).setAdapter(meetingAdapter);
        final TextView noMeeting = view.findViewById(R.id.no_meeting);
        listViewModel.getMeetings().observe(getViewLifecycleOwner(), meetings -> {
            noMeeting.setVisibility(meetings.isEmpty() ? View.VISIBLE : View.GONE);
            meetingAdapter.submitList(new ArrayList<>(meetings));
        });

        // Config FAB
        view.findViewById(R.id.add_meeting).setOnClickListener(fab -> {
            startActivity(new Intent(requireActivity(), AddActivity.class));
        });
    }
}