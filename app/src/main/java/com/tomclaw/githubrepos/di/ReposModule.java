package com.tomclaw.githubrepos.di;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jakewharton.rxrelay2.PublishRelay;
import com.tomclaw.githubrepos.core.GitHubApi;
import com.tomclaw.githubrepos.main.RepoItemConverter;
import com.tomclaw.githubrepos.main.ReposInteractor;
import com.tomclaw.githubrepos.main.ReposPresenter;
import com.tomclaw.githubrepos.main.ResourceProvider;
import com.tomclaw.githubrepos.main.list.RepoItem;
import com.tomclaw.githubrepos.util.DataProvider;
import com.tomclaw.githubrepos.util.Logger;
import com.tomclaw.githubrepos.util.PerActivity;
import com.tomclaw.githubrepos.util.SchedulersFactory;

import dagger.Module;
import dagger.Provides;

import static com.tomclaw.githubrepos.core.Config.ACCESS_TOKEN;
import static com.tomclaw.githubrepos.core.Config.REPOS_PER_PAGE;
import static com.tomclaw.githubrepos.core.Config.USER;

/**
 * Created by solkin on 20/01/2018.
 */
@Module
public class ReposModule {

    @Nullable
    private Bundle presenterState;

    @NonNull
    private PublishRelay<Boolean> scrollRelay;

    @NonNull
    private PublishRelay<RepoItem> clickRelay;

    public ReposModule(@Nullable Bundle presenterState,
                       @NonNull PublishRelay<Boolean> scrollRelay,
                       @NonNull PublishRelay<RepoItem> clickRelay) {
        this.presenterState = presenterState;
        this.scrollRelay = scrollRelay;
        this.clickRelay = clickRelay;
    }

    @Provides
    @PerActivity
    ReposPresenter provideReposPresenter(@NonNull DataProvider<RepoItem> dataProvider,
                                         @NonNull RepoItemConverter repoItemConverter,
                                         @NonNull ResourceProvider resourceProvider,
                                         @NonNull ReposInteractor interactor,
                                         @NonNull SchedulersFactory schedulers,
                                         @NonNull Logger logger) {
        return new ReposPresenter.ReposPresenterImpl(
                USER,
                dataProvider,
                repoItemConverter,
                scrollRelay,
                clickRelay,
                resourceProvider,
                interactor,
                schedulers,
                logger,
                presenterState
        );
    }

    @Provides
    @PerActivity
    ReposInteractor provideReposInteractor(@NonNull GitHubApi api,
                                           @NonNull SchedulersFactory schedulers) {
        return new ReposInteractor.ReposInteractorImpl(
                REPOS_PER_PAGE,
                ACCESS_TOKEN,
                api,
                schedulers
        );
    }

    @Provides
    @PerActivity
    DataProvider<RepoItem> provideReposDataProvider() {
        return new DataProvider<>();
    }

    @Provides
    @PerActivity
    RepoItemConverter provideRepoItemConverter() {
        return new RepoItemConverter.RepoItemConverterImpl();
    }

    @Provides
    @PerActivity
    ResourceProvider provideResourceProvider() {
        return new ResourceProvider.ResourceProviderImpl();
    }

}
