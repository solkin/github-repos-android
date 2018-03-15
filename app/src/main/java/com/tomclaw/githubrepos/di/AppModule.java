package com.tomclaw.githubrepos.di;

import android.app.Application;
import android.content.Context;

import com.tomclaw.githubrepos.util.Logger;
import com.tomclaw.githubrepos.util.SchedulersFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by solkin on 20/01/2018.
 */
@Module
public class AppModule {

    private Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app;
    }

    @Provides
    @Singleton
    SchedulersFactory provideSchedulersFactory() {
        return new SchedulersFactory.SchedulersFactoryImpl();
    }

    @Provides
    @Singleton
    Logger provideLogger() {
        return new Logger();
    }

}
