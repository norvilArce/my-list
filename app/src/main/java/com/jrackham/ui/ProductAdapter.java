package com.jrackham.ui;

import static com.jrackham.persistence.realm.service.CategoryCRUD.getCategoryById;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jrackham.R;
import com.jrackham.persistence.realm.model.CategoryRealm;
import com.jrackham.persistence.realm.model.ProductRealm;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private List<ProductRealm> products;

    public ProductAdapter(Context context, int layout, List<ProductRealm> products) {
        this.context = context;
        this.layout = layout;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ProductAdapter.ViewHolder viewHolder = new ProductAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();

    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;
        private TextView category;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvNameCategory);
            price = itemView.findViewById(R.id.tvNameCategory);
            category = itemView.findViewById(R.id.tvNameCategory);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final ProductRealm product) {

            this.name.setText(product.getName());
            this.price.setText("S/."+product.getPrice().toString());
            CategoryRealm category = getCategoryById(product.getCategoryId());
            this.category.setText(category.getName());
        }
    }

}
