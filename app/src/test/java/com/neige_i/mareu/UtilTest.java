package com.neige_i.mareu;

import androidx.annotation.NonNull;

import com.neige_i.mareu.data.DummyGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.Nullable;

import static com.neige_i.mareu.Util.NO_ERROR;
import static com.neige_i.mareu.Util.getNames;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(JUnitParamsRunner.class)
public class UtilTest {

    @Test
    @Parameters(method = "dateTimeValues")
    public void formatDateTime_returnAppropriateString(@Nullable Temporal dateTime, @NonNull String expectedDateTime) {
        assertThat(Util.formatDateTime(dateTime), is(expectedDateTime));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] dateTimeValues() {
        return new Object[]{
            // Given: a null Temporal
            // Then: the Date/Time format is an empty String
            new Object[]{null, ""},

            // Given: a non-null Temporal
            // Then: the Date/Time format is a correct String representation of the Temporal
            new Object[]{LocalDate.of(2020, 11, 4), "04/11/2020"},
            new Object[]{LocalTime.of(1, 5), "01:05"},
        };
    }

    @Test
    @Parameters(method = "fieldValues")
    public void removeRequiredError_returnAppropriateError(int initialError, Object field, int expectedError) {
        if (field instanceof String)
            assertThat(Util.removeRequiredError(initialError, (String) field), is(expectedError));
        if (field instanceof Temporal)
            assertThat(Util.removeRequiredError(initialError, (Temporal) field), is(expectedError));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private Object[] fieldValues() {
        return new Object[]{
            // Given: a non-'required' error
            // Then: the error is unchanged whatever the field value
            new Object[]{NO_ERROR, "", NO_ERROR},
            new Object[]{NO_ERROR, "     ", NO_ERROR},
            new Object[]{NO_ERROR, null, NO_ERROR},
            new Object[]{NO_ERROR, "some non-empty value", NO_ERROR},
            new Object[]{NO_ERROR, LocalDate.of(2020, 11, 5), NO_ERROR},
            new Object[]{NO_ERROR, LocalTime.of(12, 45), NO_ERROR},
            new Object[]{R.string.time_place_error, "Mario", R.string.time_place_error},
            new Object[]{R.string.occupied_error, LocalDate.of(2020, 11, 5), R.string.occupied_error},
            new Object[]{R.string.occupied_error, LocalTime.of(12, 45), R.string.occupied_error},

            // Given: a 'required' error
            // Then: remove the error only if the field is a non-empty String or a non-null Temporal
            new Object[]{R.string.required_field_error, "", R.string.required_field_error},
            new Object[]{R.string.required_field_error, "     ", R.string.required_field_error},
            new Object[]{R.string.required_field_error, null, R.string.required_field_error},
            new Object[]{R.string.required_field_error, "some non-empty value", NO_ERROR},
            new Object[]{R.string.required_field_error, LocalDate.of(2020, 11, 5), NO_ERROR},
            new Object[]{R.string.required_field_error, LocalTime.of(12, 45), NO_ERROR},

        };
    }

    @Test
    public void getNames_returnWithoutDomain() {
        // Given: an expected name list
        final List<String> expectedNames = Arrays.asList("maxime", "alex", "paul", "viviane",
                                                         "amandine", "luc", "francis", "alexandra");

        // When: retrieve the names from the email list
        final List<String> actualNames = getNames(DummyGenerator.EMAILS);

        // Then: the actual name list equals the expected one
        assertThat(actualNames, is(expectedNames));
    }
}