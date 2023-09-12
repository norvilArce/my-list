package com.jrackham.util.mapper;

import com.jrackham.model.Product;
import com.jrackham.persistence.realm.model.ProductRealm;

import java.util.List;

public interface Mapper {
    ProductRealm productToProductRealm(Product product);
    Product productRealmToProduct(ProductRealm productRealm);
    List<Product> productsRealmToProducts(List<ProductRealm> productRealms);
}
