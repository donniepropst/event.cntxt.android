package hackathon.eventcntxt;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.UUID;

import hackathon.eventcntxt.emitter.CheckInEmitter;
import hackathon.eventcntxt.models.Beacon;
import hackathon.eventcntxt.models.Event;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Donnie Propst on 3/31/2016.
 */
public class ScanningService extends Service {

    private BeaconManager beaconManager;
    public static boolean inRange = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private final String CHECK_IN = "CHECK IN";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scan();

        return START_STICKY;
    }
    public void scan(){
        final Realm realm = Realm.getDefaultInstance();
        final RealmResults<Beacon> res = realm.where(Beacon.class).findAll();
        final RealmResults<Event> events = realm.where(Event.class).findAll();
        final Beacon first = res.get(0);
        final Event event = events.get(0);
        System.out.println("UUID: " + first.getUuid());
        System.out.println("OBJ: " + first.toString());
            if(first != null) {
                beaconManager = new BeaconManager(ScanningService.this);
                beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                    @Override
                    public void onServiceReady() {
                        for(int i = 0; i < res.size(); i ++){
                            beaconManager.startMonitoring(new Region(
                                "event beacon",
                                UUID.fromString(res.get(i).getUuid()), Integer.parseInt(res.get(i).getMajor()), Integer.parseInt(res.get(i).getMinor()))

                            );
                        }
                    }
                });
                beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
                    @Override
                    public void onEnteredRegion(Region region, List<com.estimote.sdk.Beacon> list) {
                        inRange = true;
                        new CheckInEmitter(region.getProximityUUID().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()).getEventKey(event.getName());
                        showNotification("Event - Check In", "Click here to show proper check in", true);
                    }

                    @Override
                    public void onExitedRegion(Region region) {
                        inRange = false;
                    }
                });

            }



    }

    public void showNotification( String title, String message, boolean isCheckin) {
        Intent notifyIntent;
        if(isCheckin)
            notifyIntent = new Intent(this, CheckInActivity.class);
        else
            notifyIntent = new Intent(this, HomeActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_beenhere_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
