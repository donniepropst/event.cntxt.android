package hackathon.eventcntxt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.estimote.sdk.SystemRequirementsChecker;
import com.google.firebase.auth.FirebaseAuth;

import hackathon.eventcntxt.models.Beacon;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by donnie on 10/1/16.
 */



public class HomeActivity extends AppCompatActivity {
    private Realm realm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        realm = Realm.getDefaultInstance();
        UserActivity ua = new UserActivity();
        ua.getAllUserEvents();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RealmResults<Beacon> res = realm.where(Beacon.class).findAll();
                System.out.println("TOTAL BEACONS:"+ res.size());
                startService();
               startService();
            }
        }, 5000);
    }

    private void startService(){
        Intent i = new Intent(this, ScanningService.class);
        if(startService(i) != null){
            Toast.makeText(this, "Service Already Running", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Service Not Running", Toast.LENGTH_SHORT).show();
        }
    }
}
