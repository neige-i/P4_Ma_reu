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

        final ListViewModel viewModel = new ViewModelProvider(
                requireActivity(),
                ListViewModelFactory.getInstance()
        ).get(ListViewModel.class);

        configRecyclerView(viewModel);
        configTextView(viewModel);
        configFab();
    }

    private void configRecyclerView(ListViewModel viewModel) {
        final MeetingAdapter meetingAdapter = new MeetingAdapter();
        ((RecyclerView) requireView().findViewById(R.id.list_meeting)).setAdapter(meetingAdapter);
        viewModel.getMeetings().observe(getViewLifecycleOwner(), meetings -> {
            meetingAdapter.submitList(meetings);
            viewModel.setTextViewVisibility(meetings.isEmpty());
        });
    }

    private void configTextView(ListViewModel viewModel) {
        final TextView noMeeting = requireView().findViewById(R.id.no_meeting);
        viewModel.getTextViewVisibility().observe(getViewLifecycleOwner(), noMeeting::setVisibility);
    }

    private void configFab() {
        requireView().findViewById(R.id.add_meeting).setOnClickListener(fab ->
                startActivity(new Intent(requireActivity(), AddActivity.class)));
    }
}