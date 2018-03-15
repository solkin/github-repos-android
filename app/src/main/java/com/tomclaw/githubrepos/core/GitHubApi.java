package com.tomclaw.githubrepos.core;

import com.tomclaw.githubrepos.dto.Repo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by solkin on 20/01/2018.
 */
public interface GitHubApi {

    @GET("users/{user}/repos")
    Observable<List<Repo>> listRepos(@Path("user") String user,
                                     @Query("access_token") String token,
                                     @Query("page") int page,
                                     @Query("per_page") int perPage);

}
