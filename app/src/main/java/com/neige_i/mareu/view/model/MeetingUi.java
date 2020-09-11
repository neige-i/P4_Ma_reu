package com.neige_i.mareu.view.model;

import java.util.ArrayList;
import java.util.List;

// TODO: use it or not in AddViewModel...?
public class MeetingUi {

    private static final int TOPIC = 0;
    private static final int TIME = 1;
    private static final int DATE = 2;
    private static final int PLACE = 3;
    private static final int MEMBER = 4;

    private final String[] fields = {"", "", "", ""};
    private final List<MemberUi> memberUiList = new ArrayList<>();
    private final String[] errors = new String[5];

//    TODO IBRAHIM Use booleans to tell view there's errors
//    public final boolean hasMeetingNameError;
//    public final boolean hasRoomError;
//    public final boolean hasNameError;

    public String[] getFields() {
        return fields;
    }

    public List<MemberUi> getMemberUiList() {
        return memberUiList;
    }

    public String[] getErrors() {
        return errors;
    }
}
