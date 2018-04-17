package xyz.robertsen.androidoblig;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import xyz.robertsen.androidoblig.Models.Card;

/**
 * Created by gitsieg on 15.04.18.
 */

/**
 * This class handles requesting Card-data and images from external api's
 */
public class MtgApiRequestHandler {
    private final static String TAG = MtgApiRequestHandler.class.getSimpleName();
    final Context context;
    final String BASE_URL;
    final RequestQueue REQUEST_QUEUE;

    public MtgApiRequestHandler(Context context) {
        this.context = context;
        REQUEST_QUEUE = Volley.newRequestQueue(context);
        BASE_URL = "https://api.magicthegathering.io/v1/cards?name=";
    }

    /**
     * Requests cards from Mtg-api. Takes a cardname and a MtgApiResponseListener as an argument.
     * Objects using this method must supply a listener
     * @param request requested card
     * @param listener object that handles the response
     */
    public void sendRequest(String request, final MtgApiResponseListener listener) {
        Log.d(TAG, "sendRequest");
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                BASE_URL.concat(request),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse + ");
                        listener.handleMtgApiResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO
                        listener.handleMtgApiError(error);
                    }
                }
        );
        REQUEST_QUEUE.add(stringRequest);
    }

    /**
     * Interface for handling responses from the MtgApi
     */
    public interface MtgApiResponseListener {
        void handleMtgApiResponse(String response);
        void handleMtgApiError(VolleyError error);
    }

    /**
     * Interface for updating RecyclerView.ViewHolder objects asynchronously
     */
    interface OnImageReceivedListener {
        void updateViewHolder(Drawable drawable);
    }

    /**
     * Takes a RecyclerView, list of Card-objects and a Map<Integer, Drawable>
     * @param recyclerView
     * @param cards
     * @param cardImages
     */
    public synchronized void getImagesFromUrl(RecyclerView recyclerView, List<Card> cards, Map<Integer, Drawable> cardImages) {
        ImageFromUrlFetcher imgFetcher = new ImageFromUrlFetcher(context, recyclerView, cards, cardImages);
        imgFetcher.getImagesFromUrl();
    }

    private class ImageFromUrlFetcher {
        Context c;
        RecyclerView recyclerView;
        List<Card> cards;
        Map<Integer, Drawable> cardImages;

        ImageFromUrlFetcher(Context c, RecyclerView recyclerView, List<Card> cards, Map<Integer, Drawable> cardImages) {
            this.c = c;
            this.recyclerView = recyclerView;
            this.cards = cards;
            this.cardImages = cardImages;
        }

        void getImagesFromUrl() {
            new Thread() {
                public void run() {
                    for (int i = 0; i < cards.size(); i++) {
                        final int pos = i;
                        try {
                            URL url = new URL(cards.get(i).imageUrl);
                            InputStream stream = (InputStream) url.getContent();
                            Drawable img = Drawable.createFromStream(stream, null);

                            cardImages.put(pos, img);
                        } catch (IOException e) {
                            // Catching IOException handles both URL, InputStream,
                            // and createFromStream exceptions
                            e.printStackTrace();
                            System.out.println("Problem URL: ".concat(cards.get(i).imageUrl));
                            cardImages.put(pos, c.getResources().getDrawable(R.drawable.icon_2));
                        } finally {
                            ((Activity) c).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Because the currently active Viewholder(s) might need to be updated
                                    RecyclerView.ViewHolder v = recyclerView.findViewHolderForAdapterPosition(pos);
                                    if (v != null) {
                                        ((OnImageReceivedListener) v).updateViewHolder(cardImages.get(pos));
//                                    ((OnImageReceivedListener)).setImageDrawable(cardImages.get(pos));
                                    }
                                }
                            });
                        }
                    }
                }
            }.start();
        }
    }
}



