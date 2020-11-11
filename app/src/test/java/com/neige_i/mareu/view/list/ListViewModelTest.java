package com.neige_i.mareu.view.list;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.DI;
import com.neige_i.mareu.data.repository.ListingRepository;
import com.neige_i.mareu.data.model.Filters;
import com.neige_i.mareu.view.list.view_model.ListViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

@RunWith(JUnitParamsRunner.class)
public class ListViewModelTest {

    // ------------------------------------ TEST RULE VARIABLE -------------------------------------

    // Avoid error when MutableLiveData.setValue() is called in source code
    @Rule
    public final TestRule rule = new InstantTaskExecutorRule();

    // -------------------------- VARIABLES USED TO INITIALIZE VIEW MODEL --------------------------

    @NonNull
    private final ListingRepository listingRepository = DI.getListingRepository();
    @NonNull
    private static final LocalDate defaultDate = LocalDate.of(2020, 9, 20);
    @NonNull
    private static final LocalTime defaultTime = LocalTime.of(20, 10);

    // ------------------------------------- OBJECT UNDER TEST -------------------------------------

    private ListViewModel viewModel;

    // ---------------------------------------- SETUP TEST -----------------------------------------

    @Before
    public void setUp() {
        // When: init ViewModel
        viewModel = new ListViewModel(
            listingRepository,
            Clock.fixed(
                ZonedDateTime.of(defaultDate, defaultTime, ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
            )
        );

        // Then: the meeting repository variables are updated
        assertThat(listingRepository.getAllMeetings().getValue(), is(empty()));
        assertThat(listingRepository.getFilters().getValue(), is(new Filters()));
    }

    // ------------------------------------ DRAWER LAYOUT TEST -------------------------------------

    @Test
    public void toggleDrawerLayoutVisibility_setLiveDataValue() {
        // Given: nothing

        // When: toggle the drawer layout
        viewModel.toggleDrawerLayoutVisibility();

        // Then: the drawer layout's state is in 'end' position
        assertThat(viewModel.getDrawerLayoutState().getValue(), is(R.id.end));

        // When: toggle the drawer layout again
        viewModel.toggleDrawerLayoutVisibility();

        // Then: the drawer layout's state is in 'start' position
        assertThat(viewModel.getDrawerLayoutState().getValue(), is(R.id.start));
    }

    // ------------------------------------- DATE & TIME TEST --------------------------------------

    @Test
    @Parameters(method = "filtersValues")
    public void clearDateTimeField_setLiveDataValue(int selectedInputId, Filters expectedFilters) {
        // Given: set the filter for dates and times
        listingRepository.setFrom(LocalDate.of(2020, 11, 8));
        listingRepository.setUntil(LocalDate.of(2020, 11, 8));
        listingRepository.setFrom(LocalTime.of(15, 30));
        listingRepository.setUntil(LocalTime.of(18, 30));

        // When: clear the filter with the specified id
        viewModel.clearDateTimeField(selectedInputId);

        // Then: the repository's filter value is updated
        assertThat(listingRepository.getFilters().getValue(), is(expectedFilters));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] filtersValues() {
        return new Object[]{
            new Object[]{
                R.id.start_date_filter_layout, // When: the start date filter is cleared
                new Filters(
                    null, // Then: the start date filter is removed
                    LocalDate.of(2020, 11, 8),
                    LocalTime.of(15, 30),
                    LocalTime.of(18, 30),
                    new ArrayList<>(),
                    new ArrayList<>()
                )
            },
            new Object[]{
                R.id.end_date_filter_layout, // When: the end date filter is cleared
                new Filters(
                    LocalDate.of(2020, 11, 8),
                    null, // Then: the end date filter is removed
                    LocalTime.of(15, 30),
                    LocalTime.of(18, 30),
                    new ArrayList<>(),
                    new ArrayList<>()
                )
            },
            new Object[]{
                R.id.start_time_filter_layout, // When: the start time filter is cleared
                new Filters(
                    LocalDate.of(2020, 11, 8),
                    LocalDate.of(2020, 11, 8),
                    null, // Then: the start time filter is removed
                    LocalTime.of(18, 30),
                    new ArrayList<>(),
                    new ArrayList<>()
                )
            },
            new Object[]{
                R.id.end_time_filter_layout, // When: the end time filter is cleared
                new Filters(
                    LocalDate.of(2020, 11, 8),
                    LocalDate.of(2020, 11, 8),
                    LocalTime.of(15, 30),
                    null, // Then: the end time filter is removed
                    new ArrayList<>(),
                    new ArrayList<>()
                )
            },
        };
    }

    // ------------------------------------ PLACE & EMAIL TEST -------------------------------------

    @Test
    public void setPlaceFilter_setLiveDataValue() {
        // Given: nothing

        // When: select the 'Mario' place filter
        viewModel.setPlaceFilter("Mario", true);

        // Then: the repository's place filter is updated
        assertThat(listingRepository.getFilters().getValue().getPlaces(), is(Collections.singletonList("Mario")));

        // When: un-select the 'Mario' place filter
        viewModel.setPlaceFilter("Mario", false);

        // Then: the repository's place filter is updated
        assertThat(listingRepository.getFilters().getValue().getPlaces(), is(empty()));
    }

    @Test
    public void setEmailFilter_setLiveDataValue() {
        // Given: nothing

        // When: select the 'maxime' member filter
        viewModel.setEmailFilter("maxime@lamzone.com", true);

        // Then: the repository's member filter is updated
        assertThat(listingRepository.getFilters().getValue().getEmails(), is(Collections.singletonList("maxime@lamzone.com")));

        // When: un-select the 'maxime' member filter
        viewModel.setEmailFilter("maxime@lamzone.com", false);

        // Then: the repository's member filter is updated
        assertThat(listingRepository.getFilters().getValue().getEmails(), is(empty()));
    }
}