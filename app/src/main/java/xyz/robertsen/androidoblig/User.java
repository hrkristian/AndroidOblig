package xyz.robertsen.androidoblig;

/**
 * Created by kris on 25/02/18.
 */

public class User {

    String usr, pwd;

    public User(String usr, String pwd) {
        this.usr = usr;
        this.pwd = pwd;
    }

    public boolean isMatch(String usr, String pwd) {
        return (usr.equalsIgnoreCase(this.usr) && pwd.equals(this.pwd));
    }
}
