package hackathon.eventcntxt.models;

import io.realm.RealmObject;

/**
 * Created by donnie on 10/2/16.
 */
public class Registration extends RealmObject {
    private String email;
    private String event;
    private String name;
    private String phone;


    public Registration(){

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
