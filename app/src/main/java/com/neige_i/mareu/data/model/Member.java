package com.neige_i.mareu.data.model;

import androidx.annotation.NonNull;

public class Member {

    private static int memberId = 0;
    private final int id;
    @NonNull
    private final String email;

    public Member(int id, @NonNull String email) {
        this.id = id;
        this.email = email;
    }

    public Member() {
        id = memberId++;
        email = "";
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }
}
