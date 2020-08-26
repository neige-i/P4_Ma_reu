package com.neige_i.mareu.data;

public class DI {

    private static final MeetingRepository repository = new MeetingRepositoryImpl();

    public static MeetingRepository getRepository() {
        return repository;
    }
}
