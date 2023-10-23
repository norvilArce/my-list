package com.jrackham.util.mapper;

import com.jrackham.model.Product;
import com.jrackham.persistence.realm.model.CategoryRealm;
import com.jrackham.persistence.realm.model.ProductRealm;

import java.util.HashMap;
import java.util.List;

public interface Mapper {
    ProductRealm productToProductRealm(Product product);
    ProductRealm productToProductRealm(int id, Product product);
    Product productRealmToProduct(ProductRealm productRealm);
    List<Product> productsRealmToProducts(List<ProductRealm> productRealms);
    List<ProductRealm> productsToProductsRealm(List<Product> product);

    HashMap<String,Integer> categoryNameAndIdToMap(List<CategoryRealm> categoryRealms);
}
