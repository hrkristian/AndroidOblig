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

class User {

    private final String fname, lname;
    private final String usr;
    public static User authenticatedUser = null;

    User(String usr, String firstName, String lastName) {
        this.fname = firstName;
        this.lname = lastName;
        this.usr = usr;
    }

    String getName() {
        return fname.concat(" ").concat(lname);
    }
    String getUsr() {
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

    static void authenticateUser(@NonNull String usr, @NonNull String pwd,
                                 @NonNull final Context c,
                                 @NonNull final ValidationListener v)
            throws IllegalStateException {
        if (isAuthenticated())
            throw new IllegalStateException("A user is already authenticated");

        RequestQueue volley = Volley.newRequestQueue(c);
        volley.add( new JsonObjectRequest(
            Request.Method.GET,
            loginUrlBuilder(usr, pwd),
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    v.handleValidationResponse(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(c, "Oh no, Volley-error :(", Toast.LENGTH_SHORT);
                }
            })
        );
    }

    @NonNull
    private static String loginUrlBuilder(String usr, String pwd) {
        return "https://robertsen.xyz/mtg/login.php?usr="
                .concat(usr)
                .concat("&pwd=")
                .concat(pwd);
    }
    interface ValidationListener {
        void handleValidationResponse(JSONObject response);
    }
}
