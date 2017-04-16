package com.kenji1947.reselector;


public interface SelectionObserver
{
    void onSelectedChanged(int pos, boolean isSelected);

    void onSelectableChanged(boolean isSelectable);
}
