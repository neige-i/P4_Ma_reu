package com.neige_i.mareu.view.add;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DI;
import com.neige_i.mareu.view.add.ui_model.MemberUiModel;

import java.util.ArrayList;
import java.util.List;

import static com.neige_i.mareu.Util.setTextWithoutFilter;

public class MemberUiAdapter extends RecyclerView.Adapter<MemberUiAdapter.MemberUiViewHolder> {

    // ------------------------------------  CALLBACK VARIABLES ------------------------------------

    @NonNull
    private final OnMemberChangedListener onMemberChangedListener;

    @NonNull
    private List<MemberUiModel> memberUiModels = new ArrayList<>();

    // ----------------------------------- CONSTRUCTOR & SETTER ------------------------------------

    public MemberUiAdapter(@NonNull MemberUiAdapter.OnMemberChangedListener onMemberChangedListener) {
        this.onMemberChangedListener = onMemberChangedListener;
    }

    public void setNewList(@NonNull List<MemberUiModel> memberUiModels) {
        this.memberUiModels = memberUiModels;
        notifyDataSetChanged();
    }

    // -------------------------------------- ADAPTER METHODS --------------------------------------

    @NonNull
    @Override
    public MemberUiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberUiViewHolder(LayoutInflater.from(parent.getContext()).inflate(
            R.layout.list_item_member,
            parent,
            false
        ), onMemberChangedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberUiViewHolder holder, int position) {
        final MemberUiModel uiModel = memberUiModels.get(position);
        setTextWithoutFilter(holder.email, uiModel.getEmail());
        holder.emailLayout.setError(uiModel.getEmailError(holder.itemView.getContext()));
        holder.addButton.setVisibility(uiModel.getAddButtonVisibility());
        holder.removeButton.setVisibility(uiModel.getRemoveButtonVisibility());
    }

    @Override
    public int getItemCount() {
        return memberUiModels.size();
    }

    // ------------------------------------- VIEW HOLDER CLASS -------------------------------------

    public static class MemberUiViewHolder extends RecyclerView.ViewHolder {

        private final AutoCompleteTextView email;
        private final TextInputLayout emailLayout;
        private final ImageView addButton;
        private final ImageView removeButton;

        public MemberUiViewHolder(@NonNull View itemView, @NonNull OnMemberChangedListener onMemberChangedListener) {
            super(itemView);

            // Config email
            email = itemView.findViewById(R.id.email_input);
            email.setAdapter(new ArrayAdapter<>(
                itemView.getContext(),
                android.R.layout.simple_list_item_1,
                DI.getMeetingRepository().getAvailableMembers()
            ));
            email.setOnItemClickListener((parent, view, position, id) -> onMemberChangedListener.onEmailChosen(
                getAdapterPosition(),
                parent.getItemAtPosition(position).toString()
            ));

            // Config error
            emailLayout = itemView.findViewById(R.id.email_layout);

            // Config buttons
            addButton = itemView.findViewById(R.id.add_member);
            addButton.setOnClickListener(v -> onMemberChangedListener.onAddMember(getAdapterPosition()));
            removeButton = itemView.findViewById(R.id.remove_member);
            removeButton.setOnClickListener(v -> onMemberChangedListener.onRemoveMember(getAdapterPosition()));
        }
    }

    // ------------------------------------ CALLBACK INTERFACE -------------------------------------

    interface OnMemberChangedListener {
        /**
         * Called when a member must be added.
         */
        void onAddMember(int position);

        /**
         * Called when a member must be updated.
         */
        void onEmailChosen(int position, @NonNull String email);

        /**
         * Called when a member must be removed.
         */
        void onRemoveMember(int position);
    }
}
