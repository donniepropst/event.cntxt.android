package hackathon.eventcntxt;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyft.lyftbutton.LyftButton;
import com.lyft.lyftbutton.RideParams;
import com.lyft.lyftbutton.RideTypeEnum;
import com.lyft.networking.ApiConfig;

import java.util.ArrayList;
import java.util.List;

import hackathon.eventcntxt.models.Event;

/**
 * Created by donnie on 10/2/16.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private ArrayList<Event> eventList;
    private Context c;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView addressTextView;
        public LyftButton lyftButton;


        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.item_event_name);
            addressTextView = (TextView)v.findViewById(R.id.item_event_address);
            lyftButton = (LyftButton)v.findViewById(R.id.lyft_button);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventAdapter(ArrayList<Event> eventList, Context c) {
       this.eventList = eventList;
        this.c = c;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTextView.setText(eventList.get(position).getName());
        holder.addressTextView.setText(eventList.get(position).getAddress());

        LocationManager locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        config(holder.lyftButton, eventList.get(position).getAddress(), lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return eventList.size();
    }


    private void config(LyftButton lyftButton, String endAddress, double lat, double lon){
        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("ffKJbtvcBhHA")
                .setClientToken("gAAAAABX8Rxcr5BZwxBDtd1Jggcz2FTQZ2vJSOW3BpnMVJCyNJPi_RxH2JUwWiwCE4Aj0TICGTJ_MzlAFVpaN5o7MOvC2Thxes56_3bLuUEc3mCZ5OZyO-vXSGEm6cVMnwCkYpN14JvzKZbKJUlOQuFe_NFTsJi9NVRzHCOncuqeHNiAGMolxxQ=")
                .build();

        lyftButton.setApiConfig(apiConfig);

        RideParams.Builder rideParamsBuilder = new RideParams.Builder()
                .setPickupLocation(lat, lon)
                .setDropoffAddress(endAddress);
        rideParamsBuilder.setRideTypeEnum(RideTypeEnum.CLASSIC);

        lyftButton.setRideParams(rideParamsBuilder.build());
        lyftButton.load();
    }
}


