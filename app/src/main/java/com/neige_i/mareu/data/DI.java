package com.neige_i.mareu.data;

import java.time.Clock;

public class DI {

    private static final MeetingRepository repository = new MeetingRepositoryImpl();

    public static MeetingRepository getRepository() {
        return repository;
    }

    public static Clock getClock() {
        return Clock.systemDefaultZone();
    }
}
