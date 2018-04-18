package xyz.robertsen.androidoblig;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import xyz.robertsen.androidoblig.Models.Card;

public class LibAPI {


/* * * * User API Logic * * * */

    static void sendUserAuthenticationRequest(@NonNull String usr, @NonNull String pwd,
                                              @NonNull final Context c,
                                              @NonNull final AuthenticationResponseListener v)
            throws IllegalStateException {
        if (AppState.isAuthenticated())
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

    interface AuthenticationResponseListener {
        void handleValidationResponse(JSONObject response);
    }
/* – – – User API Logic – – – */



/* * * * Card API Logic * * * */

    /**
     * Sends a request to the User API, requesting a predefined action or response.
     *
     * @param listener    The object making the request call.
     * @param c           The context of the requesting object.
     * @param card        The model for the data being sent.
     * @param requestType The type of request being sent.
     * @throws IllegalStateException Thrown when a user has not yet been authenticated.
     */
    static void request(final CardRequestListener listener,
                        @NonNull Context c,
                        @NonNull Card card,
                        REQUEST requestType)
            throws IllegalStateException {

        if (!AppState.isAuthenticated())
            throw new IllegalStateException("User not authenticated");

        String url = "https://robertsen.xyz/mtg/mtg.php";
        JSONObject json = requestBuilder(card, requestType);
        try {
            Log.i("JSON Request", json.toString(2));

        } catch (JSONException e) {
        }


        RequestQueue volley = Volley.newRequestQueue(c);
        JsonRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onCardResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onCardError(error);
                    }
                }
        );
        Log.d("request-tostring", request.toString());
        volley.add(request);
    }


    /**
     * Builds the request string sent to the User API.
     * @param card        The card model
     * @param requestType The request type made to the server
     * @return The completed request string
     */
    private static JSONObject requestBuilder(Card card, REQUEST requestType) {
        JSONObject json = new JSONObject();
        try {
            if (requestType == REQUEST.IMG_GET) {
                json.put("request", "backgrounds");
                return json;
            }
            json.put("name", card.name);
            json.put("request", "sql");
            json.put("usr", AppState.getAuthenticatedUser().getUsr());
            switch (requestType) {
                case CARD_GET:
                    json.put("sql", "select");
                    break;
                case CARD_CREATE:
//                    json.put("request", "sql");
                    json.put("sql", "create");
                    json.put("manaCost", card.mana);
                    json.put("cmc", card.cmc);
                    json.put("type", card.type);
                    json.put("power", card.power);
                    json.put("toughness", card.toughness);
                    json.put("text", card.text);
                    json.put("imgUrl", card.imageUrl);
                    json.put("rulings", card.rules);
                    break;
                case CARD_UPDATE:
                    json.put("sql", "update");
                    json.put("notes", card.notes);
                    break;
                case CARD_DELETE:
                    json.put("sql", "delete");
                    break;
            }
        } catch (JSONException e) { /* No need, promise. */ }
        return json;
    }

    interface CardRequestListener {
        void onCardResponse(JSONObject response);
        void onCardError(VolleyError error);
    }


    enum REQUEST {
        IMG_GET,
        CARD_GET,
        CARD_CREATE,
        CARD_UPDATE,
        CARD_DELETE
    }

/* – – – Card API Logic – – – */
}
