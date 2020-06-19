package com.data.model.util;

import java.util.Collection;

public class Utils {
    public boolean isEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }
}
