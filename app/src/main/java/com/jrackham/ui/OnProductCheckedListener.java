package com.jrackham.ui;

import android.widget.CheckBox;

import com.jrackham.model.Product;

public interface OnProductCheckedListener {
    boolean onCheck(CheckBox checkBox, boolean isChecked, Product product);
}
