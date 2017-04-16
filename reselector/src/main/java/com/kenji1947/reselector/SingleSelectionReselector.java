package com.kenji1947.reselector;

import android.support.v7.widget.RecyclerView;

/**
 * Created by kenji1947 on 15.04.2017.
 */

public class SingleSelectionReselector extends Reselector {

    private void selectItem(int pos) {
        if (!isItemSelected(pos) && pos != RecyclerView.NO_POSITION) {
            clearSelectionAndNotifyObservers();
            selectedItems.add(pos);
            holderSelectionObservable.notifySelectionChanged(pos, true);
        }
    }

    @Override
    public void onHolderClick(RecyclerView.ViewHolder holder) {
        selectItem(holder.getAdapterPosition());
    }

    @Override
    public void onHolderLongClick(RecyclerView.ViewHolder holder) {

    }

    @Override
    public void onBindHandler(RecyclerView.ViewHolder holder) {
        holderSelectionObservable.notifySelectionChanged(holder.getAdapterPosition(),
                isItemSelected(holder.getAdapterPosition()));
    }

    @Override
    public void setActionModeStatusAndNotifyObservers(boolean isActionModeShowing) {

    }

    @Override
    public boolean getActionModeStatus() {
        return false;
    }
}
