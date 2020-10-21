package com.neige_i.mareu.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.neige_i.mareu.view.add.model.MeetingUi;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

public class DI {

    // ---------- VARIABLE

    @NonNull
    private static final MeetingRepository repository = new MeetingRepositoryImpl();
    @NonNull
    private static final List<String> availableMembers = new ArrayList<>(DummyGenerator.EMAILS);

    // ---------- INJECTION METHOD

    @NonNull
    public static MeetingRepository getRepository() {
        return repository;
    }

    @NonNull
    public static Clock getClock() {
        return Clock.systemDefaultZone();
    }

    // ASKME: inside repository

    @NonNull
    public static List<String> getAvailableMembers() {
        return availableMembers;
    }

    @NonNull
    public static MutableLiveData<MeetingUi> getMeetingUi() {
        // ASKME: allow logic here
        availableMembers.clear();
        availableMembers.addAll(DummyGenerator.EMAILS);
        return new MutableLiveData<>(new MeetingUi());
    }
}
