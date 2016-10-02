package hackathon.eventcntxt.emitter;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by donnie on 10/2/16.
 */
public class CheckInEmitter {
    private String beacon;
    private String event;
    private String name;
    private String email;
    private String datetime;

    public CheckInEmitter(String beacon, String email){
        this.beacon = beacon;
        this.event = event;
        this.name = name;
        this.email = email;
        DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm");
        datetime = df.format(Calendar.getInstance().getTime());
    }

    public void emit(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();


        Map<String, String> kvPair = new HashMap<>();
        kvPair.put("beacon", beacon);
        kvPair.put("event", event);
        kvPair.put("name", name);
        kvPair.put("email", email);
        kvPair.put("time", datetime);

        database.child("checkIns").push().setValue(kvPair);
    }

    public boolean isCheckIn(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    }


}
