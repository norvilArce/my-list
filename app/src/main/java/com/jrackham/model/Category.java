package com.jrackham.model;

import java.util.List;

public class Category {
    private int id;
    private String name;
    private List<Product> products;
    private boolean isDeletable;

    public Category() {
    }

    public Category(int id, String name, List<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
        this.isDeletable = false;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
        this.isDeletable = false;
    }

    public boolean isDeletable() {
        return isDeletable;
    }

    public void setDeletable(boolean deletable) {
        isDeletable = deletable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", products=" + products +
                ", isDeletable=" + isDeletable +
                '}';
    }
}
