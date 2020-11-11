package com.neige_i.mareu.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.neige_i.mareu.data.DI;

import java.time.LocalTime;
import java.util.Arrays;

import static com.neige_i.mareu.Util.NO_ERROR;
import static com.neige_i.mareu.Util.removeRequiredError;

/**
 * Store the information about a member.
 */
public class Member implements Parcelable {

    // ----------------------------------- PARCELABLE VARIABLES ------------------------------------

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    // ------------------------------------ INSTANCE VARIABLES -------------------------------------

    /**
     * Id of the member.
     */
    private final int id;
    /**
     * Email of the member.
     */
    @NonNull
    private String email = "";
    /**
     * Id of the String resource for the error message.
     */
    @StringRes
    private int error = NO_ERROR;
    /**
     * Start and end times of a meeting that contains this member.
     */
    private LocalTime[] sameMemberTimes;

    // ------------------------------- CONSTRUCTOR & GETTERS/SETTERS -------------------------------

    public Member() {
        id = DI.getMemberId();
    }

    public Member(int id, @NonNull String email, int error, LocalTime... sameMemberTimes) {
        this.id = id;
        this.email = email;
        this.error = error;
        this.sameMemberTimes = sameMemberTimes;
    }

    protected Member(Parcel in) {
        id = in.readInt();
        email = in.readString();
        error = in.readInt();
        sameMemberTimes = (LocalTime[]) in.readSerializable();
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
        error = removeRequiredError(error, email);
    }

    public int getError() {
        return error;
    }

    public void setError(int error, LocalTime... sameMemberTimes) {
        this.error = error;
        this.sameMemberTimes = sameMemberTimes;
    }

    public LocalTime[] getSameMemberTimes() {
        return sameMemberTimes;
    }

    // ------------------------------------ PARCELABLE METHODS -------------------------------------

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeInt(error);
        dest.writeSerializable(sameMemberTimes);
    }

    // -------------------------------------- OBJECT METHODS ---------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id == member.id &&
            error == member.error &&
            email.equals(member.email);
    }

    @NonNull
    @Override
    public String toString() {
        return "Member{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", error=" + error +
            ", sameMemberTimes=" + Arrays.toString(sameMemberTimes) +
            '}';
    }
}
