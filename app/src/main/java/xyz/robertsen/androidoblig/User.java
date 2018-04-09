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

    User(String firstName, String lastName) {
        this.fname = firstName;
        this.lname = lastName;
    }

    String getName() {
        return fname.concat(" ").concat(lname);
    }

    private static User authenticatedUser = null;
    static void setAuthenticatedUser(User user) {
        if (authenticatedUser == null)
            authenticatedUser = user;
        else
            throw new IllegalStateException("A user is already authenticated");
    }
    static boolean isAuthenticated() {
        return authenticatedUser != null;
    }
    static void authenticateUser(String usr, String pwd,
                                 final Context c,
                                 final ValidationListener v)
            throws IllegalStateException {
        if (isAuthenticated())
            throw new IllegalStateException("A user is already authenticated");

        RequestQueue volley = Volley.newRequestQueue(c);
        volley.add( new JsonObjectRequest(
            Request.Method.GET,
            compileLoginURL(usr, pwd),
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
    static void pinnedCardsUpdate(final CRUDListener listener) {

    }
    static void pinnedCardsCRUD(final CRUDListener listener,
                                @NonNull Context c,
                                @NonNull String request)
            throws IllegalStateException {

        if (!isAuthenticated())
            throw new IllegalStateException("User not authenticated");

        RequestQueue volley = Volley.newRequestQueue(c);
        volley.add( new StringRequest(
                Request.Method.POST,
                request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.handlePinnedCardsCRUD(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.handlePinnedCardsError(error);
                    }
                }
        ));
    }

    private static String compileLoginURL(String usr, String pwd) {
        return "https://robertsen.xyz/mtg/login.php?usr="
                .concat(usr)
                .concat("&pwd=")
                .concat(pwd);
    }
    private static String compileCRUD_URL(String sql) {
        return "https://robertsen.xyz/mtg/mtgdb.php?sql="
                .concat(sql);
    }
    interface ValidationListener {
        void handleValidationResponse(JSONObject response);
    }
    interface CRUDListener {
        void handlePinnedCardsCRUD(String response);
        void handlePinnedCardsError(VolleyError error);
    }
    enum DB {
        SELECT,
        CREATE,
        UPDATE,
        DELETE
    }
}
