package com.neige_i.mareu.view.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class MemberUi {

    // TODO IBRAHIM ID A mettre pour fix le probleme d'objets disparaissants

    @NonNull
    private final String email;
    @Nullable
    private final String errorMessage;
    private final int removeButtonVisibility;

    @NonNull
    public String getEmail() {
        return email;
    }

    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }

    public int getRemoveButtonVisibility() {
        return removeButtonVisibility;
    }

    public MemberUi(@NonNull String email, @Nullable String errorMessage, int removeButtonVisibility) {
        this.email = email;
        this.errorMessage = errorMessage;
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

    @Override
    public String toString() {
        return "MemberUi{" +
            "email='" + email + '\'' +
            ", errorMessage='" + errorMessage + '\'' +
            ", removeButtonVisibility=" + removeButtonVisibility +
            '}';
    }
}
