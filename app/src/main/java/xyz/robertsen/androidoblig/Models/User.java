package xyz.robertsen.androidoblig;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * Created by kris on 25/02/18.
 */

public class User {


    private final String fname, lname;
    private final String usr;
    public static User authenticatedUser = null;

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

    static void setAuthenticatedUser(User user) {
        if (authenticatedUser == null)
            authenticatedUser = user;
        else
            throw new IllegalStateException("A user is already authenticated");
    }
    static boolean isAuthenticated() {
        return authenticatedUser != null;
    }





}
