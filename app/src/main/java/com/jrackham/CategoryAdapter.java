package com.jrackham;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jrackham.model.base.Category;
import com.jrackham.model.realm.CategoryRealm;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<CategoryRealm> categories;

    public CategoryAdapter(Context context, int layout, List<CategoryRealm> categories) {
        this.context = context;
        this.layout = layout;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder vh;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(R.layout.items_categories, null);

            vh = new ViewHolder();
            vh.name = convertView.findViewById(R.id.tvNameCategory);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final CategoryRealm category = categories.get(position);

        vh.name.setText(category.getName());

        return convertView;
    }

    static class ViewHolder {
        private TextView name;
    }

}
