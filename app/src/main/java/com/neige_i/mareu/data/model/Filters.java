package com.neige_i.mareu.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Filters {

    // ------------------------------------ INSTANCE VARIABLES -------------------------------------

    @Nullable
    private final LocalDate fromDate;
    @Nullable
    private final LocalDate untilDate;
    @Nullable
    private final LocalTime fromTime;
    @Nullable
    private final LocalTime untilTime;
    @NonNull
    private final List<String> places;
    @NonNull
    private final List<String> members;

    // ----------------------------------- CONSTRUCTOR & GETTERS -----------------------------------

    public Filters() {
        fromDate = null;
        untilDate = null;
        fromTime = null;
        untilTime = null;
        places = new ArrayList<>();
        members = new ArrayList<>();
    }

    public Filters(@Nullable LocalDate fromDate, @Nullable LocalDate untilDate,
                   @Nullable LocalTime fromTime, @Nullable LocalTime untilTime,
                   @NonNull List<String> places, @NonNull List<String> members
    ) {
        this.fromDate = fromDate;
        this.untilDate = untilDate;
        this.fromTime = fromTime;
        this.untilTime = untilTime;
        this.places = places;
        this.members = members;
    }

    @Nullable
    public LocalDate getFromDate() {
        return fromDate;
    }

    @Nullable
    public LocalDate getUntilDate() {
        return untilDate;
    }

    @Nullable
    public LocalTime getFromTime() {
        return fromTime;
    }

    @Nullable
    public LocalTime getUntilTime() {
        return untilTime;
    }

    @NonNull
    public List<String> getPlaces() {
        return places;
    }

    @NonNull
    public List<String> getMembers() {
        return members;
    }

    // --------------------------------------- BUILDER CLASS ---------------------------------------

    public static class Builder {

        @Nullable
        private LocalDate fromDate;
        @Nullable
        private LocalDate untilDate;
        @Nullable
        private LocalTime fromTime;
        @Nullable
        private LocalTime untilTime;
        @NonNull
        private List<String> places;
        @NonNull
        private List<String> members;

        public Builder(@NonNull Filters filters) {
            fromDate = filters.fromDate;
            untilDate = filters.untilDate;
            fromTime = filters.fromTime;
            untilTime = filters.untilTime;
            places = filters.places;
            members = filters.members;
        }

        public Builder setFromDate(LocalDate fromDate) {
            this.fromDate = fromDate;
            return this;
        }

        public Builder setUntilDate(LocalDate untilDate) {
            this.untilDate = untilDate;
            return this;
        }

        public Builder setFromTime(LocalTime fromTime) {
            this.fromTime = fromTime;
            return this;
        }

        public Builder setUntilTime(LocalTime untilTime) {
            this.untilTime = untilTime;
            return this;
        }

        public Builder setPlaces(List<String> places) {
            this.places = places;
            return this;
        }

        public Builder setMembers(List<String> members) {
            this.members = members;
            return this;
        }

        public Filters build() {
            return new Filters(fromDate, untilDate, fromTime, untilTime, places, members);
        }
    }
}
