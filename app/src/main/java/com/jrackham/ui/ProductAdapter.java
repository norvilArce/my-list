package com.jrackham.ui;

import static com.jrackham.persistence.realm.service.CategoryCRUD.getCategoryById;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jrackham.R;
import com.jrackham.model.Product;
import com.jrackham.persistence.realm.model.CategoryRealm;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private List<Product> products;
    private OnProductLongClickListener longClickListener;
    private OnProductClickListener clickListener;

    public ProductAdapter(Context context, int layout, List<Product> products,
                          OnProductLongClickListener longClickListener,
                          OnProductClickListener clickListener) {
        this.context = context;
        this.layout = layout;
        this.products = products;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    public void setProducts(List<Product> products) {
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
        holder.bind(products.get(position),  longClickListener, clickListener);
    }

    @Override
    public int getItemCount() {
        return products.size();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mtvName;
        private TextView mtvPrice;
        private TextView mtvCategory;
        private RelativeLayout mrlCheck;
        private CheckBox mcbProductSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            mtvName = itemView.findViewById(R.id.tvNameProduct);
            mtvPrice = itemView.findViewById(R.id.tvProductPrice);
            mtvCategory = itemView.findViewById(R.id.tvCategory);
            mrlCheck = itemView.findViewById(R.id.rlCheck);
            mcbProductSelected = itemView.findViewById(R.id.cbAllProductSelected);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final Product product,
                         final OnProductLongClickListener longClickListener,
                         OnProductClickListener clickListener) {

            this.mtvName.setText(product.getName());
            this.mtvPrice.setText("S/." + product.getPrice().toString());
            CategoryRealm category = getCategoryById(product.getCategoryId());
            this.mtvCategory.setText(category.getName());

            this.mrlCheck.setVisibility((product.isSelectable()) ? View.VISIBLE : View.GONE);
            this.mcbProductSelected.setChecked(product.isSelected());

            //cuando se da click al elemento
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onProductClick(mcbProductSelected, product, getAdapterPosition());
                }
            });

            //cuando se mantiene presionado
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    longClickListener.onProductLongClick(mrlCheck, product, getAdapterPosition());
                    return true;
                }
            });
        }
    }
}

