package com.neige_i.mareu.data;

import androidx.annotation.NonNull;

import java.time.Clock;

public class DI {

    // ----------------------------------------- VARIABLES -----------------------------------------

    @NonNull
    private static final MeetingRepository repository = new MeetingRepositoryImpl();

    // ------------------------------------- INJECTION METHODS -------------------------------------

    @NonNull
    public static MeetingRepository getRepository() {
        return repository;
    }

    @NonNull
    public static Clock getClock() {
        return Clock.systemDefaultZone();
    }
}
