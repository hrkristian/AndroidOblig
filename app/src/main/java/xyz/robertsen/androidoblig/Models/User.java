package xyz.robertsen.androidoblig.Models;

/**
 * Created by kris on 25/02/18.
 */

public class User {


    private final String fname, lname;
    private final String usr;

    public User(String usr, String firstName, String lastName) {
        this.fname = firstName;
        this.lname = lastName;
        this.usr = usr;
    }

    public String getName() {
        return fname.concat(" ").concat(lname);
    }
    public String getUsr() {
        return usr;
    }
}
