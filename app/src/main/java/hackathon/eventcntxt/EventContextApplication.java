package hackathon.eventcntxt;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by donnie on 10/2/16.
 */
public class EventContextApplication extends Application{

    @Override
    public void onCreate() {

        super.onCreate();
        /*RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);*/
        Realm.init(this);

    }

}
