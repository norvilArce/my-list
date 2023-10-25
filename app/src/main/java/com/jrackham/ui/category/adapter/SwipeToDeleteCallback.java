package com.jrackham.ui.category.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.jrackham.ui.category.adapter.CategoryAdapter;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private final CategoryAdapter adapter;

    public SwipeToDeleteCallback(CategoryAdapter adapter) {
        super(0, ItemTouchHelper.RIGHT); // Permite deslizar solo hacia la derecha
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false; // No permitir arrastre
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        int categoryId = (int) adapter.getItemId(position);

        adapter.getDeleteCategoryListener().onDeleteCategory(categoryId);
    }
}

