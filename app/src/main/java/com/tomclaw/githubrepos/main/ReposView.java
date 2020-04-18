package com.tomclaw.githubrepos.main;

import android.content.Context;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.jakewharton.rxrelay2.PublishRelay;
import com.tomclaw.githubrepos.R;
import com.tomclaw.githubrepos.main.list.ReposAdapter;
import com.tomclaw.githubrepos.util.MenuAdapter;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by solkin on 20/01/2018.
 */
public interface ReposView {

    void showProgress();

    void showContent();

    void showError();

    void showPopup(@ArrayRes int items, @ArrayRes int icons, @NonNull List<String> urls);

    void updateList();

    @NonNull
    Observable<Object> retryClicks();

    @NonNull
    Observable<String> menuClicks();

    class ReposViewImpl implements ReposView {

        private final Context context;
        private final ReposAdapter adapter;
        private final ViewFlipper flipper;

        private PublishRelay<Object> retryRelay = PublishRelay.create();
        private PublishRelay<String> menuRelay = PublishRelay.create();

        public ReposViewImpl(View rootView, ReposAdapter adapter) {
            this.context = rootView.getContext();
            this.adapter = adapter;
            flipper = rootView.findViewById(R.id.flipper);
            RecyclerView recycler = rootView.findViewById(R.id.recycler);
            Button retryButton = rootView.findViewById(R.id.retry_button);

            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
            );
            adapter.setHasStableIds(true);
            recycler.setAdapter(adapter);
            recycler.setLayoutManager(layoutManager);
            retryButton.setOnClickListener(view -> retryRelay.accept(new Object()));
        }

        @Override
        public void showProgress() {
            flipper.setDisplayedChild(0);
        }

        @Override
        public void showContent() {
            flipper.setDisplayedChild(1);
        }

        @Override
        public void showError() {
            flipper.setDisplayedChild(2);
        }

        @Override
        public void showPopup(
                @ArrayRes int items,
                @ArrayRes int icons,
                @NonNull List<String> urls
        ) {
            new AlertDialog.Builder(context).setAdapter(
                    new MenuAdapter(context, items, icons),
                    (dialog, position) -> menuRelay.accept(urls.get(position))
            ).show();
        }

        @Override
        public void updateList() {
            adapter.notifyDataSetChanged();
        }

        @Override
        @NonNull
        public Observable<Object> retryClicks() {
            return retryRelay;
        }

        @Override
        @NonNull
        public Observable<String> menuClicks() {
            return menuRelay;
        }

    }

}
