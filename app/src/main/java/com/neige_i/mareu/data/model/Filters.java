package com.neige_i.mareu.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Store all the filters that can be applied to the meeting list.
 */
public class Filters {

    // ------------------------------------ INSTANCE VARIABLES -------------------------------------

    /**
     * Filter to only keep meetings that are held from this date.
     */
    @Nullable
    private LocalDate fromDate;
    /**
     * Filter to only keep meetings that are held until this date.
     */
    @Nullable
    private LocalDate untilDate;
    /**
     * Filter to only keep meetings that are held from this time.
     */
    @Nullable
    private LocalTime fromTime;
    /**
     * Filter to only keep meetings that are held until this time.
     */
    @Nullable
    private LocalTime untilTime;
    /**
     * Filter to only keep meetings that are held in these places.
     */
    @NonNull
    private final List<String> places;
    /**
     * Filter to only keep meetings that contain these members.
     */
    @NonNull
    private final List<String> emails;

    // ------------------------------- CONSTRUCTOR & GETTERS/SETTERS -------------------------------

    public Filters() {
        places = new ArrayList<>();
        emails = new ArrayList<>();
    }

    public Filters(@Nullable LocalDate fromDate, @Nullable LocalDate untilDate, @Nullable LocalTime fromTime,
                   @Nullable LocalTime untilTime, @NonNull List<String> places, @NonNull List<String> emails
    ) {
        this.fromDate = fromDate;
        this.untilDate = untilDate;
        this.fromTime = fromTime;
        this.untilTime = untilTime;
        this.places = places;
        this.emails = emails;
    }

    // -------------------------------------- GETTERS/SETTERS --------------------------------------

    @Nullable
    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(@Nullable LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    @Nullable
    public LocalDate getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(@Nullable LocalDate untilDate) {
        this.untilDate = untilDate;
    }

    @Nullable
    public LocalTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(@Nullable LocalTime fromTime) {
        this.fromTime = fromTime;
    }

    @Nullable
    public LocalTime getUntilTime() {
        return untilTime;
    }

    public void setUntilTime(@Nullable LocalTime untilTime) {
        this.untilTime = untilTime;
    }

    @NonNull
    public List<String> getPlaces() {
        return places;
    }

    @NonNull
    public List<String> getEmails() {
        return emails;
    }

    // -------------------------------------- OBJECT METHODS ---------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filters filters = (Filters) o;
        return Objects.equals(fromDate, filters.fromDate) &&
            Objects.equals(untilDate, filters.untilDate) &&
            Objects.equals(fromTime, filters.fromTime) &&
            Objects.equals(untilTime, filters.untilTime) &&
            places.equals(filters.places) &&
            emails.equals(filters.emails);
    }

    @NonNull
    @Override
    public String toString() {
        return "Filters{" +
            fromDate + " -> " + untilDate + ", " +
            fromTime + " -> " + untilTime +
            ", " + places +
            ", " + emails +
            '}';
    }
}
