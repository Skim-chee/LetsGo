package umd.letsgo;

import java.io.Serializable;

/**
 * Created by jeffsadic on 4/24/2016.
 */

//@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class User implements Serializable {

    private int id;
    private String name;
    private String email;
/*
    TODO create list to hold events the user belongs to
    TODO create fucntion to delete event once events is done or
        user decide to leave the evetn
    TODO create fucntion to add event once joined
*/
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}