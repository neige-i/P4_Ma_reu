package com.neige_i.mareu.data;

import androidx.annotation.NonNull;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.model.Meeting;

import java.time.LocalDateTime;
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
        final LocalDateTime startDateTime = LocalDateTime.of(2020, 10, 16, 8, 0);
        final LocalDateTime endDateTime = LocalDateTime.of(2020, 10, 16, 9, 0);

        return Arrays.asList(
            new Meeting(
                "Meeting A",
                PLACES.get(0),
                startDateTime,
                endDateTime,
                EMAILS.subList(0, 2)
            ),
            new Meeting(
                "Meeting B",
                PLACES.get(1),
                startDateTime.plusHours(2),
                endDateTime.plusHours(4),
                EMAILS.subList(5, 7)
            ),
            new Meeting(
                "Meeting C",
                PLACES.get(2),
                startDateTime.plusDays(1).plusHours(3),
                endDateTime.plusDays(1).plusHours(3).plusMinutes(30),
                EMAILS.subList(7, 8)
            ),
            new Meeting(
                "Meeting D",
                PLACES.get(3),
                startDateTime.plusDays(1).plusHours(6).plusMinutes(45),
                endDateTime.plusDays(1).plusHours(7).plusMinutes(10),
                EMAILS.subList(3, 6)
            ),
            new Meeting(
                "Meeting E",
                PLACES.get(4),
                startDateTime.plusDays(3).plusMinutes(20),
                endDateTime.plusDays(3).plusHours(5),
                EMAILS.subList(4, 7)
            ),
            new Meeting(
                "Meeting F",
                PLACES.get(5),
                startDateTime.plusDays(5).plusHours(5).plusMinutes(15),
                endDateTime.plusDays(5).plusHours(7).plusMinutes(55),
                EMAILS.subList(2, 6)
            )
        );
    }
}
