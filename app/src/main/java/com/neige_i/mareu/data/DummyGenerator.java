package com.neige_i.mareu.data;

import java.util.Arrays;
import java.util.List;

public abstract class DummyGenerator {

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
}
