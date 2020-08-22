package com.neige_i.mareu.view.add;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DummyGenerator;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private OnButtonClickedListener onButtonClickedListener;
    private List<String> memberList;

    public MemberAdapter(OnButtonClickedListener onButtonClickedListener, List<String> memberList) {
        this.onButtonClickedListener = onButtonClickedListener;
        this.memberList = memberList;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_member,
                parent,
                false
        ), onButtonClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        holder.bind(getItemCount());
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {

        ImageView removeButton;

        public MemberViewHolder(View itemView, OnButtonClickedListener onButtonClickedListener) {
            super(itemView);

            // Config place
            ((AutoCompleteTextView) itemView.findViewById(R.id.member_input)).setAdapter(new ArrayAdapter<>(
                    itemView.getContext(),
                    android.R.layout.simple_list_item_1,
                    DummyGenerator.generateEmailAddresses()
            ));

            // Config buttons
            itemView.findViewById(R.id.add_member).setOnClickListener(v ->
                    onButtonClickedListener.onButtonClicked(v.getId(), getAdapterPosition()));
            removeButton = itemView.findViewById(R.id.remove_member);
            removeButton.setOnClickListener(v ->
                    onButtonClickedListener.onButtonClicked(v.getId(), getAdapterPosition()));
        }

        void bind(int itemCount) {
            removeButton.setVisibility(itemCount == 1 ? View.GONE : View.VISIBLE);
        }
    }

    interface OnButtonClickedListener {
        void onButtonClicked(int viewId, int position);
    }
}
