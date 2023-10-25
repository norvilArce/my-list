package com.jrackham.ui.category.activity;

import static com.jrackham.persistence.realm.service.CategoryService.addCategory;
import static com.jrackham.persistence.realm.service.CategoryService.getAllCategories;
import static com.jrackham.util.UtilKeyboard.clearFocusAndCloseKB;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.jrackham.R;
import com.jrackham.databinding.ActivityCategoryBinding;
import com.jrackham.model.Category;
import com.jrackham.model.Product;
import com.jrackham.persistence.realm.model.CategoryRealm;
import com.jrackham.persistence.realm.service.CategoryService;
import com.jrackham.ui.category.adapter.CategoryAdapter;
import com.jrackham.ui.category.adapter.SwipeToDeleteCallback;
import com.jrackham.util.UtilValidation;
import com.jrackham.util.mapper.Mapper;
import com.jrackham.util.mapper.MapperImpl;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CategoryActivity";
    Mapper mapper = new MapperImpl();

    ActivityCategoryBinding binding;
    Button mbtnAddCategory;
    TextInputEditText metNameCategory;
    Toolbar mtbCategory;

    private List<CategoryRealm> categories = new RealmList<>();

    //list
    private RecyclerView mrvCategories;
    ItemTouchHelper itemTouchHelper;
    RecyclerView.LayoutManager layoutManager;
    private CategoryAdapter adapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categories = getAllCategories();
        setupView();

        layoutManager = new LinearLayoutManager(this);
        adapter = new CategoryAdapter(this,
                R.layout.items_categories,
                mapper.categoriesRealmToCategories(categories),
                (category, position) -> {
                    Toast.makeText(CategoryActivity.this, getString(R.string.swipe_to_delete), Toast.LENGTH_SHORT).show();
                    return true;
                },
                id -> {
                    Category category = mapper.categoryRealmToCategory(CategoryService.getCategoryById(id));
                    List<Product> products = category.getProducts();
                    String message = "";
                    if (products.size() > 0) {
                        message = getString(R.string.cant_delete_category, category.getName());
                    } else {
                        CategoryService.deleteCategoryRealmById(id);
                        adapter.setCategories(mapper.categoriesRealmToCategories(getAllCategories()));
                        message = getString(R.string.category_deleted, category.getName());
                    }
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                });
        mrvCategories.setLayoutManager(layoutManager);
        mrvCategories.setAdapter(adapter);

        itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(mrvCategories);

        mbtnAddCategory.setOnClickListener(this);

        metNameCategory.setText("");
    }

    private void setupView() {
        mbtnAddCategory = binding.btnAddCategory;
        metNameCategory = binding.etCategoryName;
        mrvCategories = binding.rvCategories;
        mtbCategory = binding.tbCategory;

        mtbCategory.setTitle(getString(R.string.categories));
        setSupportActionBar(mtbCategory);
    }

    @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddCategory:
                List<EditText> fieldsToValidate = new ArrayList<>();
                fieldsToValidate.add(metNameCategory);

                if (!UtilValidation.validateEmptyFields(fieldsToValidate)) {
                    String categoryName = metNameCategory.getText().toString();
                    if (!UtilValidation.validateExistCategory(categories, categoryName)) {
                        createCategory();
                        adapter.setCategories(mapper.categoriesRealmToCategories(getAllCategories()));
                        adapter.notifyDataSetChanged();
                        metNameCategory.setText("");
                    } else {
                        Toast.makeText(this, getString(R.string.category_already_exists, categoryName), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.empty_input), Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        clearFocusAndCloseKB(this, ev);
        return super.dispatchTouchEvent(ev);
    }

    private CategoryRealm createCategory() {
        String name = metNameCategory.getText().toString().trim();
        Log.e(TAG, "name: -> " + name);
        CategoryRealm categoryRealm = new CategoryRealm(name);
        addCategory(categoryRealm);
        return categoryRealm;
    }
}
