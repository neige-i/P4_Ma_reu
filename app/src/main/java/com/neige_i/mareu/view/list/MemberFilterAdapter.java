package com.neige_i.mareu.view.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DummyGenerator;

import java.util.List;

public class MemberFilterAdapter extends RecyclerView.Adapter<MemberFilterAdapter.MemberViewHolder> {

    // -------------------------------------- LOCAL VARIABLES --------------------------------------

    @NonNull
    private final List<String> nameList;
    @NonNull
    private final OnMemberFilterCheckedListener listener;

    // ---------------------------------------- CONSTRUCTOR ----------------------------------------

    public MemberFilterAdapter(@NonNull List<String> nameList, @NonNull OnMemberFilterCheckedListener listener) {
        this.nameList = nameList;
        this.listener = listener;
    }

    // -------------------------------------- ADAPTER METHODS --------------------------------------

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberViewHolder(LayoutInflater.from(parent.getContext()).inflate(
            R.layout.list_item_member_button,
            parent,
            false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        holder.button.setText(nameList.get(position));
        holder.button.addOnCheckedChangeListener((button, isChecked) -> listener.onMemberChecked(
            DummyGenerator.EMAILS.get(position),
            isChecked
        ));
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    // ------------------------------------- VIEW HOLDER CLASS -------------------------------------

    static class MemberViewHolder extends RecyclerView.ViewHolder {

        final MaterialButton button;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.member_button);
        }
    }

    // ------------------------------------ CALLBACK INTERFACE -------------------------------------

    interface OnMemberFilterCheckedListener {
        void onMemberChecked(@NonNull String email, boolean isChecked);
    }
}
