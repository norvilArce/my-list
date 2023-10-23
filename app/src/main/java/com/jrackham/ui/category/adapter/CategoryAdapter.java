package com.jrackham.ui.category.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jrackham.R;
import com.jrackham.model.Category;
import com.jrackham.persistence.realm.model.CategoryRealm;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private List<Category> categories;
    private OnCategoryLongClickListener longClickListener;
    private OnCategoryClickDeleteListener deleteListener;


    public CategoryAdapter(Context context, int layout, List<Category> categories,
                           OnCategoryLongClickListener longClickListener,
                           OnCategoryClickDeleteListener deleteListener) {
        this.context = context;
        this.layout = layout;
        this.categories = categories;
        this.longClickListener = longClickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(categories.get(position), longClickListener, deleteListener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mtvName;
        private ImageButton mibDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            mtvName = itemView.findViewById(R.id.tvNameCategory);
            mibDelete = itemView.findViewById(R.id.ibDeleteCategory);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final Category category,
                         final OnCategoryLongClickListener longClickListener,
                         final OnCategoryClickDeleteListener deleteListener) {

            this.mtvName.setText(category.getName());

            itemView.setOnLongClickListener(view -> {
                longClickListener.onCategoryLongClick(mibDelete, category, getAdapterPosition());
                return true;
            });

            mibDelete.setOnClickListener(view -> deleteListener.onDeleteCategory(category, getAdapterPosition()));
        }
    }
}
