package com.neige_i.mareu.data;

import androidx.annotation.NonNull;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.model.Meeting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DummyGenerator {

    @NonNull
    public static List<String> PLACES = Arrays.asList(
        "Mario",
        "Luigi",
        "Peach",
        "Toad",
        "Yoshi",
        "Donkey Kong",
        "Bowser",
        "Wario",
        "Waluigi",
        "King Boo"
    );

    @NonNull
    public static List<Integer> LOGOS = Arrays.asList(
        R.mipmap.ic_logo_mario,
        R.mipmap.ic_logo_luigi,
        R.mipmap.ic_logo_peach,
        R.mipmap.ic_logo_toad,
        R.mipmap.ic_logo_yoshi,
        R.mipmap.ic_logo_donkey_kong,
        R.mipmap.ic_logo_bowser,
        R.mipmap.ic_logo_wario,
        R.mipmap.ic_logo_waluigi,
        R.mipmap.ic_logo_king_boo
    );

    @NonNull
    public static List<Integer> CHECKABLE_LOGOS = Arrays.asList(
        R.drawable.ic_mario,
        R.drawable.ic_luigi,
        R.drawable.ic_peach,
        R.drawable.ic_toad,
        R.drawable.ic_yoshi,
        R.drawable.ic_donkey_kong,
        R.drawable.ic_bowser,
        R.drawable.ic_wario,
        R.drawable.ic_waluigi,
        R.drawable.ic_king_boo
    );

    @NonNull
    public static List<String> EMAILS = Arrays.asList(
        "maxime@lamzone.com",
        "alex@lamzone.com",
        "paul@lamzone.com",
        "viviane@lamzone.com",
        "amandine@lamzone.com",
        "luc@lamzone.com",
        "francis@lamzone.com",
        "alexandra@lamzone.com"
    );

    @NonNull
    public static List<String> getNames() {
        final List<String> nameList = new ArrayList<>();
        for (String email : EMAILS)
            nameList.add(email.substring(0, email.indexOf("@")));
        return nameList;
    }

    @NonNull
    public static List<Meeting> generateMeetings() {
        final LocalDate date = LocalDate.of(2020, 11, 1);
        final LocalTime startTime = LocalTime.of(8, 0);
        final LocalTime endTime = LocalTime.of(9, 0);

        return Arrays.asList(
            new Meeting(
                "Meeting A",
                date,
                startTime,
                endTime,
                PLACES.get(0),
                EMAILS.subList(0, 2)
            ),
            new Meeting(
                "Meeting B",
                date,
                startTime.plusHours(2),
                endTime.plusHours(4),
                PLACES.get(1),
                EMAILS.subList(5, 7)
            ),
            new Meeting(
                "Meeting C",
                date.plusDays(1),
                startTime.plusHours(3),
                endTime.plusHours(3).plusMinutes(30),
                PLACES.get(2),
                EMAILS.subList(7, 8)
            ),
            new Meeting(
                "Meeting D",
                date.plusDays(1),
                startTime.plusHours(6).plusMinutes(45),
                endTime.plusHours(7).plusMinutes(10),
                PLACES.get(3),
                EMAILS.subList(3, 6)
            ),
            new Meeting(
                "Meeting E",
                date.plusDays(3),
                startTime.plusMinutes(20),
                endTime.plusHours(5),
                PLACES.get(4),
                EMAILS.subList(4, 7)
            ),
            new Meeting(
                "Meeting F",
                date.plusDays(5),
                startTime.plusHours(5).plusMinutes(15),
                endTime.plusHours(7).plusMinutes(55),
                PLACES.get(6),
                EMAILS.subList(2, 6)
            )
        );
    }
}
