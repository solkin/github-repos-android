package com.tomclaw.githubrepos;

import android.app.Application;

import com.tomclaw.githubrepos.di.AppComponent;
import com.tomclaw.githubrepos.di.AppModule;
import com.tomclaw.githubrepos.di.DaggerAppComponent;

/**
 * Created by solkin on 20/01/2018.
 */
public class App extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = buildComponent();
    }

    public static AppComponent getComponent() {
        return component;
    }

    private AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

}
