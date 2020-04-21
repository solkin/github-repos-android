package com.tomclaw.githubrepos.main;

import androidx.annotation.NonNull;

import com.tomclaw.githubrepos.dto.Repo;
import com.tomclaw.githubrepos.main.list.RepoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by solkin on 20/01/2018.
 */
public interface RepoItemConverter {

    @NonNull
    List<RepoItem> convert(@NonNull List<Repo> repos, boolean isLoading, boolean isError);

    class RepoItemConverterImpl implements RepoItemConverter {

        @Override
        @NonNull
        public List<RepoItem> convert(
                @NonNull List<Repo> repos,
                boolean isLoading,
                boolean isError
        ) {
            List<RepoItem> items = new ArrayList<>();
            int i = 0;
            for (Repo repo : repos) {
                boolean isLast = (i++ == (repos.size() - 1));
                boolean isShowProgress = false;
                boolean isShowError = false;
                if (isLast) {
                    if (isError) {
                        isShowError = true;
                    } else if (isLoading) {
                        isShowProgress = true;
                    }
                }
                items.add(new RepoItem(
                        repo.getName(),
                        repo.getDescription(),
                        repo.getOwner().getLogin(),
                        repo.getUrl(),
                        repo.getOwner().getUrl(),
                        isShowProgress,
                        isShowError
                ));
            }
            return items;
        }

    }

}
