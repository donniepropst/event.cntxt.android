package hackathon.eventcntxt;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hackathon.eventcntxt.models.Beacon;
import hackathon.eventcntxt.models.Event;
import hackathon.eventcntxt.models.Registration;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by donnie on 10/2/16.
 */
public class UserActivity {

    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;


    private List<Registration> registrationsList;
    private List<Event> eventList;
    private List<Beacon> subscribedBeacons;

    private RealmConfiguration realmConfiguration;

    public UserActivity(){


        registrationsList = new ArrayList<>();
        eventList = new ArrayList<>();
        subscribedBeacons = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public  void getAllUserEvents(){
        String email = firebaseAuth.getCurrentUser().getEmail();
        Query q = database.child("registration")
                .orderByChild("email")
                .startAt(email)
                .endAt(email);
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(Object o : dataSnapshot.getChildren()){
                    System.out.println(o.toString());
                }
                registrationsList.add(dataSnapshot.getValue(Registration.class));;
                Registration reg = registrationsList.get(registrationsList.size()-1);
                    getEventData(reg.getEvent());
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

    public void getEventData(String eventId){
        Query q = database.child("events/"+eventId);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.add(dataSnapshot.getValue(Event.class));
                Event retrievedEvent = eventList.get(eventList.size()-1);
                saveEvent(retrievedEvent);
                System.out.println("EVENT!!1" + retrievedEvent.getName());
                getCheckinBeaconData(retrievedEvent.getBeacon());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getCheckinBeaconData(String beaconId){
        Query q = database.child("beacons/"+beaconId);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               subscribedBeacons.add(dataSnapshot.getValue(Beacon.class));
                Beacon beacon = subscribedBeacons.get(subscribedBeacons.size()-1);
                System.out.println("BACON!!1" + beacon.getName());
                saveBeacon(beacon);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveEvent(final Event event){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                Event e = realm.createObject(Event.class);
                e.setBeacon(event.getBeacon());
                e.setCreator(event.getCreator());
                e.setDatetime(event.getDatetime());
                e.setName(event.getName());
            }
        });
    }
    private void saveBeacon(final Beacon beacon){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(beacon);
        realm.commitTransaction();
       /* realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                Beacon b = realm.createObject(Beacon.class);
                b.setUuid(beacon.getUuid());
                b.setMajor(beacon.getMajor());
                b.setMinor(beacon.getMinor());
                b.setName(beacon.getName());
                b.setCreator(beacon.getCreator());
            }
        });*/
    }

}
