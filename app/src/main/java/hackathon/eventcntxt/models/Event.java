package hackathon.eventcntxt.models;

import io.realm.RealmObject;

/**
 * Created by donnie on 10/2/16.
 */
public class Event  extends RealmObject {
    private String name;
    private String datetime;
    private String creator;
    private String beacon;
    private String address;

    public Event(){

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String dateTime) {
       this.datetime = dateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creatorId) {
        this.creator = creatorId;
    }

    public String getBeacon() {
        return beacon;
    }

    public void setBeacon(String beaconId) {
        this.beacon = beaconId;
    }

}
