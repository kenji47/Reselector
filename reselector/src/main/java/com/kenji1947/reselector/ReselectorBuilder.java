package com.kenji1947.reselector;

/**
 * Created by kenji1947 on 15.04.2017.
 */

public class ReselectorBuilder {
    public enum MODE {
        CLICK,
        SINGLE_SELECTION,
        MULTI_SELECTION,
        LONG_CLICK_ACTION_MODE,
        ACTION_MODE
    }

    public static Reselector build(MODE mode) {
        switch (mode) {
            case CLICK:
                break;
            case SINGLE_SELECTION:
                return new SingleSelectionReselector();
            case MULTI_SELECTION:
                return new MultipleSelectionReselector();
            case LONG_CLICK_ACTION_MODE:
                return new ChoiceModeReselector();
            case ACTION_MODE:
                break;
        }
        return new SingleSelectionReselector();
    }
}
