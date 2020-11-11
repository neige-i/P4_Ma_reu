package com.neige_i.mareu.data;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.neige_i.mareu.data.model.Filters;
import com.neige_i.mareu.data.model.Meeting;
import com.neige_i.mareu.data.model.Member;
import com.neige_i.mareu.data.repository.ListingRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import junitparams.JUnitParamsRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

@RunWith(JUnitParamsRunner.class)
public class ListingRepositoryTest {

    // ------------------------------------ TEST RULE VARIABLE -------------------------------------

    // Avoid error when MutableLiveData.setValue() is called in source code
    @Rule
    public final TestRule rule = new InstantTaskExecutorRule();

    // ------------------------------------- OBJECT UNDER TEST -------------------------------------

    @NonNull
    private final ListingRepository listingRepository = DI.getListingRepository();

    // ---------------------------------------- SETUP TEST -----------------------------------------

    @Before
    public void setUp() {
        listingRepository.initRepository();
    }

    // ----------------------------------------- INIT TEST -----------------------------------------

    @Test
    public void initRepository() {
        // Given: nothing

        // When: initialize repository (done in setUp method)

        // Then: repository variables equal the expected ones
        assertThat(listingRepository.getAllMeetings().getValue(), is(empty()));
        assertThat(listingRepository.getFilters().getValue(), is(new Filters()));
    }

    // ------------------------------------- MEETING LIST TEST -------------------------------------

    @Test
    public void addMeeting() {
        // Given: nothing

        // When: add a meeting
        final Meeting meetingToAdd = new Meeting(
            "New topic", LocalDate.of(2020, 11, 8), LocalTime.of(14, 0), LocalTime.of(15, 30), "Mario",
            Collections.singletonList(new Member())
        );
        listingRepository.addMeeting(meetingToAdd);

        // Then: the repository's list value is updated
        assertThat(listingRepository.getAllMeetings().getValue(), is(Collections.singletonList(meetingToAdd)));
    }

    @Test
    public void addMeetings() {
        // Given: nothing

        // When: add a list
        listingRepository.addMeetings(DummyGenerator.generateMeetings());

        // Then: the repository's list value is updated
        assertThat(listingRepository.getAllMeetings().getValue(), is(DummyGenerator.generateMeetings()));
    }

    @Test
    public void deleteMeeting() {
        // Given: a meeting inside the repository's list
        listingRepository.addMeeting(new Meeting());

        // When: remove the meeting
        listingRepository.removeMeeting(0);

        // Then: the repository's list is empty
        assertThat(listingRepository.getAllMeetings().getValue(), is(empty()));
    }
}