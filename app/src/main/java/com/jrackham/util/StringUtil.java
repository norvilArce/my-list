package com.jrackham.util;

public class StringUtil{
    public static String replaceLast(String input, String search, String replace){
        StringBuilder builder = new StringBuilder(input).reverse();
        String replaced = builder.toString().replaceFirst(search, replace);

        return new StringBuilder(replaced).reverse().toString();
    }
}
