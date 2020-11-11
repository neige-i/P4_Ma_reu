package com.neige_i.mareu.view.add.ui_model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.neige_i.mareu.R;

import java.time.LocalTime;

import static com.neige_i.mareu.Util.NO_ERROR;

public class MemberUiModel {

    // ---------- INSTANCE VARIABLE

    private final int id;
    @NonNull
    private final String email;
    private final int emailError;
    private final int addButtonVisibility;
    private final int removeButtonVisibility;

    private final LocalTime[] sameMemberTimes;

    // ---------- CONSTRUCTOR

    public MemberUiModel(int id, @NonNull String email, int emailError, int addButtonVisibility,
                         int removeButtonVisibility, LocalTime... sameMemberTimes) {
        this.id = id;
        this.email = email;
        this.emailError = emailError;
        this.addButtonVisibility = addButtonVisibility;
        this.removeButtonVisibility = removeButtonVisibility;
        this.sameMemberTimes = sameMemberTimes;
    }

    // ---------- GETTER

    public int getId() {
        return id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public int getEmailErrorId() {
        return emailError;
    }

    @Nullable
    public String getEmailError(@NonNull Context context) {
        return getFieldError(emailError, context);
    }

    public int getAddButtonVisibility() {
        return addButtonVisibility;
    }

    public int getRemoveButtonVisibility() {
        return removeButtonVisibility;
    }

    public LocalTime[] getSameMemberTimes() {
        return sameMemberTimes;
    }

    @Nullable
    private String getFieldError(@StringRes int errorMessage, @NonNull Context context) {
        if (errorMessage == NO_ERROR)
            return null;
        else if (errorMessage == R.string.time_member_error)
            return context.getString(errorMessage, sameMemberTimes[0], sameMemberTimes[1]);
        else
            return context.getString(errorMessage);
    }

    // ---------- OVERRIDDEN OBJECT's METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MemberUiModel memberUiModel = (MemberUiModel) o;
        return id == memberUiModel.id &&
            emailError == memberUiModel.emailError &&
            addButtonVisibility == memberUiModel.addButtonVisibility &&
            removeButtonVisibility == memberUiModel.removeButtonVisibility &&
            email.equals(memberUiModel.email);
    }

    @NonNull
    @Override
    public String toString() {
        return "MemberUi{" +
            id +
            ", '" + email + '\'' +
            ", " + emailError +
            '}';
    }
}
