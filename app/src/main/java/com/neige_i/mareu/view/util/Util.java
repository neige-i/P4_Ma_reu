package com.neige_i.mareu.view.util;

import androidx.annotation.NonNull;

import java.time.format.DateTimeFormatter;

public class Util {

    // FIXME: might be local

    @NonNull
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @NonNull
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
}
