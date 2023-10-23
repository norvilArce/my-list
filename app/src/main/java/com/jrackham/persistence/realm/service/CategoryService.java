package com.jrackham.persistence.realm.service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.jrackham.persistence.realm.model.CategoryRealm;
import com.jrackham.persistence.realm.model.ProductRealm;

import java.util.List;

import io.realm.Realm;

public class CategoryService {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void addCategory(final CategoryRealm category) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        CategoryRealm currentCategory = realm.where(CategoryRealm.class).equalTo("name", category.getName()).findFirst();
        if (currentCategory != null) {
            realm.insertOrUpdate(currentCategory);
        } else {
            realm.copyToRealm(category);
        }
        realm.commitTransaction();
    }

    public static List<CategoryRealm> getAllCategories() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(CategoryRealm.class).findAll();
    }

    public static CategoryRealm getCategoryById(Integer id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(CategoryRealm.class).equalTo("id", id).findFirst();
    }

    public static void updateCategoryById(final CategoryRealm category) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        CategoryRealm currentCategory = realm.where(CategoryRealm.class).equalTo("id", category.getId()).findFirst();
        if (currentCategory != null) {
            currentCategory.setName(category.getName());
            realm.insertOrUpdate(currentCategory);
        }
        realm.commitTransaction();
    }

    public static void addProductToCategoryById(final ProductRealm product) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        CategoryRealm currentCategory = realm.where(CategoryRealm.class).equalTo("id", product.getCategoryId()).findFirst();
        if (currentCategory != null) {
            currentCategory.getProducts().add(product);
            realm.insertOrUpdate(currentCategory);
        }
        realm.commitTransaction();
    }

    public static void deleteCategoryRealmById(int id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        CategoryRealm currentCategoryRealm = realm.where(CategoryRealm.class).equalTo("id", id).findFirst();
        if (currentCategoryRealm != null) {
            currentCategoryRealm.deleteFromRealm();
        }
        realm.commitTransaction();
    }
}