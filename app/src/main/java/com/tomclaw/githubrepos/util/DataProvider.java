package com.tomclaw.githubrepos.util;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Created by solkin on 20/01/2018.
 */
public class DataProvider<A> {

    private List<A> data;

    public DataProvider() {
        data = emptyList();
    }

    public A getItem(int position) {
        return data.get(position);
    }

    public int size() {
        return data.size();
    }

    public void setData(List<A> data) {
        this.data = new ArrayList<>(data);
    }

}
