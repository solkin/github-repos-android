package com.tomclaw.githubrepos;

import android.os.Bundle;

import com.jakewharton.rxrelay2.PublishRelay;
import com.tomclaw.githubrepos.dto.Repo;
import com.tomclaw.githubrepos.main.RepoItemConverter;
import com.tomclaw.githubrepos.main.ReposInteractor;
import com.tomclaw.githubrepos.main.ReposPresenter;
import com.tomclaw.githubrepos.main.ReposRouter;
import com.tomclaw.githubrepos.main.ReposView;
import com.tomclaw.githubrepos.main.ResourceProvider;
import com.tomclaw.githubrepos.main.list.RepoItem;
import com.tomclaw.githubrepos.util.DataProvider;
import com.tomclaw.githubrepos.util.Logger;
import com.tomclaw.githubrepos.util.SchedulersFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by solkin on 20/01/2018.
 */
public class ReposPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private PodamFactory factory;

    private String user;

    @Mock
    DataProvider<RepoItem> dataProvider;
    @Mock
    ResourceProvider resourceProvider;
    @Mock
    ReposInteractor interactor;
    @Mock
    SchedulersFactory schedulers;
    @Mock
    Logger logger;
    @Mock
    ReposView view;
    @Mock
    ReposRouter router;

    private RepoItemConverter repoItemConverter;

    private PublishRelay<Boolean> scrollRelay;
    private PublishRelay<RepoItem> clickRelay;

    @Before
    public void init() {
        user = "user";

        factory = new PodamFactoryImpl();

        mockSchedulersFactory();
        mockInteractor();
        mockView();

        createRepoConverter();

        scrollRelay = PublishRelay.create();
        clickRelay = PublishRelay.create();
    }

    @Test
    public void attachView_loadRepoListWithCorrectUser_reposNeverLoaded() {
        ReposPresenter presenter = createReposPresenter(null);

        presenter.attachView(view);

        verify(interactor).loadReposList(eq(user), eq(1));
    }

    @Test
    public void attachView_showProgress_reposNeverLoaded() {
        ReposPresenter presenter = createReposPresenter(null);

        presenter.attachView(view);

        verify(view).showProgress();
    }

    @Test
    public void attachView_showContent_reposLoaded() {
        ReposPresenter presenter = createReposPresenter(null);

        presenter.attachView(view);

        verify(view).showContent();
    }

    @Test
    public void scrollEvent_loadNextPage() {
        ReposPresenter presenter = createReposPresenter(null);
        presenter.attachView(view);
        clearInvocations(interactor);

        scrollRelay.accept(true);

        verify(interactor).loadReposList(eq(user), eq(2));
    }

    @Test
    public void clickEvent_showPopup() {
        RepoItem item = randomRepoItem();
        when(resourceProvider.getMenuItems()).thenReturn(1);
        when(resourceProvider.getMenuIcons()).thenReturn(2);
        ReposPresenter presenter = createReposPresenter(null);
        presenter.attachView(view);
        clearInvocations(interactor);

        clickRelay.accept(item);

        verify(view).showPopup(
                eq(1),
                eq(2),
                eq(asList(item.getRepoUrl(), item.getProfileUrl()))
        );
    }

    @Test
    public void attachView_showError_reposNotLoaded() {
        ReposPresenter presenter = createReposPresenter(null);
        mockInteractorWithError();

        presenter.attachView(view);

        verify(view).showError();
    }

    @Test
    public void attachView_updateListNotInvoked_reposNeverLoaded() {
        ReposPresenter presenter = createReposPresenter(null);
        mockInteractorWithError();

        presenter.attachView(view);

        verify(view, never()).updateList();
    }

    @Test
    public void attachView_updateList_reposAtLeastOnceLoaded() {
        PublishRelay<Object> retryClickRelay = PublishRelay.create();
        when(view.retryClicks()).thenReturn(retryClickRelay);
        ReposPresenter presenter = createReposPresenter(null);
        presenter.attachView(view);
        mockInteractorWithError();
        clearInvocations(view);

        retryClickRelay.accept(new Object());

        verify(view).updateList();
    }

    @Test
    public void attachView_updateList_reposLoaded() {
        ReposPresenter presenter = createReposPresenter(null);

        presenter.attachView(view);

        verify(view).updateList();
    }

    @Test
    public void menuClickEvent_openBrowser() {
        PublishRelay<String> menuClickRelay = PublishRelay.create();
        when(view.menuClicks()).thenReturn(menuClickRelay);

        ReposPresenter presenter = createReposPresenter(null);
        presenter.attachView(view);
        presenter.attachRouter(router);

        menuClickRelay.accept("url");

        verify(router).openBrowser(eq("url"));
    }

    @Test
    public void retryClickEvent_loadReposList() {
        PublishRelay<Object> retryClickRelay = PublishRelay.create();
        when(view.retryClicks()).thenReturn(retryClickRelay);
        mockInteractorWithError();

        ReposPresenter presenter = createReposPresenter(null);
        presenter.attachView(view);
        clearInvocations(interactor);
        mockInteractor();

        retryClickRelay.accept(new Object());

        verify(interactor).loadReposList(eq(user), eq(1));
    }

    private ReposPresenter createReposPresenter(Bundle presenterState) {
        return new ReposPresenter.ReposPresenterImpl(
                user,
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

    private void mockSchedulersFactory() {
        when(schedulers.io()).thenReturn(Schedulers.trampoline());
        when(schedulers.mainThread()).thenReturn(Schedulers.trampoline());
    }

    private void mockInteractor() {
        when(interactor.loadReposList(any(), anyInt())).thenReturn(
                Observable.just(randomRepoList())
        );
    }

    private void mockInteractorWithError() {
        when(interactor.loadReposList(any(), anyInt())).thenReturn(
                Observable.error(new IOException())
        );
    }

    private void createRepoConverter() {
        repoItemConverter = new RepoItemConverter.RepoItemConverterImpl();
    }

    private void mockView() {
        when(view.menuClicks()).thenReturn(Observable.empty());
        when(view.retryClicks()).thenReturn(Observable.empty());
    }

    private List<Repo> randomRepoList() {
        return asList(
                randomRepo(),
                randomRepo(),
                randomRepo()
        );
    }

    private Repo randomRepo() {
        return factory.manufacturePojo(Repo.class);
    }

    private RepoItem randomRepoItem() {
        return factory.manufacturePojo(RepoItem.class);
    }

}
