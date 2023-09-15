package com.jrackham.persistence.realm.service;

import com.jrackham.persistence.realm.model.CategoryRealm;
import com.jrackham.persistence.realm.model.ProductRealm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ProductCRUD {

    public static void addProductRealm(final ProductRealm product) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(product);
        realm.commitTransaction();
    }

    public static List<ProductRealm> getNFirstProductRealmsSortByPriority(int n) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(ProductRealm.class).findAll().sort("priority").subList(0, n);
    }

    public static List<ProductRealm> getAllProductRealmsSortByPriority() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(ProductRealm.class).findAll().sort("priority");
    }

    public static ProductRealm getProductRealmById(Integer id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(ProductRealm.class).equalTo("id", id).findFirst();
    }

    public static void updateProductRealm(final ProductRealm product) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ProductRealm currentProductRealm = realm.where(ProductRealm.class).equalTo("id", product.getId()).findFirst();
        if (currentProductRealm != null) {
            currentProductRealm.setName(product.getName());
            currentProductRealm.setPrice(product.getPrice());
            currentProductRealm.setPriority(product.getPriority());
            realm.insertOrUpdate(currentProductRealm);
            CategoryRealm categoryRealm = realm.where(CategoryRealm.class).equalTo("id", currentProductRealm.getCategoryId()).findFirst();
            if (categoryRealm != null) {
                categoryRealm.getProducts().remove(product);
                categoryRealm.getProducts().add(currentProductRealm);
            }
        }
        realm.commitTransaction();
    }

    public static void updatePrioritiesBeforeToAddProduct(int priority) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<ProductRealm> productRealms = realm.where(ProductRealm.class).greaterThanOrEqualTo("priority", priority).findAll();
        if (productRealms != null) {
            for (ProductRealm p : productRealms) {
                p.setPriority(p.getPriority() + 1);
                realm.insertOrUpdate(p);
            }
        }
        realm.commitTransaction();
    }

    public static void updatePrioritiesBeforeToDeleteProducts() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<ProductRealm> productRealms = realm.where(ProductRealm.class).findAll().sort("priority");
        if (productRealms != null) {
            for (int i = 0; i < productRealms.size(); i++) {
                ProductRealm productRealm = realm.where(ProductRealm.class).equalTo("id", productRealms.get(i).getId()).findFirst();
                if (productRealm != null)
                    productRealm.setPriority(i);
            }
        }
        realm.commitTransaction();
    }

    public static void deleteProductRealmById(int id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ProductRealm currentProductRealm = realm.where(ProductRealm.class).equalTo("id", id).findFirst();
        if (currentProductRealm != null) {
            currentProductRealm.deleteFromRealm();
        }
        realm.commitTransaction();
    }

    public static void deleteProductsRealm(List<Integer> ids) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (int i = 0; i < ids.size(); i++) {
            ProductRealm currentProductRealm = realm.where(ProductRealm.class).equalTo("id", ids.get(i)).findFirst();
            if (currentProductRealm != null) {
                CategoryRealm categoryRealm = realm.where(CategoryRealm.class).equalTo("id", currentProductRealm.getCategoryId()).findFirst();
                if (categoryRealm != null) {
                    categoryRealm.getProducts().remove(currentProductRealm);
                    currentProductRealm.deleteFromRealm();
                }
            }
        }
        realm.commitTransaction();
    }
}