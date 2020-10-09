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
        private final TextView mainInfo;
        private final TextView members;

        public MeetingViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            mainInfo = itemView.findViewById(R.id.main_info);
            members = itemView.findViewById(R.id.members);
        }

        void bind(Meeting meeting) {
            mainInfo.setText(itemView.getResources().getString(
                    R.string.meeting_main_info,
                    meeting.getTopic(),
                    meeting.getStartDateTime().getHour(),
                    meeting.getStartDateTime().getMinute(),
                    meeting.getPlace()
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
