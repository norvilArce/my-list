package com.jrackham.persistence;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.jrackham.model.realm.ProductRealm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ProductCRUD {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void addProductRealm(final ProductRealm product) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(product);
        realm.commitTransaction();
    }

    public static List<ProductRealm> getAllProductRealmsSortByPriority() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(ProductRealm.class).findAll().sort("priority");
    }

    public static ProductRealm getProductRealmById(Integer id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(ProductRealm.class).equalTo("id", id).findFirst();
    }

    public static void updateProductRealmById(final ProductRealm product) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ProductRealm currentProductRealm = realm.where(ProductRealm.class).equalTo("id", product.getId()).findFirst();
        if (currentProductRealm != null) {
            currentProductRealm.setName(product.getName());
            currentProductRealm.setPrice(product.getPrice());
            currentProductRealm.setPriority(product.getPriority());
            realm.insertOrUpdate(currentProductRealm);
        }
        realm.commitTransaction();
    }

    public static void updateProductsPriorities(int priority) {
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

    public static void deleteProductRealmById(int id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ProductRealm currentProductRealm = realm.where(ProductRealm.class).equalTo("id", id).findFirst();
        if (currentProductRealm != null) {
            currentProductRealm.deleteFromRealm();
        }
        realm.commitTransaction();
    }
}