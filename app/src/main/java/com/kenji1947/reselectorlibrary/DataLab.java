package com.kenji1947.reselectorlibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenji1947 on 16.04.2017.
 */

public class DataLab {
    private static DataLab dataLab;
    private static List<String> list = new ArrayList<>();

    private DataLab() {
        for (int i = 0; i <= 35; i++) {
            list.add("Text:" + i);
        }
    }

    public static List<String> getData() {
        if (dataLab == null) {
            dataLab = new DataLab();
        }
        return list;
    }
}
