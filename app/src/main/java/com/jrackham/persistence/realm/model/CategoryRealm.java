package com.jrackham.persistence.realm.model;

import com.jrackham.app.MyApplication;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CategoryRealm extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private RealmList<ProductRealm> productRealms;

    public CategoryRealm() {
    }

    public CategoryRealm(String name, RealmList<ProductRealm> productRealms) {
        this.id = MyApplication.getCategoryId().incrementAndGet();
        this.name = name;
        this.productRealms = productRealms;
    }

    public CategoryRealm(String name) {
        this.id = MyApplication.getCategoryId().incrementAndGet();
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<ProductRealm> getProducts() {
        return productRealms;
    }

    public void setProducts(RealmList<ProductRealm> productRealms) {
        this.productRealms = productRealms;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", products=" + productRealms +
                '}';
    }
}
