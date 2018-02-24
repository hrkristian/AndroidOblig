package xyz.robertsen.androidoblig;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gitsieg on 23.02.18.
 */

class SearchCardAdapter extends RecyclerView.Adapter<SearchCardAdapter.CardViewHolder> {

    private static String TAG = SearchCardAdapter.class.getSimpleName();

    private ArrayList<Card> cardArrayList;
    private LayoutInflater inflater;
    private Context context;

    public SearchCardAdapter(Context context, ArrayList<Card> cardList) {
        Log.d(TAG, "Instantiate SearchCardAdapter");
        cardArrayList = cardList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View itemView = inflater.inflate(R.layout.search_recycler_carditem, parent, false);
        return new CardViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        Card card = cardArrayList.get(position);
        holder.title.setText(card.title);
        holder.cmc.setText(card.convertedManaCost+"");
        holder.cardImage.setImageDrawable(card.image);

    }

    @Override
    public int getItemCount() {
        return cardArrayList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final String TAG = CardViewHolder.class.getSimpleName();

        private final TextView title, cmc;
        private final ImageView cardImage;
        final SearchCardAdapter cardAdapter;

        public CardViewHolder(View itemView, SearchCardAdapter adapter) {
            super(itemView);
            Log.d(TAG, "Instantiate SearchCardAdapter");
            title = itemView.findViewById(R.id.search_carditem_title);
            cmc = itemView.findViewById(R.id.search_carditem_cmc);
            cardImage = itemView.findViewById(R.id.search_carditem_image);
            cardAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        /**
         * Start CardActivity, new search for the card that is clicked.
         * @param view
         */
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, CardActivity.class);
            intent.putExtra("searched_item", cardArrayList.get(getAdapterPosition()).title);
            context.startActivity(intent);
        }
    }
}
