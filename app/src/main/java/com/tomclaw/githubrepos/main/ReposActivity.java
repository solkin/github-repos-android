package com.tomclaw.githubrepos.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.tomclaw.githubrepos.App;
import com.tomclaw.githubrepos.R;
import com.tomclaw.githubrepos.di.ReposModule;
import com.tomclaw.githubrepos.main.list.RepoItem;
import com.tomclaw.githubrepos.main.list.ReposAdapter;
import com.tomclaw.githubrepos.util.DataProvider;
import com.tomclaw.githubrepos.util.Logger;
import com.jakewharton.rxrelay2.PublishRelay;

import javax.inject.Inject;

public class ReposActivity extends AppCompatActivity implements ReposRouter {

    private static final String KEY_PRESENTER_STATE = "presenter_state";

    @Inject
    ReposPresenter presenter;

    @Inject
    Logger logger;

    @Inject
    DataProvider<RepoItem> repoDataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle presenterState = null;
        if (savedInstanceState != null) {
            presenterState = savedInstanceState.getBundle(KEY_PRESENTER_STATE);
        }
        PublishRelay<Boolean> scrollRelay = PublishRelay.create();
        PublishRelay<RepoItem> clickRelay = PublishRelay.create();
        App.getComponent()
                .reposComponent(new ReposModule(presenterState, scrollRelay, clickRelay))
                .inject(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repos);
        ReposAdapter reposAdapter = new ReposAdapter(
                this,
                repoDataProvider,
                scrollRelay,
                clickRelay
        );
        ReposView view = new ReposView.ReposViewImpl(getWindow().getDecorView(), reposAdapter);

        presenter.attachView(view);
        presenter.attachRouter(this);
    }

    @Override
    protected void onDestroy() {
        presenter.detachRouter();
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(KEY_PRESENTER_STATE, presenter.onSaveState());
    }

    @Override
    public void openBrowser(@NonNull String url) {
        try {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
            );
            startActivity(intent);
        } catch (Throwable ex) {
            logger.log("unable to start browser", ex);
        }
    }
}
