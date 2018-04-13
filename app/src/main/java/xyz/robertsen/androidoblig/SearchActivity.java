package xyz.robertsen.androidoblig;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    /**
     * Class variables
     */
    SearchView cardHitSearchView;
    RecyclerView recyclerSearchHits;
    SearchAdapter searchAdapter;
    RequestHandler requestHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        requestHandler = new RequestHandler();
        /**
         * Initalizing
         */
        cardHitSearchView = findViewById(R.id.cardHitSearchView);

        // TODO!!!
        //cardArrayList = new ArrayList<>(Arrays.asList(Card.getExampleData(this)));

        recyclerSearchHits = findViewById(R.id.card_recycler_cardHits);


        /**
         * Checks device orentation, if "landscape" -> Runs SetHorizontalOffsets-method
         **/
        /*
        setRecyclerHorizontalOffsets();
        */
        handleIntent(getIntent());

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        String searchString;
        if (!Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchString = intent.getStringExtra("search_string");
        } else {
            searchString = intent.getStringExtra(SearchManager.QUERY).replace(' ', '+');
        }

        Log.d(TAG, searchString);
        requestHandler.sendRequest(
                searchString

        );
        Log.d(TAG, "handleIntent");
    }

    private void generateCardView(String JSONString) {
        Log.d(TAG, "generateCardView");
        List<Card> cards = new ArrayList<>();
        Map<Integer, Drawable> cardImages = new HashMap<>();

        try {
            JSONObject tmp = new JSONObject(JSONString), item;
            JSONArray json;

            if (tmp.has("card")) {
                json = tmp.getJSONArray("card");
            } else {
                json = tmp.getJSONArray("cards");
//                System.out.println(json.toString(2));
            }

            for (int i = 0; i < json.length(); i++) {
                item = json.getJSONObject(i);
                cards.add(Card.newCard(this, item));
            }

            searchAdapter = new SearchAdapter(this, cards, cardImages);

            recyclerSearchHits.setAdapter(searchAdapter);
            recyclerSearchHits.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            // Load images as they arrive
            getImageFromURL(cards, cardImages);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * RequestHandler, for sending and receiving requests
     */
    private class RequestHandler {
        final String BASE_URL;
        final RequestQueue REQUEST_QUEUE;

        private RequestHandler() {
            REQUEST_QUEUE = Volley.newRequestQueue(getApplicationContext());
            BASE_URL = "https://api.magicthegathering.io/v1/cards?name=";
        }

        void sendRequest(String request) {
            Log.d(TAG, "sendRequest");
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET,
                    BASE_URL.concat(request),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse + ");
                            generateCardView(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error.getMessage());
                            // TODO
                        }
                    }
            );
            REQUEST_QUEUE.add(stringRequest);
        }
    }

    /**
     * Fetches and generates the linked image asychronously on a new thread,
     * in order to not lock the main thread
     *
     * @param cards
     * @param cardImages
     */
    private void getImageFromURL(final List<Card> cards, final Map<Integer, Drawable> cardImages) {
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
                        cardImages.put(pos, getResources().getDrawable(R.drawable.icon_2));
                    } finally {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Because the currently active Viewholder(s) might need to be updated
                                SearchAdapter.SearchHitHolder v =
                                        (SearchAdapter.SearchHitHolder)
                                                recyclerSearchHits.findViewHolderForAdapterPosition(pos);
                                if (v != null) {
                                    v.image.setImageDrawable(cardImages.get(pos));
                                }
                            }
                        });
                    }
                }
            }
        }.start();

    }

    /**
     * Sets Horizintal Offsets in RecycleView
     */
    private void setRecyclerHorizontalOffsets() {
        recyclerSearchHits.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int totalWidth = parent.getWidth();
                int cardWidth = getResources().getDimensionPixelOffset(R.dimen.land_card_width);
                int sidePad = (totalWidth - cardWidth) / 2;
                sidePad = Math.max(0, sidePad);
                outRect.set(sidePad, 0, sidePad, 0);
            }
        });

    }
}
