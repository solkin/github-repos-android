package com.tomclaw.githubrepos.di;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by solkin on 20/01/2018.
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    ReposComponent reposComponent(ReposModule module);

}
