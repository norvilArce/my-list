package com.jrackham.ui;

import android.view.View;
import android.widget.CheckBox;

import com.jrackham.model.Product;

public interface OnProductClickListener {
    boolean onProductClick(CheckBox checkBox, Product product, int position);
}
