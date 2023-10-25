package com.jrackham.persistence.realm.model;

import com.jrackham.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductRealm extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private Float price;
    private int priority;
    private int categoryId;

    public ProductRealm() {
    }

    public ProductRealm(String name, Float price, int priority, int categoryId) {
        this.id = MyApplication.getProductId().incrementAndGet();
        this.name = name;
        this.price = price;
        this.priority = priority;
        this.categoryId = categoryId;
    }

    public ProductRealm(String name, Float price, int categoryId) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", priority=" + priority +
                ", categoryId=" + categoryId +
                '}';
    }
}
