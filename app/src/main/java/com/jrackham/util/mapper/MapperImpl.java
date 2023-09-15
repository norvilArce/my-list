package com.jrackham.util.mapper;

import com.jrackham.model.Product;
import com.jrackham.persistence.realm.model.ProductRealm;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class MapperImpl implements Mapper {
    @Override
    public ProductRealm productToProductRealm(Product product) {
        String name = product.getName();
        Float price = product.getPrice();
        int priority = product.getPriority();
        int categoryId = product.getCategoryId();
        return new ProductRealm(name, price, priority, categoryId);
    }

    @Override
    public Product productRealmToProduct(ProductRealm productRealm) {
        int id = productRealm.getId();
        String name = productRealm.getName();
        Float price = productRealm.getPrice();
        int priority = productRealm.getPriority();
        int categoryId = productRealm.getCategoryId();
        return new Product(id, name, price, priority, categoryId);
    }

    @Override
    public List<Product> productsRealmToProducts(List<ProductRealm> productRealms) {
        List<Product> products = new ArrayList<>();
        for (ProductRealm productRealm : productRealms) {
            Product product = productRealmToProduct(productRealm);
            products.add(product);
        }
        return products;
    }

    @Override
    public List<ProductRealm> productsToProductsRealm(List<Product> products) {
        List<ProductRealm> productsRealm = new ArrayList<>();
        for (Product product : products) {
            ProductRealm productRealm = productToProductRealm(product);
            productsRealm.add(productRealm);
        }
        return productsRealm;
    }
}
