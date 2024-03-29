package com.jrackham.ui.product.activity;

import static com.jrackham.util.UtilValidation.areCheckBoxesSelectables;
import static com.jrackham.util.UtilValidation.areCheckBoxesSelected;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jrackham.R;
import com.jrackham.databinding.ActivityMainBinding;
import com.jrackham.model.Product;
import com.jrackham.persistence.realm.model.CategoryRealm;
import com.jrackham.persistence.realm.model.ProductRealm;
import com.jrackham.persistence.realm.service.CategoryService;
import com.jrackham.persistence.realm.service.ProductService;
import com.jrackham.ui.category.activity.CategoryActivity;
import com.jrackham.ui.product.adapter.OnProductClickEditListener;
import com.jrackham.ui.product.adapter.OnProductClickListener;
import com.jrackham.ui.product.adapter.OnProductLongClickListener;
import com.jrackham.ui.product.adapter.ProductAdapter;
import com.jrackham.util.DialogBuilder;
import com.jrackham.util.StringUtil;
import com.jrackham.util.UtilKeyboard;
import com.jrackham.util.UtilValidation;
import com.jrackham.util.mapper.Mapper;
import com.jrackham.util.mapper.MapperImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import io.realm.Realm;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "MainActivity";
    Resources resources;
    Mapper mapper = new MapperImpl();
    SharedPreferences preferences;
    //ui
    ActivityMainBinding binding;
    Button mbtnAddProduct, mbtnNewProduct, mbtnProductValidate, mbtnDone;
    TextInputEditText metNameProduct, metPriceProduct;
    TextInputLayout mtilCategory;
    TextView mtvTotalProducts, mtvTotalAmount;
    ImageButton mimbtnAddCategory, mibDeleteProduct;
    LinearLayout mllProductOptions;
    CheckBox mcbAllProductSelected;
    AutoCompleteTextView mactvCategory;
    ConstraintLayout mclRoot;

    //persistencia
    private Realm realm;
    List<CategoryRealm> categories = new RealmList<>();
    HashMap<String, Integer> categoryMaps = new HashMap<String, Integer>();
    private List<ProductRealm> productRealms = new RealmList<>();

    //list
    private List<Product> products = new ArrayList<>();
    private RecyclerView mrvProducts;
    private ProductAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    int leftLimit = 0;
    int rightLimit = 0;
    int media = 0;
    boolean closeApp = false;
    String and;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);

        categories = CategoryService.getAllCategories();
        //productRealms = getNFirstProductRealmsSortByPriority(NUMBER_OF_PRODUCTS); todo implementar luego
        productRealms = ProductService.getAllProductRealmsSortByPriority();
        products = mapper.productsRealmToProducts(productRealms);
        setupView();

        layoutManager = new LinearLayoutManager(this);
        adapter = new ProductAdapter(this,
                R.layout.items_products, products,
                getOnProductLongClickListener(),
                getOnProductClickListener(),
                getOnProductClickEditListener());

        mrvProducts.setLayoutManager(layoutManager);
        mrvProducts.setAdapter(adapter);

        mbtnAddProduct.setOnClickListener(this);
        mimbtnAddCategory.setOnClickListener(this);
        mibDeleteProduct.setOnClickListener(this);
        mcbAllProductSelected.setOnCheckedChangeListener(this);

        updateViews();
        //setNumberOfProducts(15); todo
    }

    @NonNull
    private OnProductClickEditListener getOnProductClickEditListener() {
        return (product, position) -> {
            showDialogEdit(product);
        };
    }

    @SuppressLint("NotifyDataSetChanged")
    @NonNull
    private OnProductClickListener getOnProductClickListener() {
        return (checkBox, product, position) -> {
            if (areCheckBoxesSelectables(products)) {
                product.setSelected(!checkBox.isChecked());
                adapter.setProducts(products);
                adapter.notifyDataSetChanged();
                //Toast.makeText(MainActivity.this, "p-> " + product.getName(), Toast.LENGTH_SHORT).show();
            }
            return true;
        };
    }

    @SuppressLint("NotifyDataSetChanged")
    @NonNull
    private OnProductLongClickListener getOnProductLongClickListener() {
        return (view, product, position) -> {
            mllProductOptions.setVisibility(View.VISIBLE);
            products.forEach(p -> p.setSelectable(true));

            products.get(position).setSelected(true);
            adapter.setProducts(products);

            adapter.notifyDataSetChanged();
            return true;
        };
    }

    private void showDialogEdit(Product product) {
        Dialog dialog = DialogBuilder.getDialogEdit(MainActivity.this, resources.getString(R.string.edit), product);

        dialog.findViewById(R.id.aceptar).setOnClickListener(view -> {

            String name = ((TextView) dialog.findViewById(R.id.etName)).getText().toString();
            String price = ((TextView) dialog.findViewById(R.id.etPrice)).getText().toString();

            product.setName(name);
            product.setPrice(Float.parseFloat(price));

            ProductService.updateProductRealm(mapper.productToProductRealm(product.getId(), product));
            dialog.dismiss();
            //onBackPressed();
            updateProductsList();
            updateViews();
            UtilKeyboard.hideKeyBoard(MainActivity.this, mclRoot);
            Toast.makeText(MainActivity.this, String.format(resources.getString(R.string.product_edited_with_price), product.getName(), product.getPrice()), Toast.LENGTH_SHORT).show();
        });
        dialog.findViewById(R.id.cancelar).setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        categories = CategoryService.getAllCategories();
        //products = mapper.productsRealmToProducts(getNFirstProductRealmsSortByPriority(NUMBER_OF_PRODUCTS)); todo
        products = mapper.productsRealmToProducts(ProductService.getAllProductRealmsSortByPriority());
        setupView();
        updateViews();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateProductsList() {
        //products = mapper.productsRealmToProducts(getNFirstProductRealmsSortByPriority(NUMBER_OF_PRODUCTS)); todo
        products = mapper.productsRealmToProducts(ProductService.getAllProductRealmsSortByPriority());
        adapter.setProducts(products);
        adapter.notifyDataSetChanged();
        mllProductOptions.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void updateViews() {
        metPriceProduct.setText("");
        metNameProduct.setText("");
        mactvCategory.setText("");

        mtvTotalProducts.setText(String.format(resources.getString(R.string.products), products.size()));
        double totalAmount = productRealms.stream().mapToDouble(ProductRealm::getPrice).sum();
        double amountRound = Math.round(totalAmount * 100.0) / 100.0;
        mtvTotalAmount.setText(String.format(resources.getString(R.string.formatted_amount), amountRound));
    }

    private void goToCategoryActivity() {
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void setupView() {
        mbtnAddProduct = binding.btnAddProduct;
        mrvProducts = binding.rvProducts;
        metNameProduct = binding.etNameProdcuct;
        metPriceProduct = binding.etPriceProdcuct;
        mtilCategory = binding.tilCategory;
        mimbtnAddCategory = binding.imbtnAddCategory;
        mtvTotalProducts = binding.tvTotalProducts;
        mtvTotalAmount = binding.tvTotalAmount;
        mibDeleteProduct = binding.ibDeleteProduct;
        mllProductOptions = binding.llProductOptions;
        mcbAllProductSelected = binding.cbAllProductSelected;
        mactvCategory = binding.actvCategory;
        mclRoot = binding.clRoot;

        List<String> categoryNames = categories.stream().map(CategoryRealm::getName).collect(Collectors.toList());
        mactvCategory.setAdapter(new ArrayAdapter<>(this, R.layout.drop_down_item_categories, categoryNames));

        resources = getResources();
        and = resources.getString(R.string.and);
    }

    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddProduct:
                if (validateEmptyCategoryList()) {
                    showDialogGoToCreateCategory();
                } else {
                    Product product = createProduct();
                    validatePriority(product);
                }
                break;
            case R.id.imbtnAddCategory:
                goToCategoryActivity();
                break;
            case R.id.ibDeleteProduct:
                validateDelete();
                break;
        }
    }

    private boolean validateEmptyCategoryList() {
        return CategoryService.getAllCategories().size() <= 0;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        UtilKeyboard.clearFocusAndCloseKB(this, ev);
        return super.dispatchTouchEvent(ev);
    }

    private void validateDelete() {
        List<Product> listProductsToDelete = getProductsToDelete();
        List<String> productNames = listProductsToDelete.stream().map(Product::getName).collect(Collectors.toList());
        String namesProductsToDelete = StringUtil.setFinalAnd(productNames, and);
        Dialog dialog = DialogBuilder.getDialogConfirm(this,
                resources.getString(R.string.delete),
                String.format(resources.getString(R.string.confirm_delete_products), namesProductsToDelete));

        dialog.findViewById(R.id.aceptar).setOnClickListener(view -> {
            deleteSelectedProducts();
            setNewPriorities();
            updateProductsList();
            updateViews();
            dialog.dismiss();
        });
        dialog.findViewById(R.id.cancelar).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @NonNull
    private List<Product> getProductsToDelete() {
        return products.stream()
                .filter(Product::isSelected)
                .collect(Collectors.toList());
    }

    private void setNewPriorities() {
        ProductService.updatePrioritiesBeforeToDeleteProducts();
        Log.e(TAG, "setNewPriorities: " + products);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBackPressed() {
        if (!closeApp) {
            if (mllProductOptions.getVisibility() == View.VISIBLE) {
                if (areCheckBoxesSelected(products)) {
                    mcbAllProductSelected.setChecked(false);
                    mcbAllProductSelected.setSelected(false);
                    products.forEach(p -> p.setSelected(false));
                    adapter.setProducts(products);
                } else {
                    mllProductOptions.setVisibility(View.GONE);
                    products.forEach(p -> p.setSelectable(false));
                    adapter.setProducts(products);
                }
                adapter.notifyDataSetChanged();
            } else {
                showDialogCloseAppConfirmation();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void showDialogGoToCreateCategory() {
        Dialog dialog = DialogBuilder.getDialogConfirm(this,
                resources.getString(R.string.select_categories),
                resources.getString(R.string.create_category));

        dialog.findViewById(R.id.aceptar).setOnClickListener(view -> {
            goToCategoryActivity();
            dialog.dismiss();
        });
        dialog.findViewById(R.id.cancelar).setVisibility(View.GONE);
        dialog.show();
    }

    @SuppressLint("ResourceAsColor")
    private void showDialogCloseAppConfirmation() {
        Dialog dialog = DialogBuilder.getDialogConfirm(this,
                resources.getString(R.string.exit),
                resources.getString(R.string.exit_confirmation));

        dialog.findViewById(R.id.aceptar).setOnClickListener(view -> {
            closeApp = true;
            dialog.dismiss();
            onBackPressed();
        });
        dialog.findViewById(R.id.cancelar).setOnClickListener(view -> {
            closeApp = false;
            dialog.dismiss();
        });
        dialog.show();
    }

    private void deleteSelectedProducts() {

        List<String> productNames = getProductsToDelete().stream().map(Product::getName).collect(Collectors.toList());
        String namesProductsToDelete = StringUtil.setFinalAnd(productNames, and);

        String toastMessage = String.format(resources.getString(R.string.delete_confirmation), namesProductsToDelete);

        ProductService.deleteProductsRealm(getProductsToDelete().stream().map(Product::getId).collect(Collectors.toList()));

        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }

    private Product createProduct() {

        String name = metNameProduct.getText().toString().trim();
        String price = metPriceProduct.getText().toString().trim();

        String categoryNameSelected = mactvCategory.getText().toString();
        categoryMaps = mapper.categoryNameAndIdToMap(categories);
        Integer categoryIdSelected = categoryMaps.get(categoryNameSelected);
        Log.e(TAG, "categorias: \n" + categories.toString());

        CategoryRealm category = CategoryService.getCategoryById(categoryIdSelected);

        if (UtilValidation.validateEmptyFields(Arrays.asList(metNameProduct, metPriceProduct, mactvCategory)))
            return null;

        return new Product(name, Float.parseFloat(price), category.getId());
    }

    @SuppressLint("NotifyDataSetChanged")
    void validatePriority(Product product) {
        if (product == null) {
            Toast.makeText(this, resources.getString(R.string.some_data_is_not_correct), Toast.LENGTH_SHORT).show();
            return;
        }

        //validar que hay mas de un elemento en la lista sino solo se agrega el producto a la lista
        if (products.size() < 1) {
            product.setPriority(0);
            ProductRealm productRealm = mapper.productToProductRealm(product);
            ProductService.addProductRealm(productRealm);
            CategoryService.addProductToCategoryById(productRealm);
            updateProductsList();
            updateViews();
            updateInitLimits();
        } else {
            showAlertDialogValidatePriority(product);
        }
        adapter.notifyDataSetChanged();
    }

    private void sortProductInList(Product product) {
        //actualiza la prioridad de los productos posteriores al ingresado
        ProductService.updatePrioritiesBeforeToAddProduct(leftLimit);

        product.setPriority(leftLimit);
        ProductRealm productRealm = mapper.productToProductRealm(product);

        //agrega el producto a la db
        ProductService.addProductRealm(productRealm);
        CategoryService.addProductToCategoryById(productRealm);
    }

    private void showAlertDialogValidatePriority(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.alert_validate_priority, null);

        mbtnNewProduct = dialogView.findViewById(R.id.btnNewProduct);
        mbtnProductValidate = dialogView.findViewById(R.id.btnProductValidate);
        mbtnDone = dialogView.findViewById(R.id.btnDone);

        builder.setView(dialogView);
        builder.setTitle(resources.getString(R.string.validation)).setMessage(resources.getString(R.string.validate_priority_message));

        leftLimit = 0;
        rightLimit = products.size() - 1;
        updateButtons(product);

        mbtnNewProduct.setOnClickListener(v -> {
            rightLimit = media - 1;
            boolean validated = validateLimits();
            if (validated) updateButtons(product);
        });
        mbtnProductValidate.setOnClickListener(v -> {
            leftLimit = media + 1;
            boolean validated = validateLimits();
            if (validated) updateButtons(product);
        });
        builder.setCancelable(false)
                .create();

        AlertDialog dialog = builder.show();
        mbtnDone.setOnClickListener(v -> {
            sortProductInList(product);
            updateInitLimits();
            dialog.dismiss();
            updateProductsList();
            updateViews();
            UtilKeyboard.hideKeyBoard(MainActivity.this, mclRoot);
        });
    }

    private void updateInitLimits() {
        leftLimit = 0;
        rightLimit = products.size() - 1;
    }

    private boolean validateLimits() {
        if (rightLimit < leftLimit) {
            mbtnNewProduct.setVisibility(View.GONE);
            mbtnProductValidate.setVisibility(View.GONE);
            mbtnDone.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    private void updateButtons(Product product) {
        media = (int) Math.floor((leftLimit + rightLimit) / 2.0);
        if (media >= 0) {
            mbtnNewProduct.setText(product.getName());
            mbtnProductValidate.setText(products.get(media).getName());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            products.forEach(p -> p.setSelected(true));
        } else {
            products.forEach(p -> p.setSelected(false));
        }
        adapter.setProducts(products);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("ApplySharedPref")
    private void setNumberOfProducts(int n) {
        int numberOfProductsInDB = ProductService.getAllProductRealmsSortByPriority().size();
        if (n > numberOfProductsInDB) n = numberOfProductsInDB;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("numberOfProducts", n);
        editor.commit();//no continua hasta que se cargue
        //editor.apply();//continua sin terminar de cargar
    }
}
