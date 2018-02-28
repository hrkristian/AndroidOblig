package xyz.robertsen.androidoblig;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CardActivity extends AppCompatActivity {
    /**
     * Class variables
     */
    SearchView cardHitSearchView;
    RecyclerView recyclerCardHits;
    CardHitAdapter cardHitAdapter;
    ArrayList<Card> cardArrayList;


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
        setCardHitSearchFocus();
        cardArrayList = new ArrayList<>(Arrays.asList(Card.getExampleData(this)));
        cardHitAdapter = new CardHitAdapter(this, cardArrayList);
        recyclerCardHits = findViewById(R.id.card_recycler_cardHits);
        recyclerCardHits.setAdapter(cardHitAdapter);
        recyclerCardHits.setLayoutManager(new LinearLayoutManager(this));
        /**
         * Checks device orentation, if "landscape" -> Runs SetHorizontalOffsets-method
         */
            setRecyclerHorizontalOffsets();

        handleIntent(getIntent());

    }

    /**
     * Method for setting focus in cardHitSearchView -> Search active
     */
    private void setCardHitSearchFocus() {
        cardHitSearchView.setIconifiedByDefault(true);
        cardHitSearchView.setFocusable(true);
        cardHitSearchView.setIconified(false);
        cardHitSearchView.clearFocus();
        cardHitSearchView.requestFocusFromTouch();
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


}
