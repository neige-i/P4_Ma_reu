package com.neige_i.mareu.data;

import androidx.annotation.NonNull;

import com.neige_i.mareu.data.repository.ListingRepository;
import com.neige_i.mareu.data.repository.ListingRepositoryImpl;
import com.neige_i.mareu.data.repository.MeetingRepository;
import com.neige_i.mareu.data.repository.MeetingRepositoryImpl;

import java.time.Clock;

public class DI {

    // ----------------------------------------- VARIABLES -----------------------------------------

    @NonNull
    private static final ListingRepository listingRepository = new ListingRepositoryImpl();
    @NonNull
    private static final MeetingRepository meetingRepository = new MeetingRepositoryImpl();
    private static int memberId = 0;

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

    public static int getMemberId() {
        return memberId++;
    }

    public static void setMemberId(int memberId) {
        DI.memberId = memberId;
    }
}
