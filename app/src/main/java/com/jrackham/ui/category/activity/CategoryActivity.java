package com.jrackham.ui.category.activity;

import static com.jrackham.persistence.realm.service.CategoryService.addCategory;
import static com.jrackham.persistence.realm.service.CategoryService.getAllCategories;
import static com.jrackham.util.UtilKeyboard.clearFocusAndCloseKB;
import static com.jrackham.util.UtilKeyboard.closeKeyboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.jrackham.R;
import com.jrackham.databinding.ActivityCategoryBinding;
import com.jrackham.model.Category;
import com.jrackham.persistence.realm.model.CategoryRealm;
import com.jrackham.persistence.realm.service.CategoryService;
import com.jrackham.ui.category.adapter.CategoryAdapter;
import com.jrackham.ui.category.adapter.OnCategoryClickDeleteListener;
import com.jrackham.ui.category.adapter.OnCategoryLongClickListener;
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
    RecyclerView.LayoutManager layoutManager;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categories = getAllCategories();
        setupView();

        layoutManager = new LinearLayoutManager(this);
        adapter = new CategoryAdapter(this, R.layout.items_categories, mapper.categoriesRealmToCategories(categories), new OnCategoryLongClickListener() {
            @Override
            public boolean onCategoryLongClick(View view, Category category, int position) {
                view.setVisibility(View.VISIBLE);
                return true;
            }
        }, new OnCategoryClickDeleteListener() {
            @Override
            public void onDeleteCategory(Category category, int position) {
                Toast.makeText(CategoryActivity.this, "¿en serio quieres elimnar: "+category.getName()+"?", Toast.LENGTH_SHORT).show();
            }
        });
        mrvCategories.setLayoutManager(layoutManager);
        mrvCategories.setAdapter(adapter);

        mbtnAddCategory.setOnClickListener(this);

        metNameCategory.setText("");
    }

    private void setupView() {
        mbtnAddCategory = binding.btnAddCategory;
        metNameCategory = binding.etCategoryName;
        mrvCategories = binding.rvCategories;
        mtbCategory = binding.tbCategory;

        mtbCategory.setTitle("Categories");
        setSupportActionBar(mtbCategory);
    }

    @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddCategory:
                List<TextInputEditText> fieldsToValidate = new ArrayList<>();
                fieldsToValidate.add(metNameCategory);

                if (UtilValidation.validateEmptyFields(fieldsToValidate)) {
                    String categoryName = metNameCategory.getText().toString();
                    if (!UtilValidation.validateExistCategory(categories, categoryName)) {
                        createCategory();
                        adapter.notifyDataSetChanged();
                        metNameCategory.setText("");
                        closeKeyboard(CategoryActivity.this);
                    } else {
                        Toast.makeText(this, "La categoria " + categoryName + " ya existe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "No has ingresado nada", Toast.LENGTH_SHORT).show();
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