package com.jrackham.ui;

import android.view.View;

import com.jrackham.model.Product;

public interface OnProductLongClickListener {
    boolean onProductLongClick(View view, Product product, int position);
}
