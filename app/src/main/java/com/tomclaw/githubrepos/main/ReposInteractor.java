package com.tomclaw.githubrepos.main;

import android.support.annotation.NonNull;

import com.tomclaw.githubrepos.core.GitHubApi;
import com.tomclaw.githubrepos.dto.Repo;
import com.tomclaw.githubrepos.util.SchedulersFactory;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by solkin on 20/01/2018.
 */
public interface ReposInteractor {

    @NonNull
    Observable<List<Repo>> loadReposList(String user, int page);

    class ReposInteractorImpl implements ReposInteractor {

        private int reposPerPage;
        private @NonNull
        String accessToken;
        private @NonNull
        GitHubApi api;
        private @NonNull
        SchedulersFactory schedulers;

        public ReposInteractorImpl(int reposPerPage,
                                   @NonNull String accessToken,
                                   @NonNull GitHubApi api,
                                   @NonNull SchedulersFactory schedulers) {
            this.reposPerPage = reposPerPage;
            this.accessToken = accessToken;
            this.api = api;
            this.schedulers = schedulers;
        }

        @Override
        @NonNull
        public Observable<List<Repo>> loadReposList(String user, int page) {
            return api.listRepos(user, accessToken, page, reposPerPage)
                    .subscribeOn(schedulers.io());
        }
    }

}
