package com.neige_i.mareu.view.list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.view.add.AddActivity;

import java.util.List;

public class ListFragment extends Fragment {

    private ListViewModel viewModel;

    // -------------------------------------- FACTORY METHODS --------------------------------------

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    // ------------------------------------ OVERRIDDEN METHODS -------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true); // ASKME: leave this call here or move it to onViewCreated()
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(
            requireActivity(),
            ListViewModelFactory.getInstance()
        ).get(ListViewModel.class);

        // Config views
        final MeetingAdapter adapter = configRecyclerView(viewModel);
        final TextView noMeetingTxt = requireView().findViewById(R.id.no_meeting);
        configFab(viewModel);
        final AlertDialog filterDialog = initDialog();
        final AutoCompleteTextView dateFilterInput = configDateFilter(viewModel);
        final AutoCompleteTextView timeFilterInput = configTimeFilter(viewModel);
        final AutoCompleteTextView placeFilterInput = configPlaceFilter(viewModel);
        final AutoCompleteTextView memberFilterInput = configMemberFilter(viewModel);

//        final MaterialButton button = requireView().findViewById(R.id.date_filter);
//        button.setOnClickListener(v -> {
////            View contentView = ((LayoutInflater) requireActivity().getApplicationContext()
////                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
////                .inflate(R.layout.list_item_member, null);
//            View contentView = LayoutInflater.from(requireContext()).inflate(R.layout.layout_spinner, null);
//            Spinner spinner = contentView.findViewById(R.id.spinner);
//            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
//                requireContext(),
//                android.R.layout.simple_list_item_1,
//                DummyGenerator.generateEmailAddresses()
//            );
//            spinner.setAdapter(arrayAdapter);
//            final PopupWindow popupWindow = new PopupWindow(
//                contentView,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                true
//            );
//            popupWindow.showAsDropDown(v);
////            registerForContextMenu(v);
////            final PopupMenu popupMenu = new PopupMenu(requireContext(), v);
////            popupMenu.getMenuInflater().inflate(R.menu.menu_list, popupMenu.getMenu());
////            popupMenu.show();
//        });

        // Observe LiveData to update UI state
        viewModel.getMeetings().observe(getViewLifecycleOwner(), adapter::submitList);
        viewModel.getTextVisibilityState().observe(getViewLifecycleOwner(), noMeetingTxt::setVisibility);

        // Observe LiveData to trigger UI events
        viewModel.getDropDownVisibility().observe(getViewLifecycleOwner(), dropDownVisibility -> {
            if (dropDownVisibility) {
                placeFilterInput.showDropDown();
            } else {
                placeFilterInput.dismissDropDown();
            }
        });
        viewModel.getShowDialogEvent().observe(getViewLifecycleOwner(), aVoid -> {
            filterDialog.show();
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_filter, menu);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo
    ) {
        requireActivity().getMenuInflater().inflate(R.menu.menu_filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter) {
            Log.d("Neige", "ListFragment::onOptionsItemSelected: filter");
//            viewModel.onFilterClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Log.d("Neige", "ListFragment::onContextItemSelected: filter");
        if (item.getItemId() == R.id.filter) {
            Log.d("Neige", "ListFragment::onContextItemSelected: filter");
//            viewModel.onFilterClicked();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    // -------------------------------------- PRIVATE METHODS --------------------------------------

    @NonNull
    private MeetingAdapter configRecyclerView(@NonNull ListViewModel viewModel) {
        final MeetingAdapter meetingAdapter = new MeetingAdapter(viewModel::onRemoveMeeting);
        ((RecyclerView) requireView().findViewById(R.id.list_meeting)).setAdapter(meetingAdapter);
        return meetingAdapter;
    }

    private void configFab(@NonNull ListViewModel viewModel) {
        final View fab = requireView().findViewById(R.id.add_meeting);
        fab.setOnClickListener(v -> startActivity(new Intent(requireActivity(), AddActivity.class)));
        fab.setOnLongClickListener(v -> {
            viewModel.onGenerateDummyList();
            return true;
        });
    }

    @NonNull
    private AlertDialog initDialog() {
        return new AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_fitler)
            .setPositiveButton(R.string.ok_button, null)
            .setNegativeButton(R.string.cancel_button, null)
            .setNeutralButton(R.string.reset_button, null)
            .create();
    }

    @NonNull
    private AutoCompleteTextView configDateFilter(@NonNull ListViewModel viewModel) {
        final AutoCompleteTextView dateFilterInput = requireView().findViewById(R.id.date_filter_input);
        return dateFilterInput;
    }

    @NonNull
    private AutoCompleteTextView configTimeFilter(@NonNull ListViewModel viewModel) {
        final AutoCompleteTextView timeFilterInput = requireView().findViewById(R.id.time_filter_input);
        return timeFilterInput;
    }

    @NonNull
    private AutoCompleteTextView configPlaceFilter(@NonNull ListViewModel viewModel) {
        final AutoCompleteTextView placeFilterInput = requireView().findViewById(R.id.place_filter_input);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_list_item_multiple_choice,
            DummyGenerator.generateMeetingPlaces()
        );
        placeFilterInput.setAdapter(arrayAdapter);
        placeFilterInput.setDropDownWidth(500); // TODO: better wrap_content than fixed
        placeFilterInput.setOnItemClickListener((parent, view, position, id) -> {
            final CheckedTextView checkedTextView = (CheckedTextView) view;
            Log.d("Neige", "ListFragment::configPlaceFilter: " + checkedTextView.isChecked());
            ((CheckedTextView) view).setChecked(true);
            arrayAdapter.notifyDataSetChanged();
            Log.d("Neige", "ListFragment::configPlaceFilter: " + checkedTextView.isChecked());
        });
        return placeFilterInput;
    }

    @NonNull
    private AutoCompleteTextView configMemberFilter(@NonNull ListViewModel viewModel) {
        final List<String> names = DummyGenerator.generateEmailAddresses();
        for (int i = 0; i < names.size(); i++) {
            final String name = names.get(i);
            names.set(i, name.substring(0, name.indexOf('@'))); // Keep only the name
        }
        final AutoCompleteTextView memberFilterInput = requireView().findViewById(R.id.member_filter_input);
        memberFilterInput.setAdapter(new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_list_item_multiple_choice,
            names
        ));
        memberFilterInput.setDropDownWidth(500);
        return memberFilterInput;
    }
}