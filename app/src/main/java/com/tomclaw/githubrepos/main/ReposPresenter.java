package com.tomclaw.githubrepos.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jakewharton.rxrelay2.PublishRelay;
import com.tomclaw.githubrepos.dto.Repo;
import com.tomclaw.githubrepos.main.list.RepoItem;
import com.tomclaw.githubrepos.util.DataProvider;
import com.tomclaw.githubrepos.util.Logger;
import com.tomclaw.githubrepos.util.SchedulersFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by solkin on 20/01/2018.
 */
public interface ReposPresenter {

    void attachView(ReposView view);

    void attachRouter(ReposRouter router);

    void detachView();

    void detachRouter();

    @NonNull
    Bundle onSaveState();

    class ReposPresenterImpl implements ReposPresenter {

        private static final String KEY_PAGE = "page";
        private static final String KEY_LOADED = "loaded";
        private static final String KEY_ERROR = "error";
        private static final String KEY_REPOS = "repos";

        @NonNull
        private String user;

        @NonNull
        private DataProvider<RepoItem> dataProvider;

        @NonNull
        private RepoItemConverter repoItemConverter;

        @NonNull
        private PublishRelay<Boolean> scrollRelay;

        @NonNull
        private PublishRelay<RepoItem> clickRelay;

        @NonNull
        private ResourceProvider resourceProvider;

        @NonNull
        private ReposInteractor interactor;

        @NonNull
        private SchedulersFactory schedulers;

        private Logger logger;

        @Nullable
        private Bundle state;

        @Nullable
        private ReposView view;

        @Nullable
        private ReposRouter router;

        private final CompositeDisposable subscriptions;

        private int page = 1;
        private boolean isLoaded;
        private boolean isError;
        private ArrayList<Repo> repos = new ArrayList<>();

        public ReposPresenterImpl(
                @NonNull String user,
                @NonNull DataProvider<RepoItem> dataProvider,
                @NonNull RepoItemConverter repoItemConverter,
                @NonNull PublishRelay<Boolean> scrollRelay,
                @NonNull PublishRelay<RepoItem> clickRelay,
                @NonNull ResourceProvider resourceProvider,
                @NonNull ReposInteractor interactor,
                @NonNull SchedulersFactory schedulers,
                @NonNull Logger logger,
                @Nullable Bundle state
        ) {
            this.user = user;
            this.dataProvider = dataProvider;
            this.repoItemConverter = repoItemConverter;
            this.scrollRelay = scrollRelay;
            this.clickRelay = clickRelay;
            this.resourceProvider = resourceProvider;
            this.interactor = interactor;
            this.schedulers = schedulers;
            this.logger = logger;
            this.state = state;

            subscriptions = new CompositeDisposable();

            restoreState();
        }

        private void restoreState() {
            if (state != null) {
                page = state.getInt(KEY_PAGE, 1);
                isLoaded = state.getBoolean(KEY_LOADED, false);
                isError = state.getBoolean(KEY_ERROR, false);
                repos = state.getParcelableArrayList(KEY_REPOS);
            }
        }

        @Override
        public void attachView(@NonNull ReposView view) {
            this.view = view;
            subscriptions.add(
                    view.retryClicks()
                            .observeOn(schedulers.mainThread())
                            .subscribe(o -> loadReposList())
            );
            subscriptions.add(
                    view.menuClicks()
                            .observeOn(schedulers.mainThread())
                            .subscribe(url -> {
                                if (router != null) {
                                    router.openBrowser(url);
                                }
                            })
            );
            subscriptions.add(
                    scrollRelay
                            .subscribeOn(schedulers.mainThread())
                            .subscribe(last -> {
                                if (last && !isLoaded) {
                                    loadReposList();
                                }
                            })
            );
            subscriptions.add(
                    clickRelay
                            .subscribeOn(schedulers.mainThread())
                            .subscribe(item -> {
                                List<String> urls = Arrays.asList(
                                        item.getRepoUrl(),
                                        item.getProfileUrl()
                                );
                                showPopup(urls);
                            })
            );
            if (repos.isEmpty()) {
                loadReposList();
            } else {
                bindReposList();
            }
        }

        @Override
        public void attachRouter(@NonNull ReposRouter router) {
            this.router = router;
        }

        @Override
        public void detachView() {
            subscriptions.clear();
            this.view = null;
        }

        @Override
        public void detachRouter() {
            this.router = null;
        }

        @Override
        @NonNull
        public Bundle onSaveState() {
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_PAGE, page);
            bundle.putBoolean(KEY_LOADED, isLoaded);
            bundle.putBoolean(KEY_ERROR, isError);
            bundle.putParcelableArrayList(KEY_REPOS, repos);
            return bundle;
        }

        private void loadReposList() {
            final int loadPage = page;
            if (repos.isEmpty()) {
                showProgress();
            } else {
                showContent();
            }
            isLoaded = false;
            isError = false;
            logger.log("load page " + loadPage);
            subscriptions.add(
                    interactor.loadReposList(user, loadPage)
                            .observeOn(schedulers.mainThread())
                            .subscribe(
                                    items -> onLoaded(items, loadPage),
                                    throwable -> onError()
                            )
            );
        }

        private void onLoaded(List<Repo> items, int loadPage) {
            logger.log("repos loaded: " + items);
            page = loadPage + 1;
            repos.addAll(items);
            isLoaded = items.isEmpty();
            bindReposList();
        }

        private void bindReposList() {
            List<RepoItem> repoItems = repoItemConverter.convert(repos, !isLoaded, isError);
            dataProvider.setData(repoItems);
            updateList();
            showContent();
        }

        private void onError() {
            logger.log("repos loading error");
            isError = true;
            if (repos.isEmpty()) {
                showError();
            } else {
                bindReposList();
            }
        }

        private void showProgress() {
            if (view != null) view.showProgress();
        }

        private void updateList() {
            if (view != null) view.updateList();
        }

        private void showContent() {
            if (view != null) view.showContent();
        }

        private void showError() {
            if (view != null) view.showError();
        }

        private void showPopup(List<String> urls) {
            if (view != null) view.showPopup(
                    resourceProvider.getMenuItems(),
                    resourceProvider.getMenuIcons(),
                    urls
            );
        }

    }

}
