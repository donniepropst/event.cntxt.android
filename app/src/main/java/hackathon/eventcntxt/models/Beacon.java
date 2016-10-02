package hackathon.eventcntxt.models;

import io.realm.RealmObject;

/**
 * Created by donnie on 10/2/16.
 */
public class Beacon extends RealmObject {
    private String creator;
    private String major;
    private String minor;
    private String name;
    private String uuid;

    public Beacon() {

    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
