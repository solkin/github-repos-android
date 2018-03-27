package com.tomclaw.githubrepos.main.list;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by solkin on 20/01/2018.
 */
public class RepoItem implements Parcelable {

    private final @NonNull
    String name;
    private final @NonNull
    String description;
    private final @NonNull
    String ownerLogin;
    private final @NonNull
    String repoUrl;
    private final @NonNull
    String profileUrl;
    private final boolean isShowProgress;
    private final boolean isShowError;

    public RepoItem(@NonNull String name,
                    @NonNull String description,
                    @NonNull String ownerLogin,
                    @NonNull String repoUrl,
                    @NonNull String profileUrl,
                    boolean isShowProgress,
                    boolean isShowError) {
        this.name = name;
        this.description = description;
        this.ownerLogin = ownerLogin;
        this.repoUrl = repoUrl;
        this.profileUrl = profileUrl;
        this.isShowProgress = isShowProgress;
        this.isShowError = isShowError;
    }

    protected RepoItem(Parcel in) {
        name = in.readString();
        description = in.readString();
        ownerLogin = in.readString();
        repoUrl = in.readString();
        profileUrl = in.readString();
        isShowProgress = in.readByte() != 0;
        isShowError = in.readByte() != 0;
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
    public String getOwnerLogin() {
        return ownerLogin;
    }

    @NonNull
    public String getRepoUrl() {
        return repoUrl;
    }

    @NonNull
    public String getProfileUrl() {
        return profileUrl;
    }

    public boolean isShowProgress() {
        return isShowProgress;
    }

    public boolean isShowError() {
        return isShowError;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(ownerLogin);
        dest.writeString(repoUrl);
        dest.writeString(profileUrl);
        dest.writeByte((byte) (isShowProgress ? 1 : 0));
        dest.writeByte((byte) (isShowError ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RepoItem> CREATOR = new Creator<RepoItem>() {
        @Override
        public RepoItem createFromParcel(Parcel in) {
            return new RepoItem(in);
        }

        @Override
        public RepoItem[] newArray(int size) {
            return new RepoItem[size];
        }
    };
}
