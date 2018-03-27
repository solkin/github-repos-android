package com.tomclaw.githubrepos.main.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxrelay2.PublishRelay;
import com.tomclaw.githubrepos.R;
import com.tomclaw.githubrepos.util.DataProvider;

/**
 * Created by solkin on 20/01/2018.
 */
public class ReposAdapter extends RecyclerView.Adapter<ReposViewHolder> {

    @NonNull
    private Context context;

    @NonNull
    private DataProvider<RepoItem> repos;
    private PublishRelay<Boolean> scrollRelay;
    private PublishRelay<RepoItem> clickRelay;

    public ReposAdapter(@NonNull Context context,
                        @NonNull DataProvider<RepoItem> repos,
                        @NonNull PublishRelay<Boolean> scrollRelay,
                        @NonNull PublishRelay<RepoItem> clickRelay) {
        this.context = context;
        this.repos = repos;
        this.scrollRelay = scrollRelay;
        this.clickRelay = clickRelay;
    }

    @Override
    public ReposViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_repo, parent, false);
        return new ReposViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReposViewHolder holder, int position) {
        RepoItem item = repos.getItem(position);
        boolean isLast = (repos.size() - 1 == position);
        scrollRelay.accept(isLast);
        holder.bind(item, clickRelay);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }
}
