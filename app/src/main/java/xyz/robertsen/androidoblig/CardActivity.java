package xyz.robertsen.androidoblig;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CardActivity extends AppCompatActivity {

    RecyclerView recyclerCardHits;
    CardHitAdapter cardHitAdapter;
    ArrayList<Card> cardArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        recyclerCardHits = findViewById(R.id.card_recycler_cardHits);
        cardArrayList = new ArrayList<>(Arrays.asList(Card.getExampleData(this)));
        cardHitAdapter = new CardHitAdapter(this, cardArrayList);
        recyclerCardHits.setAdapter(cardHitAdapter);
        recyclerCardHits.setLayoutManager(new LinearLayoutManager(this));


    }
}
