package com.tomclaw.githubrepos.di;

import com.tomclaw.githubrepos.core.Config;
import com.tomclaw.githubrepos.core.GitHubApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by solkin on 20/01/2018.
 */
@Module
class ApiModule {

    @Provides
    @Singleton
    GitHubApi provideApi() {
        return new Retrofit.Builder()
                .baseUrl(Config.API_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GitHubApi.class);
    }
}
