package com.neige_i.mareu.view.add;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.neige_i.mareu.R;

import java.time.LocalTime;
import java.util.Objects;

import static com.neige_i.mareu.view.util.Util.NO_ERROR;

/**
 * Model UI for {@link com.neige_i.mareu.R.layout#list_item_meeting list_item_meeting} layout.
 */
public class MemberUiModel {

    // ---------- CLASS VARIABLE

    private static int memberId = 0;

    // ---------- INSTANCE VARIABLE

    private final int id;
    @NonNull
    private String email;
    private int emailError;
    private final int addButtonVisibility;
    private final int removeButtonVisibility;

    @NonNull
    private final Application application;

    @Nullable
    private LocalTime[] existingTimes;

    // ---------- CONSTRUCTOR

//    /**
//     * Initializes a Member with a new id.
//     */
//    public MemberUiModel(@NonNull String email, int emailError, int removeButtonVisibility, @NonNull Application application) {
//        this(memberId++, email, emailError, removeButtonVisibility, application);
//    }

    /**
     * Constructor used to set members' properties.
     */
    public MemberUiModel(int id, @NonNull String email, int emailError, int addButtonVisibility, int removeButtonVisibility, @NonNull Application application) {
        this.id = id;
        this.email = email;
        this.emailError = emailError;
        this.addButtonVisibility = addButtonVisibility;
        this.removeButtonVisibility = removeButtonVisibility;
        this.application = application;
    }

    public MemberUiModel(@NonNull String email, @NonNull Application application) {
        this(memberId++, email, NO_ERROR, View.VISIBLE, View.VISIBLE, application);
    }

    // ---------- GETTER

    public int getId() {
        return id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
        emailError = setFieldError(email, emailError);
    }

    public int getEmailErrorId() {
        return emailError;
    }

    @Nullable
    public String getEmailError() {
        return getFieldError(emailError);
    }

    public void setEmailError(int emailError, LocalTime... localTimes) {
        this.emailError = emailError;
        existingTimes = localTimes;
    }

    public int getAddButtonVisibility() {
        return addButtonVisibility;
    }

    public int getRemoveButtonVisibility() {
        return removeButtonVisibility;
    }

    // FIXME: duplicate

    @Nullable
    private String getFieldError(@StringRes int errorMessage) {
        switch (errorMessage) {
            case NO_ERROR:
                return null;
            case R.string.time_member_error:
                return application.getString(errorMessage, existingTimes);
            default:
                return application.getString(errorMessage);
        }
    }

    private int setFieldError(@NonNull String field, @StringRes int errorMessage) {
        if (errorMessage == R.string.empty_field_error && !field.isEmpty())
            return NO_ERROR; // Remove error
        else
            return errorMessage; // Stay unchanged
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
            "id=" + id +
            ", email='" + email + '\'' +
            ", errorMessage=" + emailError +
            ", add=" + addButtonVisibility +
            ", remove=" + removeButtonVisibility +
            '}';
    }
}
