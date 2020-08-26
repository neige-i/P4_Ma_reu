package com.neige_i.mareu.data;

import com.neige_i.mareu.data.model.Meeting;

import java.util.Arrays;
import java.util.Calendar;
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

    public static List<Meeting> generateMeetings() {
        List<String> placeList = generateMeetingPlaces();
        List<String> emailList = generateEmailAddresses().subList(0, 3);
        Calendar date = Calendar.getInstance();

        return Arrays.asList(
                new Meeting("Réunion A", placeList.get(0), date, emailList),
                new Meeting("Réunion B", placeList.get(1), date, emailList),
                new Meeting("Réunion C", placeList.get(2), date, emailList),
                new Meeting("Réunion D", placeList.get(3), date, emailList),
                new Meeting("Réunion E", placeList.get(4), date, emailList),
                new Meeting("Réunion F", placeList.get(5), date, emailList),
                new Meeting("Réunion G", placeList.get(6), date, emailList),
                new Meeting("Réunion H", placeList.get(7), date, emailList),
                new Meeting("Réunion I", placeList.get(8), date, emailList),
                new Meeting("Réunion J", placeList.get(9), date, emailList),
                new Meeting("Réunion K", placeList.get(0), date, emailList),
                new Meeting("Réunion L", placeList.get(1), date, emailList)
        );
    }
}
