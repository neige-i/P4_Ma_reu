package com.neige_i.mareu.view.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DummyGenerator;

import java.util.List;

public class PlaceFilterAdapter extends RecyclerView.Adapter<PlaceFilterAdapter.PlaceViewHolder> {

    @NonNull
    private final List<Integer> imageList;
    @NonNull
    private final OnPlaceFilterCheckedListener listener;

    public PlaceFilterAdapter(@NonNull List<Integer> imageList,
                              @NonNull OnPlaceFilterCheckedListener listener
    ) {
        this.imageList = imageList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaceViewHolder(LayoutInflater.from(parent.getContext()).inflate(
            R.layout.list_item_place_button,
            parent,
            false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.image.setBackgroundResource(imageList.get(position));
        holder.image.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onPlaceChecked(
            DummyGenerator.PLACES.get(position),
            isChecked
        ));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {

        final CheckBox image;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.place_button);
        }
    }

    interface OnPlaceFilterCheckedListener {
        void onPlaceChecked(@NonNull String place, boolean isChecked);
    }
}
