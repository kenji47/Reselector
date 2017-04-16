package com.kenji1947.reselector;

import android.support.v7.widget.RecyclerView;

/**
 * Created by kenji1947 on 15.04.2017.
 */

public interface ReselectorHolderListener {
    void onHolderClick(RecyclerView.ViewHolder holder);
    void onHolderLongClick(RecyclerView.ViewHolder holder);
}
