package umd.letsgo;

import java.io.Serializable;

/**
 * Created by jeffsadic on 4/24/2016.
 */

//@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class User implements Serializable {



    private String email;
    private String id;
/*
    TODO create list to hold events the user belongs to
    TODO create fucntion to delete event once events is done or
        user decide to leave the evetn
    TODO create fucntion to add event once joined
*/
    public User(String id,String email) {
        this.id = id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

}