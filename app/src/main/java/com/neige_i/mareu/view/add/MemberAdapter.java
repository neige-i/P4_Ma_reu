package com.neige_i.mareu.view.add;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.view.model.MemberUi;

public class MemberAdapter extends ListAdapter<MemberUi, MemberAdapter.MemberViewHolder> {

    private final OnMemberChangedListener onMemberChangedListener;

    protected MemberAdapter(OnMemberChangedListener onMemberChangedListener) {
        super(new MemberDiffCallback());
        this.onMemberChangedListener = onMemberChangedListener;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_member,
                parent,
                false
        ), onMemberChangedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {

        final AutoCompleteTextView email;
        final TextInputLayout emailLayout;
        final ImageView removeButton;

        public MemberViewHolder(View itemView, OnMemberChangedListener onMemberChangedListener) {
            super(itemView);

            // Config email
            email = itemView.findViewById(R.id.email_input);
            email.setAdapter(new ArrayAdapter<>(
                    itemView.getContext(),
                    android.R.layout.simple_list_item_1,
                    DummyGenerator.generateEmailAddresses()
            ));
            email.setOnItemClickListener((parent, view, position, id) ->
                    onMemberChangedListener.onEmailChosen(getAdapterPosition(), parent.getItemAtPosition(position).toString()));

            // Config error
            emailLayout = itemView.findViewById(R.id.email_layout);

            // Config buttons
            itemView.findViewById(R.id.add_member).setOnClickListener(v ->
                    onMemberChangedListener.onAddMember(getAdapterPosition()));
            removeButton = itemView.findViewById(R.id.remove_member);
            removeButton.setOnClickListener(v ->
                    onMemberChangedListener.onRemoveMember(getAdapterPosition()));
        }

        void bind(MemberUi memberUi) {
            email.setText(memberUi.getEmail());
            emailLayout.setError(memberUi.getErrorMessage());
            removeButton.setVisibility(memberUi.getRemoveButtonVisibility());
        }
    }

    private static class MemberDiffCallback extends DiffUtil.ItemCallback<MemberUi> {

        @Override
        public boolean areItemsTheSame(@NonNull MemberUi oldItem, @NonNull MemberUi newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull MemberUi oldItem, @NonNull MemberUi newItem) {
            return oldItem.equals(newItem);
        }
    }

    interface OnMemberChangedListener {
        void onAddMember(int position);
        void onRemoveMember(int position);
        void onEmailChosen(int position, String email);
    }
}
