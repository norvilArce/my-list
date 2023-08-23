package com.jrackham;

import static com.jrackham.logic.Util.closeKeyboard;
import static com.jrackham.persistence.CategoryCRUD.addCategory;
import static com.jrackham.persistence.CategoryCRUD.getAllCategories;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.jrackham.databinding.ActivityCategoryBinding;
import com.jrackham.model.realm.CategoryRealm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CategoryActivity";

    ActivityCategoryBinding binding;
    Button mbtnAddCategory;

    EditText metNameCategory;

    private List<CategoryRealm> categories = new RealmList<>();
    //List<CategoryRealm> categories= new ArrayList<>();

    //list
    private ListView mlvCategories;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categories = getAllCategories();
        setupView();

        adapter = new CategoryAdapter(this, R.layout.items_categories, categories);
        mlvCategories.setAdapter(adapter);

        mbtnAddCategory.setOnClickListener(this);

        metNameCategory.setText("");
    }

    private void setupView() {
        mbtnAddCategory = binding.btnAddCategory;
        metNameCategory = binding.etNameCategory;
        mlvCategories = binding.lvCategories;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddCategory:
                createCategory();
                adapter.notifyDataSetChanged();
                metNameCategory.setText("");
                closeKeyboard(CategoryActivity.this);
        }
    }

    private CategoryRealm createCategory() {
        /*Integer id = 1;
        if (categories.size() > 0) {
            List<Integer> ids = categories.stream().map(CategoryRealm::getId).collect(Collectors.toList());
            Log.e(TAG, "ids: -> " + ids);
            id += ids.stream().max(Integer::compare).get();
            Log.e(TAG, "id max: -> " + id);
        }*/
        String name = metNameCategory.getText().toString().trim();
        Log.e(TAG, "name: -> " + name);
        CategoryRealm categoryRealm = new CategoryRealm(name);
        addCategory(categoryRealm);
        return categoryRealm;
    }
}