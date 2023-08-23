package com.jrackham.logic;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Util {
    private static final String UTIL = "Util";

    public static void closeKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}