package com.jrackham.mapper;

import com.jrackham.model.base.Product;
import com.jrackham.model.realm.ProductRealm;

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
