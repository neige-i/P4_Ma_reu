package com.neige_i.mareu.data.model;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Filters {

    private final LocalDate fromDate;
    private final LocalDate untilDate;
    private final LocalTime fromTime;
    private final LocalTime untilTime;
    private final List<String> places;
    private final List<String> members;

    public Filters() {
        fromDate = null;
        untilDate = null;
        fromTime = null;
        untilTime = null;
        places = new ArrayList<>();
        members = new ArrayList<>();
    }

    public Filters(LocalDate fromDate, LocalDate untilDate, LocalTime fromTime,
                   LocalTime untilTime,
                   List<String> places,
                   List<String> members
    ) {
        this.fromDate = fromDate;
        this.untilDate = untilDate;
        this.fromTime = fromTime;
        this.untilTime = untilTime;
        this.places = places;
        this.members = members;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getUntilDate() {
        return untilDate;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public LocalTime getUntilTime() {
        return untilTime;
    }

    public List<String> getPlaces() {
        return places;
    }

    public List<String> getMembers() {
        return members;
    }

    public static class Builder {

        private LocalDate fromDate;
        private LocalDate untilDate;
        private LocalTime fromTime;
        private LocalTime untilTime;
        private List<String> places;
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