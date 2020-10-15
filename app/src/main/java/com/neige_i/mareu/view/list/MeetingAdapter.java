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
import com.neige_i.mareu.data.model.Meeting;

import static com.neige_i.mareu.view.util.Util.DATE_FORMAT;
import static com.neige_i.mareu.view.util.Util.TIME_FORMAT;

public class MeetingAdapter extends ListAdapter<Meeting, MeetingAdapter.MeetingViewHolder> {

    protected MeetingAdapter() {
        super(new MeetingDiffCallback());
    }

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
        holder.bind(getItem(position));
    }

    static class MeetingViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView topicAndPlace;
        private final TextView dateAndTime;
        private final TextView members;

        public MeetingViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            topicAndPlace = itemView.findViewById(R.id.topic_and_place);
            dateAndTime = itemView.findViewById(R.id.date_and_time);
            members = itemView.findViewById(R.id.members);
        }

        void bind(Meeting meeting) {
            topicAndPlace.setText(itemView.getResources().getString(
                R.string.topic_and_place,
                meeting.getTopic(),
                meeting.getPlace()
            ));
            dateAndTime.setText(itemView.getResources().getString(
                R.string.start_and_end_time,
                meeting.getStartDateTime().toLocalDate().format(DATE_FORMAT),
                meeting.getStartDateTime().toLocalTime().format(TIME_FORMAT),
                meeting.getEndDateTime().toLocalTime().format(TIME_FORMAT)
            ));
            members.setText(meeting.getEmailList().toString());
        }
    }

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
}
