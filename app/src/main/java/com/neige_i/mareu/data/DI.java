package com.neige_i.mareu.data;

import androidx.annotation.NonNull;

import java.time.Clock;

public class DI {

    // ----------------------------------------- VARIABLES -----------------------------------------

    @NonNull
    private static final ListingRepository listingRepository = new ListingRepositoryImpl();
    @NonNull
    private static final MeetingRepository meetingRepository = new MeetingRepositoryImpl();

    // ------------------------------------- INJECTION METHODS -------------------------------------

    @NonNull
    public static ListingRepository getListingRepository() {
        return listingRepository;
    }

    @NonNull
    public static MeetingRepository getMeetingRepository() {
        return meetingRepository;
    }

    @NonNull
    public static Clock getClock() {
        return Clock.systemDefaultZone();
    }
}
