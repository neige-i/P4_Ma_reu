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

// TODO: duplicate code
public class MemberFilterAdapter extends RecyclerView.Adapter<MemberFilterAdapter.MemberViewHolder> {

    @NonNull
    private final List<String> nameList;
    @NonNull
    private final OnMemberFilterCheckedListener listener;

    public MemberFilterAdapter(@NonNull List<String> nameList,
                               @NonNull OnMemberFilterCheckedListener listener
    ) {
        this.nameList = nameList;
        this.listener = listener;
    }

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

    static class MemberViewHolder extends RecyclerView.ViewHolder {

        final MaterialButton button;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.member_button);
        }
    }

    interface OnMemberFilterCheckedListener {
        void onMemberChecked(@NonNull String email, boolean isChecked);
    }
}
