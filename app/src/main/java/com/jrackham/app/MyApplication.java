package com.jrackham.app;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.jrackham.model.realm.CategoryRealm;
import com.jrackham.model.realm.ProductRealm;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyApplication extends Application {
    private static final String DB_NAME = "priorities";

    @Override
    public void onCreate() {
        super.onCreate();
        setUpRealmConfig();
        Realm realm = Realm.getDefaultInstance();
        realm.close();
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
