package com.jrackham.app;

import static com.jrackham.util.UtilPreferences.getNumberOfProductsListPreferences;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.jrackham.persistence.realm.model.CategoryRealm;
import com.jrackham.persistence.realm.model.ProductRealm;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MyApplication extends Application {
    private static final String DB_NAME = "priorities";
    public static int NUMBER_OF_PRODUCTS;
    SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        setUpRealmConfig();
        Realm realm = Realm.getDefaultInstance();
        realm.close();
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        NUMBER_OF_PRODUCTS = getNumberOfProductsListPreferences(preferences);
    }

    private void setUpRealmConfig() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(DB_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .allowWritesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private static <T extends RealmObject> AtomicInteger getIdFromTable(Class<T> anyClass) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<T> resultado = realm.where(anyClass).findAll();
        return (resultado.size() > 0) ? new AtomicInteger(resultado.max("id").intValue()) : new AtomicInteger(1);
    }

    public static AtomicInteger getCategoryId() {
        return getIdFromTable(CategoryRealm.class);
    }

    public static AtomicInteger getProductId() {
        return getIdFromTable(ProductRealm.class);
    }
}
