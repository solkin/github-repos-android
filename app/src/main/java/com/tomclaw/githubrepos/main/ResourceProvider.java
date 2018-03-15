package com.tomclaw.githubrepos.main;

import android.support.annotation.ArrayRes;

import com.tomclaw.githubrepos.R;

/**
 * Created by solkin on 20/01/2018.
 */
public interface ResourceProvider {

    @ArrayRes int getMenuItems();

    @ArrayRes int getMenuIcons();

    class ResourceProviderImpl implements ResourceProvider {

        @Override
        @ArrayRes
        public int getMenuItems() {
            return R.array.context_menu_items;
        }

        @Override
        @ArrayRes
        public int getMenuIcons() {
            return R.array.context_menu_icons;
        }

    }

}
