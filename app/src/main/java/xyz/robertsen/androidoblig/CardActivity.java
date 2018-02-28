package xyz.robertsen.androidoblig;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CardActivity extends AppCompatActivity {

    SearchView cardHitSearchView;
    RecyclerView recyclerCardHits;
    CardHitAdapter cardHitAdapter;
    ArrayList<Card> cardArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        cardHitSearchView = (SearchView) findViewById(R.id.cardHitSearchView); // inititate a search view
        setCardHitSearchFocus();
        cardArrayList = new ArrayList<>(Arrays.asList(Card.getExampleData(this)));
        cardHitAdapter = new CardHitAdapter(this, cardArrayList);
        recyclerCardHits = findViewById(R.id.card_recycler_cardHits);
        recyclerCardHits.setAdapter(cardHitAdapter);
        recyclerCardHits.setLayoutManager(new LinearLayoutManager(this));


        handleIntent(getIntent());

    }
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

}
