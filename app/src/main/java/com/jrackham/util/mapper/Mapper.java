package com.jrackham.util.mapper;

import com.jrackham.model.Product;
import com.jrackham.persistence.realm.model.ProductRealm;

public interface Mapper {
    ProductRealm productToProductRealm(Product product);
}
