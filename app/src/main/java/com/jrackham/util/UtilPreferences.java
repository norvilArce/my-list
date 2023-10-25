package com.jrackham.util;

import android.content.SharedPreferences;

public class UtilPreferences {
    public static int getNumberOfProductsListPreferences(SharedPreferences preferences){
        return preferences.getInt("numberOfProducts",10);
    }
}
