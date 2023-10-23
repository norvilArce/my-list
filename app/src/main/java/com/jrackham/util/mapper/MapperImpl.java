package com.jrackham.util.mapper;

import com.jrackham.model.Category;
import com.jrackham.model.Product;
import com.jrackham.persistence.realm.model.CategoryRealm;
import com.jrackham.persistence.realm.model.ProductRealm;

import java.util.ArrayList;
import java.util.HashMap;
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
    public ProductRealm productToProductRealm(int id, Product product) {
        String name = product.getName();
        Float price = product.getPrice();
        int priority = product.getPriority();
        int categoryId = product.getCategoryId();
        ProductRealm productRealm = new ProductRealm(name, price, priority, categoryId);
        productRealm.setId(id);
        return productRealm;
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

    @Override
    public HashMap<String,Integer> categoryNameAndIdToMap(List<CategoryRealm> categoryRealms) {
        HashMap<String,Integer> maps = new HashMap<String,Integer>();
        for (CategoryRealm c : categoryRealms) {
            maps.put(c.getName(), c.getId());
        }
        return maps;
    }

    @Override
    public Category categoryRealmToCategory(CategoryRealm categoryRealm) {
        Category category = new Category();
        category.setId(categoryRealm.getId());
        category.setName(categoryRealm.getName());
        category.setProducts(productsRealmToProducts(categoryRealm.getProducts()));
        return category;
    }

    @Override
    public List<Category> categoriesRealmToCategories(List<CategoryRealm> categoryRealms) {
        List<Category> categories = new ArrayList<>();
        for (CategoryRealm c:       categoryRealms      ){
            Category category = categoryRealmToCategory(c);
            categories.add(category);
        }
        return categories;
    }

    @Override
    public CategoryRealm categoryToCategoryRealm(Category category) {
        CategoryRealm categoryRealm = new CategoryRealm();
        categoryRealm.setId(category.getId());
        categoryRealm.setName(category.getName());
        categoryRealm.setProducts((RealmList<ProductRealm>) productsToProductsRealm(category.getProducts()));
        return categoryRealm;
    }

    @Override
    public List<CategoryRealm> categoriesToCategoriesRealm(List<Category> categories) {
        List<CategoryRealm> categoryRealms = new ArrayList<>();
        for (Category c:           categories  ){
            CategoryRealm categoryRealm = categoryToCategoryRealm(c);
            categoryRealms.add(categoryRealm);
        }
        return categoryRealms;
    }
}
