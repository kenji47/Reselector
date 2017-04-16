package com.kenji1947.reselector;

import android.support.v7.widget.RecyclerView;

/**
 * Created by kenji1947 on 16.04.2017.
 */

public class ChoiceModeReselector extends Reselector {
    private boolean isActionModeShowing = false;

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
        if (isActionModeShowing) {
            selectItem(holder.getAdapterPosition());
        } else {
            holderClickObservable.notifyOnHolderClick(holder.getAdapterPosition());
        }
    }

    @Override
    public void onHolderLongClick(RecyclerView.ViewHolder holder) {
        //Если AM == false - запустить его, иначе проксировать longClick
        if (!isActionModeShowing) {
            isActionModeShowing = true;
            clearSelectionAndNotifyObservers();
            holderSelectionObservable.notifySelectableChanged(true);
            selectItem(holder.getAdapterPosition());
        } else {
            holderClickObservable.notifyOnHolderLongClick(holder.getAdapterPosition());
        }
    }

    @Override
    public void onBindHandler(RecyclerView.ViewHolder holder) {

    }

    @Override
    public void setActionModeStatusAndNotifyObservers(boolean isActionModeShowing) {
        this.isActionModeShowing = isActionModeShowing;
        holderSelectionObservable.notifySelectableChanged(isActionModeShowing);
    }

    @Override
    public boolean getActionModeStatus() {
        return isActionModeShowing;
    }
}
