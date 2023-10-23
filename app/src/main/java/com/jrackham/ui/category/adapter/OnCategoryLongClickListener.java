package com.jrackham.ui.category.adapter;

import android.view.View;

import com.jrackham.model.Category;

public interface OnCategoryLongClickListener {
    boolean onCategoryLongClick(View view, Category category, int position);
}
