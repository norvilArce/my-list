package com.jrackham.util;

import android.content.res.Resources;

import com.jrackham.R;

import java.util.List;

public class StringUtil {
    public static String setFinalAnd(List<String> productNames) {
        return StringUtil.replaceLast(productNames.toString().substring(1, productNames.toString().length() - 1),
                ",",
                Resources.getSystem().getString(R.string.and) + " ");
    }

    private static String replaceLast(String input, String search, String replace) {
        StringBuilder builder = new StringBuilder(input).reverse();
        String replaced = builder.toString().replaceFirst(search, replace);

        return new StringBuilder(replaced).reverse().toString();
    }

}
