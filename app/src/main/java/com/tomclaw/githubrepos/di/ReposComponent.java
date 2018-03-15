package com.tomclaw.githubrepos.di;

import com.tomclaw.githubrepos.main.ReposActivity;
import com.tomclaw.githubrepos.util.PerActivity;

import dagger.Subcomponent;

/**
 * Created by solkin on 20/01/2018.
 */
@PerActivity
@Subcomponent(modules = ReposModule.class)
public interface ReposComponent {

    void inject(ReposActivity activity);

}
