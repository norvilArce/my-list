package com.jrackham.ui;

import static com.jrackham.persistence.realm.service.CategoryCRUD.addProductToCategoryById;
import static com.jrackham.persistence.realm.service.CategoryCRUD.getAllCategories;
import static com.jrackham.persistence.realm.service.ProductCRUD.addProductRealm;
import static com.jrackham.persistence.realm.service.ProductCRUD.getAllProductRealmsSortByPriority;
import static com.jrackham.persistence.realm.service.ProductCRUD.updateProductsPriorities;
import static com.jrackham.util.Util.closeKeyboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categories = getAllCategories();
        productRealms = getAllProductRealmsSortByPriority();
        products = mapper.productsRealmToProducts(productRealms);
        setupView();

        layoutManager = new LinearLayoutManager(this);
        adapter = new ProductAdapter(this, R.layout.items_products, products, new OnProductClickDeleteListener() {
            @Override
            public void onDeleteProduct(Product product, int position) {
                Toast.makeText(MainActivity.this, "Este producto se borrara-> " + product.getName() + ", priority: " + product.getPriority(), Toast.LENGTH_LONG).show();
            }
        }, new OnProductLongClickListener() {
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
                if (areCheckBoxesSelectables()) {
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
    }

    private boolean areCheckBoxesSelectables() {
        boolean selectable = false;
        List<Boolean> collect = products.stream().map(Product::isSelectable).collect(Collectors.toList());
        for (boolean b : collect) {
            if (b) {
                selectable = true;
                break;
            }
        }
        return selectable;
    }
    private boolean areCheckBoxesSelected() {
        boolean selected = false;
        List<Boolean> collect = products.stream().map(Product::isSelected).collect(Collectors.toList());
        for (boolean b : collect) {
            if (b) {
                selected = true;
                break;
            }
        }
        return selected;
    }

    @Override
    protected void onResume() {
        super.onResume();
        categories = getAllCategories();
        products = mapper.productsRealmToProducts(getAllProductRealmsSortByPriority());
        setupView();
        updateViews();
    }

    @SuppressLint("SetTextI18n")
    private void updateViews() {
        metPriceProduct.setText("");
        metNameProduct.setText("");

        mtvTotalProducts.setText(products.size() + " productos: ");
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
                break;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBackPressed() {
        if (mllProductOptions.getVisibility()==View.VISIBLE){
            if (areCheckBoxesSelected()) {
                mcbAllProductSelected.setChecked(false);
                mcbAllProductSelected.setSelected(false);
                products.forEach(p -> p.setSelected(false));
                adapter.setProducts(products);
            } else{
                mllProductOptions.setVisibility(View.GONE);
                products.forEach(p -> p.setSelectable(false));
                adapter.setProducts(products);
            }
            adapter.notifyDataSetChanged();
        }else{
            super.onBackPressed();
        }
    }
    private void deleteSelectedProducts() {
        List<Product> productsThatWillDelete = products.stream().filter(Product::isSelected).collect(Collectors.toList());
        Toast.makeText(this, "Se eliminaran los productos: " + productsThatWillDelete.stream().map(Product::getName).collect(Collectors.toList()), Toast.LENGTH_LONG).show();
    }

    private Product createProduct() {

        String name = metNameProduct.getText().toString().trim();
        String price = metPriceProduct.getText().toString().trim();
        Log.e(TAG, "name: -> " + name);
        Log.e(TAG, "price: -> " + price);

        int selectedItemPosition = mspnCategory.getSelectedItemPosition(); //FIXME buscar por nombre
        Log.e(TAG, "selectedItemPosition: -> " + selectedItemPosition);
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

        //saludo
        Log.e(TAG, "\n\n");
        Log.e(TAG, "Vamos a registar lo que necesitas comprar...");
        Log.e(TAG, "... y ordenar los productos segun su prioridad");
        Log.e(TAG, "...");
        Log.e(TAG, "...");
        Log.e(TAG, "Ikuzo!!");
        Log.e(TAG, "\n\n");

        //validar que hay mas de un elemento en la lista sino solo se agrega el producto a la lista
        if (products.size() < 1) {
            product.setPriority(0);
            ProductRealm productRealm = mapper.productToProductRealm(product);
            addProductRealm(productRealm);
            addProductToCategoryById(productRealm);
            updateInitLimits();
        } else {
            Log.d(TAG, "abriendo alert dialog... ");
            showAlertDialogValidatePriority(product);
        }
        adapter.notifyDataSetChanged();
        Log.d(TAG, products.toString());
    }

    private void sortProductInList(Product product) {

        Log.d(TAG, "leftLimit final: " + leftLimit);
        Log.d(TAG, "rightLimit final: " + rightLimit);
        Log.d(TAG, "media final: " + media);

        updateProductsPriorities(leftLimit);//actualiza la prioridad de los productos posteriores al ingresado

        product.setPriority(leftLimit);
        ProductRealm productRealm = mapper.productToProductRealm(product);

        addProductRealm(productRealm);//agrega el producto a la db
        Log.e(TAG, "esta es la lista saliendo despues de ordenar:\n" + products);
    }

    private void showAlertDialogValidatePriority(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.alert_validate_priority, null);

        mbtnNewProduct = dialogView.findViewById(R.id.btnNewProduct);
        mbtnProductValidate = dialogView.findViewById(R.id.btnProductValidate);
        mbtnDone = dialogView.findViewById(R.id.btnDone);

        builder.setView(dialogView);
        builder.setTitle("Validación").setMessage("¿Que producto es mas importante?");

        Log.d(TAG, "Ok, ordenemos el nuevo producto por orden de prioridad");
        //repetir hasta que no hayan elementos para comparar
        //mostrar elemento central y comparar con el producto ingresado
        Log.e(TAG, "prducto: " + product.getName());
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
                products = mapper.productsRealmToProducts(getAllProductRealmsSortByPriority());
                updateViews();
                closeKeyboard(MainActivity.this);
            }
        });
    }

    private void updateInitLimits() {
        leftLimit = 0;
        rightLimit = products.size() - 1;
        Log.e(TAG, "leftLimit init: " + leftLimit);
        Log.e(TAG, "rightLimit init: " + rightLimit);
        Log.e(TAG, "media init: " + media);
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
        Log.e(TAG, "leftLimit: " + leftLimit);
        Log.e(TAG, "rightLimit: " + rightLimit);
        Log.e(TAG, "media: " + media);
        if (media >= 0) {
            mbtnNewProduct.setText(product.getName());
            mbtnProductValidate.setText(products.get(media).getName());//todo validar si es products o productRealms
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
}