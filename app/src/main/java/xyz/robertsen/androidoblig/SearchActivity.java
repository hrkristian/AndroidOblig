package xyz.robertsen.androidoblig;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;


import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.robertsen.androidoblig.Models.Card;
import xyz.robertsen.androidoblig.Models.User;

public class SearchActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener,
        MtgApiRequestHandler.MtgApiResponseListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    /**
     * Class variables
     */
    SearchView cardHitSearchView;
    RecyclerView recyclerSearchHits;
    SearchAdapter searchAdapter;
    MtgApiRequestHandler requestHandler;

    // TODO HANDLE CONFIGURATION CHANGES, FLIP PHONE ETC
    // TODO, DO NOT ADD TO BACKSTACK
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        requestHandler = new MtgApiRequestHandler(this);
        /**
         * Initalizing
         */
        cardHitSearchView = findViewById(R.id.cardHitSearchView);
        cardHitSearchView.setOnQueryTextListener(this);
        recyclerSearchHits = findViewById(R.id.card_recycler_cardHits);

        /**
         * Checks device orentation, if "landscape" -> Runs SetHorizontalOffsets-method
         **/
        setRecyclerHorizontalOffsets();
        handleIntent(getIntent());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

        if (AppState.isAuthenticated())
            RecentSearchItem.addRecentSearchItem(this, searchString);

        requestHandler.sendRequest(
                searchString, this
        );

        Log.d(TAG, "handleIntent");
    }

    private void generateCardView(String JSONString) {
        List<Card> cards = new ArrayList<>();
        Map<Integer, Drawable> cardImages = new HashMap<>();
        try {
            JSONObject tmp = new JSONObject(JSONString), item;
            JSONArray json;

            if (tmp.has("card")) {
                json = tmp.getJSONArray("card");
            } else {
                json = tmp.getJSONArray("cards");
            }

            for (int i = 0; i < json.length(); i++) {
                item = json.getJSONObject(i);
                cards.add(Card.newCard(this, item));
            }

            searchAdapter = new SearchAdapter(this, cards, cardImages);
            recyclerSearchHits.setAdapter(searchAdapter);
            recyclerSearchHits.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            // Load images as they arrive
            requestHandler.getImagesFromUrl(recyclerSearchHits, cards, cardImages);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.d(TAG, "onQueryTextSubmit(String s)");
        // TODO Remove focus from SearchView
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        intent.setAction(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, s);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void onMtgApiResponse(String response) {
        generateCardView(response);
    }

    @Override
    public void onMtgApiError(VolleyError error) {
        error.printStackTrace();
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
