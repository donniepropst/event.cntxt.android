package hackathon.eventcntxt.emitter;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import hackathon.eventcntxt.models.Beacon;
import hackathon.eventcntxt.models.Event;

/**
 * Created by donnie on 10/2/16.
 */
public class CheckInEmitter {
    private String beacon;
    private String eventName;
    private String email;
    private String datetime;

    public CheckInEmitter(String beacon, String email){
        this.beacon = beacon;
        this.eventName = eventName;
        this.email = email;
        DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm");
        datetime = df.format(Calendar.getInstance().getTime());
    }

    public void emit(String key){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        Map<String, String> kvPair = new HashMap<>();
        kvPair.put("beacon", beacon);
        kvPair.put("event", key);
        kvPair.put("email", email);
        kvPair.put("time", datetime);

        database.child("checkIns").push().setValue(kvPair);
    }

    public void getEventKey(String eventName){
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query q = database.child("events").orderByChild("name").startAt(eventName).endAt(eventName);
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                emit(key);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
