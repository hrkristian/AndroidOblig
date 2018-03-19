package xyz.robertsen.androidoblig;

/**
 * Created by kris on 25/02/18.
 */

public class User {

    long db_id;
    String usr, pwd;

    public User(String usr, String pwd) {
        this.usr = usr;
        this.pwd = pwd;
    }

    public boolean isMatch(String usr, String pwd) {
        return (usr.equalsIgnoreCase(this.usr) && pwd.equals(this.pwd));
    }

    public String getUsr() {
        return usr;
    }

    public String getPwd() {
        return pwd;
    }

    public void setId(long id) {
        db_id = id;
    }
}
