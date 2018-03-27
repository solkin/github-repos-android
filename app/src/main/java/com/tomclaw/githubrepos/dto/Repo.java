package com.tomclaw.githubrepos.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by solkin on 20/01/2018.
 */
public class Repo implements Parcelable {

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    @SerializedName("html_url")
    private String url;

    @NonNull
    private Owner owner;

    public Repo(@NonNull String name,
                @NonNull String description,
                @NonNull String url,
                @NonNull Owner owner) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.owner = owner;
    }

    protected Repo(Parcel in) {
        name = in.readString();
        description = in.readString();
        url = in.readString();
        owner = in.readParcelable(Owner.class.getClassLoader());
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @NonNull
    public Owner getOwner() {
        return owner;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeParcelable(owner, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Repo> CREATOR = new Creator<Repo>() {
        @Override
        public Repo createFromParcel(Parcel in) {
            return new Repo(in);
        }

        @Override
        public Repo[] newArray(int size) {
            return new Repo[size];
        }
    };
}
