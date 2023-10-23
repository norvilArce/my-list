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
import com.jrackham.persistence.realm.model.CategoryRealm;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private List<CategoryRealm> categories;
    private OnCategoryLongClickListener longClickListener;
    private OnCategoryClickDeleteListener deleteListener;


    public CategoryAdapter(Context context, int layout, List<CategoryRealm> categories, OnCategoryLongClickListener longClickListener, OnCategoryClickDeleteListener deleteListener) {
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
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvNameCategory);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final CategoryRealm category) {

            this.name.setText(category.getName());
        }
    }
}
