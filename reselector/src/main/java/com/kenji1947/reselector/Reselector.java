package com.kenji1947.reselector;

import android.database.Observable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by kenji1947 on 15.04.2017.
 */

abstract public class Reselector implements ReselectorHolderListener{
    final HashSet<Integer> selectedItems = new HashSet<>();
    final HolderClickObservable holderClickObservable = new HolderClickObservable();
    final SelectionObservable holderSelectionObservable = new SelectionObservable();

    //----------------------------------------------------------------------------
    HolderClickObservable getHolderClickObservable() {
        return holderClickObservable;
    }
    public HashSet<Integer> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(HashSet<Integer> selectedItems) {
        this.selectedItems.clear();
        this.selectedItems.addAll(selectedItems);
    }
    abstract public void onBindHandler(RecyclerView.ViewHolder holder);
    abstract public void setActionModeStatusAndNotifyObservers(boolean isActionModeShowing);
    abstract public boolean getActionModeStatus();
    //----------------------------------------------------------------------------------
    public void removeItem(int removed_pos) {
        selectedItems.remove(removed_pos);
        shiftOnRemoveSelectedItems(removed_pos);
    }
    private void shiftOnRemoveSelectedItems(int removed_pos) {
        //Нужно сместить выделенные позиции из массива, если удаляется элемент стоящий перед ними
        HashSet<Integer> selectedItems = new HashSet<>();
        Iterator<Integer> iterator = getSelectedItems().iterator();
        while (iterator.hasNext()) {
            int selected_pos = iterator.next();
            if (selected_pos > removed_pos) {
                selectedItems.add(selected_pos - 1);
            } else {
                selectedItems.add(selected_pos);
            }
        }
        setSelectedItems(selectedItems);
    }
    //----------------------------------------------------------------------------------
    public boolean isItemSelected(int position)
    {
        return selectedItems.contains(position);
    }

    public void clearSelectionAndNotifyObservers() {
        for (int pos : selectedItems) {
            holderSelectionObservable.notifySelectionChanged(pos, false);
        }
        selectedItems.clear();
    }
    //----------------------------------------------------------------------------------------------
    class HolderClickObservable extends Observable<HolderClickObserver> {
        public final void notifyOnHolderClick(int pos) {
            synchronized (mObservers) {
                for (HolderClickObserver observer : mObservers) {
                    observer.onHolderClick(pos);
                }
            }
        }
        public final boolean notifyOnHolderLongClick(int pos) {
            boolean isConsumed = false;
            synchronized (mObservers) {
                for (HolderClickObserver observer : mObservers) {
                    isConsumed = isConsumed || observer.onHolderLongClick(pos);
                }
            }
            return isConsumed;
        }
    }

    class SelectionObservable extends Observable<SelectionObserver> {
        public void notifySelectionChanged(int pos, boolean isSelected) {
            synchronized (mObservers) {
                for (SelectionObserver observer : mObservers) {
                    observer.onSelectedChanged(pos, isSelected);
                }
            }
        }

        public void notifySelectableChanged(boolean isSelectable) {
            synchronized (mObservers) {
                for (SelectionObserver observer : mObservers) {
                    observer.onSelectableChanged(isSelectable);
                }
            }
        }
    }
    public final void registerHolderClickObserver(@NonNull HolderClickObserver observer) {
        holderClickObservable.registerObserver(observer);
    }

    @SuppressWarnings("UnusedDeclaration")
    public final void unregisterSelectionObserver(@NonNull SelectionObserver observer) {
        holderSelectionObservable.unregisterObserver(observer);
    }

    public final void registerSelectionObserver(@NonNull SelectionObserver observer) {
        holderSelectionObservable.registerObserver(observer);
    }

    @SuppressWarnings("UnusedDeclaration")
    public final void unregisterHolderClickObserver(@NonNull HolderClickObserver observer) {
        holderClickObservable.unregisterObserver(observer);
    }
}
