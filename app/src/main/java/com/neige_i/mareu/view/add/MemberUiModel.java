package com.neige_i.mareu.view.add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Model UI for {@link com.neige_i.mareu.R.layout#list_item_meeting list_item_meeting} layout.
 */
public class MemberUiModel {

    // ---------- CLASS VARIABLE

    private static int memberId = 0;

    // ---------- INSTANCE VARIABLE

    private final int id; // Member's unique id
    @NonNull
    private final String email; // TextInputEditText's content
    @Nullable
    private final String emailError; // TextInputLayout's error message
    private final int removeButtonVisibility; // ImageView's visibility

    // ---------- CONSTRUCTOR

    /**
     * Initializes a Member with a new id.
     */
    public MemberUiModel(@NonNull String email, @Nullable String emailError, int removeButtonVisibility) {
        this(memberId++, email, emailError, removeButtonVisibility);
    }

    /**
     * Constructor used to set members' properties.
     */
    public MemberUiModel(int id, @NonNull String email, @Nullable String emailError, int removeButtonVisibility) {
        this.id = id;
        this.email = email;
        this.emailError = emailError;
        this.removeButtonVisibility = removeButtonVisibility;
    }

    // ---------- GETTER

    public int getId() {
        return id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @Nullable
    public String getEmailError() {
        return emailError;
    }

    public int getRemoveButtonVisibility() {
        return removeButtonVisibility;
    }

    // ---------- OVERRIDDEN OBJECT's METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberUiModel memberUiModel = (MemberUiModel) o;
        return id == memberUiModel.id &&
            removeButtonVisibility == memberUiModel.removeButtonVisibility &&
            Objects.equals(email, memberUiModel.email) &&
            Objects.equals(emailError, memberUiModel.emailError);
    }

    @NonNull
    @Override
    public String toString() {
        return "MemberUi{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", errorMessage='" + emailError + '\'' +
            ", removeButtonVisibility=" + removeButtonVisibility +
            '}';
    }
}