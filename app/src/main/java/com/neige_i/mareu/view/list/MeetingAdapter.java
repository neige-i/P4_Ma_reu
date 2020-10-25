package com.neige_i.mareu.view.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DummyGenerator;
import com.neige_i.mareu.data.model.Meeting;

import static com.neige_i.mareu.view.util.Util.DATE_FORMAT;
import static com.neige_i.mareu.view.util.Util.TIME_FORMAT;

public class MeetingAdapter extends ListAdapter<Meeting, MeetingAdapter.MeetingViewHolder> {

    // ------------------------------------  INSTANCE VARIABLES ------------------------------------

    @NonNull
    private final OnMeetingChangedListener onMeetingChangedListener;

    // ---------------------------------------- CONSTRUCTOR ----------------------------------------

    protected MeetingAdapter(@NonNull OnMeetingChangedListener onMeetingChangedListener) {
        super(new MeetingDiffCallback());
        this.onMeetingChangedListener = onMeetingChangedListener;
    }

    // ------------------------------------ OVERRIDDEN METHODS -------------------------------------

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MeetingViewHolder(LayoutInflater.from(parent.getContext()).inflate(
            R.layout.list_item_meeting,
            parent,
            false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        final Meeting meeting = getItem(position);
        final int placeIndex = DummyGenerator.PLACES.indexOf(meeting.getPlace());
        holder.image.setImageResource(DummyGenerator.LOGOS.get(placeIndex));
        holder.topic.setText(meeting.getTopic());
        holder.dateAndTime.setText(holder.itemView.getResources().getString(
            R.string.start_and_end_time,
            meeting.getStartDateTime().toLocalDate().format(DATE_FORMAT),
            meeting.getStartDateTime().toLocalTime().format(TIME_FORMAT),
            meeting.getEndDateTime().toLocalTime().format(TIME_FORMAT)
        ));
        holder.members.setText(meeting.getEmailList().toString());
        holder.deleteButton.setOnClickListener(v -> onMeetingChangedListener.onRemoveMeeting(meeting.getId()));
    }

    // ------------------------------------- VIEW HOLDER CLASS -------------------------------------

    static class MeetingViewHolder extends RecyclerView.ViewHolder {

        // ----------------------------------  INSTANCE VARIABLES ----------------------------------

        final ImageView image;
        final TextView topic;
        final TextView dateAndTime;
        final TextView members;
        final ImageButton deleteButton;

        // -------------------------------------  CONSTRUCTOR --------------------------------------

        public MeetingViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            topic = itemView.findViewById(R.id.topic);
            dateAndTime = itemView.findViewById(R.id.date_and_time);
            members = itemView.findViewById(R.id.members);
            deleteButton = itemView.findViewById(R.id.delete_meeting);
        }
    }

    // -------------------------------------- DIFF UTIL CLASS --------------------------------------

    private static class MeetingDiffCallback extends DiffUtil.ItemCallback<Meeting> {

        @Override
        public boolean areItemsTheSame(@NonNull Meeting oldItem, @NonNull Meeting newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Meeting oldItem, @NonNull Meeting newItem) {
            return oldItem.equals(newItem);
        }
    }

    // ------------------------------------ CALLBACK INTERFACE -------------------------------------

    interface OnMeetingChangedListener {
        void onRemoveMeeting(int meetingId);
    }
}
