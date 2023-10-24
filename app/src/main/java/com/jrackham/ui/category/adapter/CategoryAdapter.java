package com.jrackham.ui.category.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jrackham.R;
import com.jrackham.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private List<Category> categories;
    private OnCategoryLongClickListener longClickListener;
    private DeleteCategoryListener deleteCategoryListener;

    public CategoryAdapter(Context context, int layout, List<Category> categories,
                           OnCategoryLongClickListener longClickListener,
                           DeleteCategoryListener deleteCategoryListener) {
        this.context = context;
        this.layout = layout;
        this.categories = categories;
        this.longClickListener = longClickListener;
        this.deleteCategoryListener = deleteCategoryListener;
    }

    public DeleteCategoryListener getDeleteCategoryListener() {
        return deleteCategoryListener;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(categories.get(position), longClickListener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public long getItemId(int position) {
        return categories.get(position).getId();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mtvName;

        public ViewHolder(View itemView) {
            super(itemView);
            mtvName = itemView.findViewById(R.id.tvNameCategory);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final Category category,
                         final OnCategoryLongClickListener longClickListener) {

            this.mtvName.setText(category.getName());

            itemView.setOnLongClickListener(view -> {
                longClickListener.onCategoryLongClick(category, getAdapterPosition());
                return true;
            });
        }
    }
}
