package com.kenji1947.reselector;

import android.support.v7.widget.RecyclerView;

/**
 * Created by kenji1947 on 15.04.2017.
 */

public class MultipleSelectionReselector extends Reselector {


    private void selectItem(int pos) {
        boolean isItemSelected = isItemSelected(pos);
        if (pos != RecyclerView.NO_POSITION) {
            if (isItemSelected) {
                selectedItems.remove(pos);
            } else {
                selectedItems.add(pos);
            }
            //Если выделен - снять, если не выделен - поставить выделение
            holderSelectionObservable.notifySelectionChanged(pos, !isItemSelected);
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
