package xyz.robertsen.androidoblig;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LibAPI {
    /**
     * Sends a request to the User API, requesting a predefined action or response.
     *
     * @param listener    The object making the request call.
     * @param c           The context of the requesting object.
     * @param card        The model for the data being sent.
     * @param requestType The type of request being sent.
     * @throws IllegalStateException Thrown when a user has not yet been authenticated.
     */
    static void request(final RequestListener listener,
                        @NonNull Context c,
                        @NonNull Card card,
                        REQUEST requestType)
            throws IllegalStateException {

        if (!User.isAuthenticated())
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
                        listener.handlePinnedCardsResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.handlePinnedCardsError(error);
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
            json.put("usr", User.authenticatedUser.getUsr());
            switch (requestType) {
                case CARD_GET:
                    json.put("sql", "select");
                    break;
                case CARD_PUSH:
//                    json.put("request", "sql");
                    json.put("sql", "push");
                    json.put("manaCost", card.mana);
                    json.put("cmc", card.cmc);
                    json.put("type", card.type);
                    json.put("power", card.power);
                    json.put("toughness", card.toughness);
                    json.put("text", card.text);
                    json.put("imgUrl", card.imageUrl);
                    json.put("rulings", card.rules);
                    json.put("notes", card.notes);
                    break;
                case CARD_DELETE:
                    json.put("sql", "delete");
                    break;
            }
        } catch (JSONException e) { /* No need, promise. */ }
        return json;
    }

    interface RequestListener {
        void handlePinnedCardsResponse(JSONObject response);
        void handlePinnedCardsError(VolleyError error);
    }

    enum REQUEST {
        IMG_GET,
        CARD_GET,
        CARD_PUSH,
        CARD_UPDATE,
        CARD_DELETE,
    }
}
