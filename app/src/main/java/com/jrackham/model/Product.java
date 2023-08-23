package com.jrackham.model;

public class Product{
    private String name;
    private Float price;
    private int priority;
    private int categoryId;

    public Product() {

    }

    //metodo para agregar productos a lista fake
    public Product(String name, Float price, int priority, int categoryId) {
        this.name = name;
        this.price = price;
        this.priority = priority;
        this.categoryId = categoryId;
    }
    public Product(String name, Float price, int categoryId) {
        this.name = name;
        this.price = price;
        this.priority = 0;
        this.categoryId = categoryId;
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
                "name='" + name + '\'' +
                ", price=" + price +
                ", priority=" + priority +
                ", categoryId=" + categoryId +
                '}';
    }
}
