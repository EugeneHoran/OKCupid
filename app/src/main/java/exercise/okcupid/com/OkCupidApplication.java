package exercise.okcupid.com;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class OkCupidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("okcupid.realm")
                .schemaVersion(2)
                .build();
        Realm.deleteRealm(config); // Delete Realm Data on app start
        Realm.setDefaultConfiguration(config);
    }
}
