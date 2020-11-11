package com.neige_i.mareu.view.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.neige_i.mareu.R;
import com.neige_i.mareu.view.list.ui_model.MeetingUiModel;

// TIPS: use a ListAdapter<T, VH> instead of Adapter<VH> for better UI updates
public class MeetingUiAdapter extends ListAdapter<MeetingUiModel, MeetingUiAdapter.MeetingViewHolder> {

    // ------------------------------------  CALLBACK VARIABLES ------------------------------------

    @NonNull
    private final OnMeetingChangedListener onMeetingChangedListener;

    // ---------------------------------------- CONSTRUCTOR ----------------------------------------

    protected MeetingUiAdapter(@NonNull OnMeetingChangedListener onMeetingChangedListener) {
        super(new MeetingUiDiffCallback());
        this.onMeetingChangedListener = onMeetingChangedListener;
    }

    // -------------------------------------- ADAPTER METHODS --------------------------------------

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MeetingViewHolder(LayoutInflater.from(parent.getContext()).inflate(
            R.layout.list_item_meeting,
            parent,
            false
        ), onMeetingChangedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        final MeetingUiModel uiModel = getItem(position);
        holder.image.setImageResource(uiModel.getImage());
        holder.topic.setText(uiModel.getTopic());
        holder.dateAndTime.setText(holder.itemView.getResources().getString(
            R.string.start_and_end_time,
            uiModel.getDate(),
            uiModel.getStartTime(),
            uiModel.getEndTime()
        ));
        holder.members.setText(uiModel.getMembers());
    }

    // ------------------------------------- VIEW HOLDER CLASS -------------------------------------

    static class MeetingViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView topic;
        private final TextView dateAndTime;
        private final TextView members;

        public MeetingViewHolder(@NonNull View itemView, @NonNull OnMeetingChangedListener onMeetingChangedListener) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            topic = itemView.findViewById(R.id.topic);
            dateAndTime = itemView.findViewById(R.id.date_and_time);
            members = itemView.findViewById(R.id.members);
            itemView.findViewById(R.id.delete_meeting)
                .setOnClickListener(v -> onMeetingChangedListener.onRemoveMeeting(getAdapterPosition()));
        }
    }

    // -------------------------------------- DIFF UTIL CLASS --------------------------------------

    private static class MeetingUiDiffCallback extends DiffUtil.ItemCallback<MeetingUiModel> {

        @Override
        public boolean areItemsTheSame(@NonNull MeetingUiModel oldItem, @NonNull MeetingUiModel newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull MeetingUiModel oldItem, @NonNull MeetingUiModel newItem) {
            return oldItem.equals(newItem);
        }
    }

    // ------------------------------------ CALLBACK INTERFACE -------------------------------------

    interface OnMeetingChangedListener {
        /**
         * Called when a meeting must be removed.
         */
        void onRemoveMeeting(int position);
    }
}
