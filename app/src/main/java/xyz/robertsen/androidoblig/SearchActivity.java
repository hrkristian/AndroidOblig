package xyz.robertsen.androidoblig;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {
    /**
     * Class variables
     */
    SearchView cardHitSearchView;
    RecyclerView recyclerCardHits;
    SearchAdapter searchAdapter;
    ArrayList<Card> cardArrayList;
    RequestHandler requestHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        /**
         * Initalizing
         */
        cardHitSearchView = (SearchView) findViewById(R.id.cardHitSearchView);
        /**
         * Calls the method setCardHitSearchFocus
         */
        cardArrayList = new ArrayList<>(Arrays.asList(Card.getExampleData(this)));
        searchAdapter = new SearchAdapter(this, cardArrayList);
        recyclerCardHits = findViewById(R.id.card_recycler_cardHits);
        recyclerCardHits.setAdapter(searchAdapter);
        recyclerCardHits.setLayoutManager(new LinearLayoutManager(this));
        /**
         * Checks device orentation, if "landscape" -> Runs SetHorizontalOffsets-method
         */
            setRecyclerHorizontalOffsets();

        handleIntent(getIntent());

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("SearchQuery", query);
        }
    }

    /**
     * Sets Horizintal Offsets in RecycleView
     */
    private void setRecyclerHorizontalOffsets() {
        recyclerCardHits.addItemDecoration(new RecyclerView.ItemDecoration() {
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


    private class RequestHandler {
        final String BASE_URL;
        final RequestQueue REQUEST_QUEUE;

        private RequestHandler() {
            REQUEST_QUEUE = Volley.newRequestQueue(getApplicationContext());
            BASE_URL = "https://api.magicthegathering.io/v1/cards?name=";
        }

        void sendRequest(String request) {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET,
                    BASE_URL.concat(request),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
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

    private void generateCardView(String response) {
    }
}
