package com.jrackham.ui.product.adapter;

import static com.jrackham.persistence.realm.service.CategoryService.getCategoryById;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
    private OnProductClickEditListener clickEditListener;

    public ProductAdapter(Context context, int layout, List<Product> products,
                          OnProductLongClickListener longClickListener,
                          OnProductClickListener clickListener,
                          OnProductClickEditListener clickEditListener) {
        this.context = context;
        this.layout = layout;
        this.products = products;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
        this.clickEditListener = clickEditListener;
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
        holder.bind(products.get(position), longClickListener, clickListener
                , clickEditListener);
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
        private ImageButton mibEditProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            mtvName = itemView.findViewById(R.id.tvNameProduct);
            mtvPrice = itemView.findViewById(R.id.tvProductPrice);
            mtvCategory = itemView.findViewById(R.id.tvCategory);
            mrlCheck = itemView.findViewById(R.id.rlCheck);
            mcbProductSelected = itemView.findViewById(R.id.cbAllProductSelected);
            mibEditProduct = itemView.findViewById(R.id.ibEditProduct);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final Product product,
                         final OnProductLongClickListener longClickListener,
                         final OnProductClickListener clickListener,
                         final OnProductClickEditListener clickEditListener) {

            this.mtvName.setText(product.getName());
            this.mtvPrice.setText("S/." + product.getPrice().toString());
            CategoryRealm category = getCategoryById(product.getCategoryId());
            this.mtvCategory.setText(category.getName());

            this.mrlCheck.setVisibility((product.isSelectable()) ? View.VISIBLE : View.GONE);
            this.mibEditProduct.setVisibility((product.isSelected()) ? View.VISIBLE : View.GONE);
            this.mcbProductSelected.setChecked(product.isSelected());

            //cuando se da click al elemento
            itemView.setOnClickListener(v -> clickListener.onProductClick(mcbProductSelected, product, getAdapterPosition()));

            //cuando se mantiene presionado
            itemView.setOnLongClickListener(v -> {
                longClickListener.onProductLongClick(mrlCheck, product, getAdapterPosition());
                return true;
            });

            //cuando se da click al boton editar
            mibEditProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickEditListener.onEditProduct(product, getAdapterPosition());
                }
            });
        }
    }
}
