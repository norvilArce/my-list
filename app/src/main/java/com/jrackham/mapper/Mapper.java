package com.jrackham.mapper;

import com.jrackham.model.base.Product;
import com.jrackham.model.realm.ProductRealm;

public interface Mapper {
    ProductRealm productToProductRealm(Product product);
}
