package com.neige_i.mareu;

import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.lifecycle.MutableLiveData;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Util {

    // -------------------------------------  CLASS VARIABLES --------------------------------------

    /**
     * Pattern used to format from and parse to a {@link java.time.LocalDate LocalDate}.
     */
    @NonNull
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Pattern used to format from and parse to a {@link java.time.LocalTime LocalTime}.
     */
    @NonNull
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Represents the absence of error of a {@link com.google.android.material.textfield.TextInputLayout TextInputLayout}.
     */
    public static final int NO_ERROR = 0;

    // --------------------------------------  CLASS METHODS ---------------------------------------

    /**
     * Sets the text of the specified {@link AutoCompleteTextView} without filtering to keep the
     * drop-down list unchanged.
     */
    public static void setTextWithoutFilter(@NonNull AutoCompleteTextView input, @NonNull String text) {
        input.setText(text, false); // TIPS: disable filtering
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void updateLiveData(MutableLiveData liveData) {
        liveData.setValue(liveData.getValue());
    }

    /**
     * Returns a String representation of the specified Temporal, or an empty String if it is null.
     */
    @NonNull
    public static String formatDateTime(@Nullable Temporal dateTime) {
        if (dateTime == null)
            return "";
        else if (dateTime instanceof LocalDate)
            return ((LocalDate) dateTime).format(DATE_FORMAT);
        else if (dateTime instanceof LocalTime)
            return ((LocalTime) dateTime).format(TIME_FORMAT);
        else
            throw new IllegalArgumentException();
    }

    /**
     * Removes the error if it is a 'required' one and the field is not empty anymore.
     */
    public static int removeRequiredError(@StringRes int errorMessage, @NonNull String field) {
        return errorMessage == R.string.required_field_error && !field.trim().isEmpty() ? NO_ERROR : errorMessage;
    }

    /**
     * Removes the error if it is a 'required' one and the field is not null anymore.
     */
    public static int removeRequiredError(@StringRes int errorMessage, @Nullable Temporal field) {
        return errorMessage == R.string.required_field_error && field != null ? NO_ERROR : errorMessage;
    }

    /**
     * Returns a list of names from a list of emails.
     */
    @NonNull
    public static List<String> getNames(@NonNull List<String> emails) {
        return emails.stream().map(s -> s.substring(0, s.indexOf("@"))).collect(Collectors.toList());
    }
}
