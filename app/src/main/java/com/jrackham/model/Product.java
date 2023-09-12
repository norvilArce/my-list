package com.jrackham.model;

public class Product{
    private int id;
    private String name;
    private Float price;
    private int priority;
    private int categoryId;

    private boolean isSelectable;
    private boolean isSelected;

    public Product(String name, Float price, int categoryId) {
        this.name = name;
        this.price = price;
        this.priority = 0;
        this.categoryId = categoryId;
        this.isSelectable = false;
    }

    public Product(int id,String name, Float price, int priority, int categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.priority = priority;
        this.categoryId = categoryId;
        this.isSelectable = false;
        this.isSelected = false;
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

    public boolean isSelectable() {
        return isSelectable;
    }

    public void setSelectable(boolean selectable) {
        isSelectable = selectable;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
