package com.jrackham.ui;

import static com.jrackham.persistence.realm.service.CategoryCRUD.addProductToCategoryById;
import static com.jrackham.persistence.realm.service.CategoryCRUD.getAllCategories;
import static com.jrackham.persistence.realm.service.ProductCRUD.addProductRealm;
import static com.jrackham.persistence.realm.service.ProductCRUD.deleteProductsRealm;
import static com.jrackham.persistence.realm.service.ProductCRUD.getAllProductRealmsSortByPriority;
import static com.jrackham.persistence.realm.service.ProductCRUD.updatePrioritiesBeforeToAddProduct;
import static com.jrackham.persistence.realm.service.ProductCRUD.updatePrioritiesBeforeToDeleteProducts;
import static com.jrackham.util.Util.closeKeyboard;
import static com.jrackham.util.Validation.areCheckBoxesSelectables;
import static com.jrackham.util.Validation.areCheckBoxesSelected;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jrackham.R;
import com.jrackham.databinding.ActivityMainBinding;
import com.jrackham.model.Product;
import com.jrackham.persistence.realm.model.CategoryRealm;
import com.jrackham.persistence.realm.model.ProductRealm;
import com.jrackham.util.mapper.Mapper;
import com.jrackham.util.mapper.MapperImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.realm.Realm;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "MainActivity";
    Mapper mapper = new MapperImpl();
    SharedPreferences preferences;
    //ui
    ActivityMainBinding binding;
    Button mbtnAddProduct, mbtnNewProduct, mbtnProductValidate, mbtnDone;
    EditText metNameProduct, metPriceProduct;
    Spinner mspnCategory;
    TextView mtvTotalProducts, mtvTotalAmount;
    ImageButton mimbtnAddCategory, mibDeleteProduct;
    LinearLayout mllProductOptions;
    CheckBox mcbAllProductSelected;

    //persistencia
    private Realm realm;
    List<CategoryRealm> categories = new RealmList<>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);

        categories = getAllCategories();
        //productRealms = getNFirstProductRealmsSortByPriority(NUMBER_OF_PRODUCTS); todo
        productRealms = getAllProductRealmsSortByPriority();
        products = mapper.productsRealmToProducts(productRealms);
        setupView();

        layoutManager = new LinearLayoutManager(this);
        adapter = new ProductAdapter(this, R.layout.items_products, products, new OnProductLongClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onProductLongClick(View view, Product product, int position) {
                mllProductOptions.setVisibility(View.VISIBLE);
                products.forEach(p -> p.setSelectable(true));
                products.get(position).setSelected(true);
                adapter.setProducts(products);
                adapter.notifyDataSetChanged();
                return true;
            }
        }, new OnProductClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onProductClick(CheckBox checkBox, Product product, int position) {
                if (areCheckBoxesSelectables(products)) {
                    product.setSelected(!checkBox.isChecked());
                    adapter.setProducts(products);
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        mrvProducts.setLayoutManager(layoutManager);
        mrvProducts.setAdapter(adapter);

        mbtnAddProduct.setOnClickListener(this);
        mimbtnAddCategory.setOnClickListener(this);
        mibDeleteProduct.setOnClickListener(this);
        mcbAllProductSelected.setOnCheckedChangeListener(this);

        updateViews();
        //setNumberOfProducts(15); todo
    }

    @Override
    protected void onResume() {
        super.onResume();
        categories = getAllCategories();
        //products = mapper.productsRealmToProducts(getNFirstProductRealmsSortByPriority(NUMBER_OF_PRODUCTS)); todo
        products = mapper.productsRealmToProducts(getAllProductRealmsSortByPriority());
        setupView();
        updateViews();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateProductsList() {
        //products = mapper.productsRealmToProducts(getNFirstProductRealmsSortByPriority(NUMBER_OF_PRODUCTS)); todo
        products = mapper.productsRealmToProducts(getAllProductRealmsSortByPriority());
        adapter.setProducts(products);
        adapter.notifyDataSetChanged();
        mllProductOptions.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void updateViews() {
        metPriceProduct.setText("");
        metNameProduct.setText("");

        mtvTotalProducts.setText(products.size() + " mas importantes");
        double totalAmount = productRealms.stream().mapToDouble(ProductRealm::getPrice).sum();
        double amountRound = Math.round(totalAmount * 100.0) / 100.0;
        mtvTotalAmount.setText("S/. " + amountRound);
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
        mspnCategory = binding.spnCategory;
        mimbtnAddCategory = binding.imbtnAddCategory;
        mtvTotalProducts = binding.tvTotalProducts;
        mtvTotalAmount = binding.tvTotalAmount;
        mibDeleteProduct = binding.ibDeleteProduct;
        mllProductOptions = binding.llProductOptions;
        mcbAllProductSelected = binding.cbAllProductSelected;
        List<String> categoryNames = categories.stream().map(CategoryRealm::getName).collect(Collectors.toList());
        mspnCategory.setAdapter(new ArrayAdapter<>(this, R.layout.items_spinner_categories, categoryNames));
    }

    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddProduct:
                Product product = createProduct();
                validatePriority(product);
                break;
            case R.id.imbtnAddCategory:
                goToCategoryActivity();
                break;
            case R.id.ibDeleteProduct:
                deleteSelectedProducts();
                setNewPriorities();
                updateProductsList();
                updateViews();
                break;
        }
    }

    private void setNewPriorities() {
        updatePrioritiesBeforeToDeleteProducts();
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
                showAlertDialogCloseAppConfirmation();
            }
        }else {
            super.onBackPressed();
        }
    }

    private void showAlertDialogCloseAppConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Salir").setMessage("¿Realmente deseas salir de la aplicacion?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        closeApp = true;
                        onBackPressed();
                    }
                }).setNegativeButton("Cancelar", null)
                .create()
                .show();
    }

    private void deleteSelectedProducts() {
        List<Product> productsThatWillDelete = products.stream().filter(Product::isSelected).collect(Collectors.toList());
        Toast.makeText(this, "Se eliminaran los productos: " + productsThatWillDelete.stream().map(Product::getId).collect(Collectors.toList()), Toast.LENGTH_LONG).show();
        deleteProductsRealm(productsThatWillDelete.stream().map(Product::getId).collect(Collectors.toList()));
    }

    private Product createProduct() {

        String name = metNameProduct.getText().toString().trim();
        String price = metPriceProduct.getText().toString().trim();

        int selectedItemPosition = mspnCategory.getSelectedItemPosition();
        CategoryRealm category = categories.get(selectedItemPosition);

        if (name.equalsIgnoreCase("") || price.equalsIgnoreCase("")) return null;

        return new Product(name, Float.parseFloat(price), category.getId());
    }

    @SuppressLint("NotifyDataSetChanged")
    void validatePriority(Product product) {
        if (product == null) {
            Toast.makeText(this, "agrega bien", Toast.LENGTH_SHORT).show();
            return;
        }

        //validar que hay mas de un elemento en la lista sino solo se agrega el producto a la lista
        if (products.size() < 1) {
            product.setPriority(0);
            ProductRealm productRealm = mapper.productToProductRealm(product);
            addProductRealm(productRealm);
            addProductToCategoryById(productRealm);
            updateInitLimits();
        } else {
            showAlertDialogValidatePriority(product);
        }
        adapter.notifyDataSetChanged();
    }

    private void sortProductInList(Product product) {
        //actualiza la prioridad de los productos posteriores al ingresado
        updatePrioritiesBeforeToAddProduct(leftLimit);

        product.setPriority(leftLimit);
        ProductRealm productRealm = mapper.productToProductRealm(product);

        //agrega el producto a la db
        addProductRealm(productRealm);
    }

    private void showAlertDialogValidatePriority(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.alert_validate_priority, null);

        mbtnNewProduct = dialogView.findViewById(R.id.btnNewProduct);
        mbtnProductValidate = dialogView.findViewById(R.id.btnProductValidate);
        mbtnDone = dialogView.findViewById(R.id.btnDone);

        builder.setView(dialogView);
        builder.setTitle("Validación").setMessage("¿Que producto es mas importante?");

        leftLimit = 0;
        rightLimit = products.size() - 1;
        updateButtons(product);

        mbtnNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightLimit = media - 1;
                boolean validated = validateLimits();
                if (validated) updateButtons(product);
            }
        });
        mbtnProductValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftLimit = media + 1;
                boolean validated = validateLimits();
                if (validated) updateButtons(product);
            }
        });
        builder.setCancelable(false)
                .create();

        AlertDialog dialog = builder.show();
        mbtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortProductInList(product);
                updateInitLimits();
                dialog.dismiss();
                updateProductsList();
                updateViews();
                closeKeyboard(MainActivity.this);
            }
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
        int numberOfProductsInDB = getAllProductRealmsSortByPriority().size();
        if (n > numberOfProductsInDB) n = numberOfProductsInDB;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("numberOfProducts", n);
        editor.commit();//no continua hasta que se cargue
        //editor.apply();//continua sin terminar de cargar
    }
}
