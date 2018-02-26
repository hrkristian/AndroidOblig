package xyz.robertsen.androidoblig;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

public class CardActivity extends AppCompatActivity {

    RecyclerView recyclerCardHits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        recyclerCardHits = findViewById(R.id.card_recycler_cardHits);

    }
}
