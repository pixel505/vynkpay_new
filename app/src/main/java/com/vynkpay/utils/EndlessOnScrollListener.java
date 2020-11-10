package com.vynkpay.utils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by user10 on 7/5/16.
 */
public abstract class EndlessOnScrollListener extends RecyclerView.OnScrollListener {

    public static String TAG = EndlessOnScrollListener.class.getSimpleName();

    // use your LayoutManager instead
    private LinearLayoutManager llm;

    public EndlessOnScrollListener() {
        // this.llm = sglm;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (!recyclerView.canScrollVertically(1)) {
            onScrolledToEnd();
        }
    }

    public abstract void onScrolledToEnd();
}

