package com.neige_i.mareu.data;

import androidx.annotation.NonNull;

import com.neige_i.mareu.R;
import com.neige_i.mareu.data.model.Meeting;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public abstract class DummyGenerator {

    @NonNull
    public static List<String> generateMeetingPlaces() {
        return Arrays.asList(
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
    }

    @NonNull
    public static List<Integer> generatePlaceLogos() {
        return Arrays.asList(
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
    }

    @NonNull
    public static List<String> generateEmailAddresses() {
        return Arrays.asList(
            "maxime@lamzone.com",
            "alex@lamzone.com",
            "paul@lamzone.com",
            "viviane@lamzone.com",
            "amandine@lamzone.com",
            "luc@lamzone.com",
            "francis@lamzone.com",
            "alexandra@lamzone.com"
        );
    }

    @NonNull
    public static List<Meeting> generateMeetings() {
        final List<String> placeList = generateMeetingPlaces();
        final List<String> emailList = generateEmailAddresses();
        final LocalDateTime startDateTime = LocalDateTime.of(2020, 10, 16, 8, 0);
        final LocalDateTime endDateTime = LocalDateTime.of(2020, 10, 16, 9, 0);

        return Arrays.asList(
            new Meeting(
                "Meeting A",
                placeList.get(0),
                startDateTime,
                endDateTime,
                emailList.subList(0, 2)
            ),
            new Meeting(
                "Meeting B",
                placeList.get(1),
                startDateTime.plusHours(2),
                endDateTime.plusHours(4),
                emailList.subList(5, 7)
            ),
            new Meeting(
                "Meeting C",
                placeList.get(2),
                startDateTime.plusDays(1).plusHours(3),
                endDateTime.plusDays(1).plusHours(3).plusMinutes(30),
                emailList.subList(7, 8)
            ),
            new Meeting(
                "Meeting D",
                placeList.get(3),
                startDateTime.plusDays(1).plusHours(6).plusMinutes(45),
                endDateTime.plusDays(1).plusHours(7).plusMinutes(10),
                emailList.subList(3, 6)
            ),
            new Meeting(
                "Meeting E",
                placeList.get(4),
                startDateTime.plusDays(3).plusMinutes(20),
                endDateTime.plusDays(3).plusHours(5),
                emailList.subList(4, 7)
            ),
            new Meeting(
                "Meeting F",
                placeList.get(5),
                startDateTime.plusDays(5).plusHours(5).plusMinutes(15),
                endDateTime.plusDays(5).plusHours(7).plusMinutes(55),
                emailList.subList(2, 6)
            )
        );
    }
}
