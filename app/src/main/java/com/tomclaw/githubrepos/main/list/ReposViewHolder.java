package com.tomclaw.githubrepos.main.list;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxrelay2.PublishRelay;
import com.tomclaw.githubrepos.R;

/**
 * Created by solkin on 20/01/2018.
 */
class ReposViewHolder extends RecyclerView.ViewHolder {

    private CardView cardView;
    private TextView nameView;
    private TextView descriptionView;
    private TextView ownerView;
    private View progressView;
    private View errorView;

    public ReposViewHolder(@NonNull View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.card_view);
        nameView = itemView.findViewById(R.id.name_view);
        descriptionView = itemView.findViewById(R.id.description_view);
        ownerView = itemView.findViewById(R.id.owner_view);
        progressView = itemView.findViewById(R.id.progress_view);
        errorView = itemView.findViewById(R.id.error_view);
    }

    public void bind(@NonNull RepoItem item,
                     @NonNull PublishRelay<RepoItem> clickRelay) {
        nameView.setText(item.getName());
        descriptionView.setText(item.getDescription());
        ownerView.setText(item.getOwnerLogin());
        progressView.setVisibility(item.isShowProgress() ? View.VISIBLE : View.GONE);
        errorView.setVisibility(item.isShowError() ? View.VISIBLE : View.GONE);
        cardView.setOnClickListener(view -> clickRelay.accept(item));
    }
}
