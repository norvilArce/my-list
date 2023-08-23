package com.jrackham.util.mapper;

import com.jrackham.model.Product;
import com.jrackham.persistence.realm.model.ProductRealm;

public class MapperImpl implements Mapper{
    @Override
    public ProductRealm productToProductRealm(Product product) {
        String name = product.getName();
        Float price = product.getPrice();
        int priority = product.getPriority();
        int categoryId = product.getCategoryId();
        return new ProductRealm(name, price, priority, categoryId);
    }
}
