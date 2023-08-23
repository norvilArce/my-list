package com.jrackham;

import static com.jrackham.persistence.CategoryCRUD.getCategoryById;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jrackham.model.base.Product;
import com.jrackham.model.realm.CategoryRealm;
import com.jrackham.model.realm.ProductRealm;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<ProductRealm> products;

    public ProductAdapter(Context context, int layout, List<ProductRealm> products) {
        this.context = context;
        this.layout = layout;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder vh;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(R.layout.items_products, null);

            vh = new ViewHolder();
            vh.name = convertView.findViewById(R.id.tvNameProduct);
            vh.price = convertView.findViewById(R.id.tvProductPrice);
            vh.category = convertView.findViewById(R.id.tvCategory);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final ProductRealm product = products.get(position);

        vh.name.setText(product.getName());
        vh.price.setText("S/."+product.getPrice().toString());
        CategoryRealm category = getCategoryById(product.getCategoryId());
        vh.category.setText(category.getName());

        return convertView;
    }

    static class ViewHolder {
        private TextView name;
        private TextView price;
        private TextView category;
    }

}
