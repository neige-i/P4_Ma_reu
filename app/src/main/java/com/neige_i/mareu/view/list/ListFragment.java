package com.neige_i.mareu.view.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        // Config FAB
        requireView().findViewById(R.id.add_meeting).setOnClickListener(fab ->
                startActivity(new Intent(requireActivity(), AddActivity.class))
        );
    }
}