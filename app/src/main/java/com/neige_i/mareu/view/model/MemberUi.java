package com.neige_i.mareu.view.model;

import android.view.View;

import java.util.Objects;

public class MemberUi {

    private String email = "";
    private String errorMessage;
    private int removeButtonVisibility = View.VISIBLE;

    public MemberUi() {
    }

    public MemberUi(int removeButtonVisibility) {
        this.removeButtonVisibility = removeButtonVisibility;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        if (email != null)
            errorMessage = null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getRemoveButtonVisibility() {
        return removeButtonVisibility;
    }

    public void setRemoveButtonVisibility(int removeButtonVisibility) {
        this.removeButtonVisibility = removeButtonVisibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberUi memberUi = (MemberUi) o;
        return removeButtonVisibility == memberUi.removeButtonVisibility &&
                Objects.equals(email, memberUi.email) &&
                Objects.equals(errorMessage, memberUi.errorMessage);
    }
}
