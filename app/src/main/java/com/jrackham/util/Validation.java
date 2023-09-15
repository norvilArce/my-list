package com.jrackham.util;

import com.jrackham.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class Validation {
    public static boolean areCheckBoxesSelectables(List<Product> products) {
        boolean selectable = false;
        List<Boolean> collect = products.stream().map(Product::isSelectable).collect(Collectors.toList());
        for (boolean b : collect) {
            if (b) {
                selectable = true;
                break;
            }
        }
        return selectable;
    }

    public static boolean areCheckBoxesSelected(List<Product> products) {
        boolean selected = false;
        List<Boolean> collect = products.stream().map(Product::isSelected).collect(Collectors.toList());
        for (boolean b : collect) {
            if (b) {
                selected = true;
                break;
            }
        }
        return selected;
    }
}
