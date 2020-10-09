package com.neige_i.mareu.view.add;

import android.util.Log;
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
import com.neige_i.mareu.view.model.MemberUi;

import java.util.List;

public class MemberAdapter extends ListAdapter<MemberUi, MemberAdapter.MemberViewHolder> {

    private final List<String> dropDown;
    private final OnMemberChangedListener onMemberChangedListener;

    protected MemberAdapter(List<String> dropDown, OnMemberChangedListener onMemberChangedListener) {
        super(new MemberDiffCallback());
        this.dropDown = dropDown;
        this.onMemberChangedListener = onMemberChangedListener;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_member,
                parent,
                false
        ), onMemberChangedListener, dropDown);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        holder.bind(getItem(position), onMemberChangedListener);
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {

        final AutoCompleteTextView email;
        final TextInputLayout emailLayout;
        final ImageView removeButton;

        public MemberViewHolder(View itemView, OnMemberChangedListener onMemberChangedListener, List<String> dropDown) {
            super(itemView);

            // Config email
            email = itemView.findViewById(R.id.email_input);
            email.setAdapter(new ArrayAdapter<>(
                    itemView.getContext(),
                    android.R.layout.simple_list_item_1,
                    dropDown
            ));
            email.setOnItemClickListener((parent, view, position, id) ->
                    onMemberChangedListener.onEmailChosen(getAdapterPosition(), parent.getItemAtPosition(position).toString()));

            // Config error
            emailLayout = itemView.findViewById(R.id.email_layout);

            // Config buttons
            removeButton = itemView.findViewById(R.id.remove_member);
            itemView.findViewById(R.id.add_member).setOnClickListener(v ->
                    onMemberChangedListener.onAddMember(getAdapterPosition()));
        }

        void bind(MemberUi memberUi, OnMemberChangedListener onMemberChangedListener) {
            Log.d("Nino", "bind() called with: memberUi = [" + memberUi + "]");
            email.setText(memberUi.getEmail(), false);
            emailLayout.setError(memberUi.getEmailError());
            removeButton.setVisibility(memberUi.getRemoveButtonVisibility());
            removeButton.setOnClickListener(v ->
                                                onMemberChangedListener.onRemoveMember(memberUi));
        }
    }

    private static class MemberDiffCallback extends DiffUtil.ItemCallback<MemberUi> {

        @Override
        public boolean areItemsTheSame(@NonNull MemberUi oldItem, @NonNull MemberUi newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MemberUi oldItem, @NonNull MemberUi newItem) {
            return oldItem.equals(newItem);
        }
    }

    interface OnMemberChangedListener {
        void onAddMember(int position);
        void onRemoveMember(MemberUi memberUi);
        void onEmailChosen(int position, String email);
    }
}
