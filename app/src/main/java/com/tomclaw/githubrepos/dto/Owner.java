package com.tomclaw.githubrepos.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by solkin on 20/01/2018.
 */
public class Owner implements Parcelable {

    @NonNull
    private String login;

    @NonNull
    @SerializedName("html_url")
    private String url;

    public Owner(@NonNull String login,
                 @NonNull String url) {
        this.login = login;
        this.url = url;
    }

    protected Owner(Parcel in) {
        login = in.readString();
        url = in.readString();
    }

    @NonNull
    public String getLogin() {
        return login;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Owner> CREATOR = new Creator<Owner>() {
        @Override
        public Owner createFromParcel(Parcel in) {
            return new Owner(in);
        }

        @Override
        public Owner[] newArray(int size) {
            return new Owner[size];
        }
    };
}
