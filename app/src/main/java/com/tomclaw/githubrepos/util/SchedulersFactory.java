package com.tomclaw.githubrepos.util;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by solkin on 20/01/2018.
 */
public interface SchedulersFactory {

    Scheduler io();

    Scheduler mainThread();

    class SchedulersFactoryImpl implements SchedulersFactory {

        @Override
        public Scheduler io() {
            return Schedulers.io();
        }

        @Override
        public Scheduler mainThread() {
            return AndroidSchedulers.mainThread();
        }
    }

}
