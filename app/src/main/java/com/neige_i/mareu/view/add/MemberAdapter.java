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
import com.neige_i.mareu.data.DI;
import com.neige_i.mareu.view.add.model.MemberUi;

public class MemberAdapter extends ListAdapter<MemberUi, MemberAdapter.MemberViewHolder> {

    @NonNull
    private final OnMemberChangedListener onMemberChangedListener;

    protected MemberAdapter(@NonNull OnMemberChangedListener onMemberChangedListener) {
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
        holder.bind(getItem(position), onMemberChangedListener);
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {

        final AutoCompleteTextView email;
        final TextInputLayout emailLayout;
        final ImageView removeButton;

        public MemberViewHolder(@NonNull View itemView, @NonNull OnMemberChangedListener onMemberChangedListener) {
            super(itemView);

            // Config email
            email = itemView.findViewById(R.id.email_input);
            email.setAdapter(new ArrayAdapter<>(
                itemView.getContext(),
                android.R.layout.simple_list_item_1,
                DI.getAvailableMembers()
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

        void bind(@NonNull MemberUi memberUi, @NonNull OnMemberChangedListener onMemberChangedListener) {
            Log.d("Nino", "bind() called with: memberUi = [" + memberUi + "]");
            email.setText(memberUi.getEmail(), false);
            emailLayout.setError(memberUi.getEmailError());
            removeButton.setVisibility(memberUi.getRemoveButtonVisibility());
            removeButton.setOnClickListener(v -> onMemberChangedListener.onRemoveMember(memberUi));
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
        void onRemoveMember(@NonNull MemberUi memberUi);
        void onEmailChosen(int position, @NonNull String email);
    }
}
