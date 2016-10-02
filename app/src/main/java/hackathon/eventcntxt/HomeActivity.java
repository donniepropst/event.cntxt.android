package hackathon.eventcntxt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.estimote.sdk.SystemRequirementsChecker;

import java.net.MalformedURLException;
import java.util.ArrayList;

import hackathon.eventcntxt.models.Beacon;
import hackathon.eventcntxt.models.Event;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by donnie on 10/1/16.
 */



public class HomeActivity extends AppCompatActivity {
    private Realm realm;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        realm = Realm.getDefaultInstance();
        UserActivity ua = new UserActivity();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Just a moment.. ");
        dialog.show();
        ua.getAllUserEvents();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RealmResults<Beacon> res = realm.where(Beacon.class).findAll();
                RealmResults<Event> events = realm.where(Event.class).findAll();
                ArrayList<Event> eventList = new ArrayList<Event>();
                for(int i = 0; i < events.size(); i++){
                    eventList.add(events.get(i));
                }
                dialog.hide();
                startService();
                configureRecyclerView(eventList);

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

    private void configureRecyclerView(ArrayList<Event> list){
        mRecyclerView = (RecyclerView) findViewById(R.id.home_event_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new EventAdapter(list, this);
        mRecyclerView.setAdapter(mAdapter);
    }


}
