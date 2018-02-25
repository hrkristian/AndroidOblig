package xyz.robertsen.androidoblig;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gitsieg on 23.02.18.
 */

class SearchCardAdapter extends RecyclerView.Adapter<SearchCardAdapter.CardViewHolder> {

    private static String TAG = SearchCardAdapter.class.getSimpleName();
    private final boolean isRecentSearches;

    private ArrayList<Card> cardArrayList;
    private LayoutInflater inflater;
    private Context context;

    /**
     * @param context
     * @param cardList
     * @param isRecentSearches - If fragment containing this adapter is RecentCardsFragment.(shitty)
     */
    public SearchCardAdapter(Context context, ArrayList<Card> cardList, boolean isRecentSearches) {
        Log.d(TAG, "Instantiate SearchCardAdapter");
        cardArrayList = cardList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.isRecentSearches = isRecentSearches;
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
        holder.cmc.setText(String.valueOf(card.convertedManaCost));
        holder.cardCropImage.setImageDrawable(card.cropImage);
        holder.text.setText(card.text);
        // Not ideal
        if (!isRecentSearches) {
            holder.searchPin.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return cardArrayList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final String TAG = CardViewHolder.class.getSimpleName();

        private final TextView title, cmc, text;
        private final ImageView cardCropImage;
        private final ImageButton searchPin;
        final SearchCardAdapter cardAdapter;

        public CardViewHolder(View itemView, SearchCardAdapter adapter) {
            super(itemView);
            Log.d(TAG, "Instantiate CardViewHolder");
            title = itemView.findViewById(R.id.search_carditem_title);
            cmc = itemView.findViewById(R.id.search_carditem_cmc);
            cardCropImage = itemView.findViewById(R.id.search_carditem_cropImage);
            text = itemView.findViewById(R.id.search_carditem_text);
            searchPin = itemView.findViewById(R.id.search_pin_btn);
            searchPin.setOnClickListener(this);
            cardAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        /**
         * Start CardActivity and adds the cardname title to the intent as a stringextra
         *
         * @param view
         */
        @Override
        public void onClick(final View view) {
            int viewID = view.getId();
            int adapterPos = getAdapterPosition();
            Card thisCard = cardArrayList.get(adapterPos);
            if (viewID == R.id.search_pin_btn) {
                Log.d(TAG, "Pin is clicked: " + thisCard.title + " pos " + adapterPos);
                view.animate().alpha(0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });
            } else {
                Intent intent = new Intent(context, CardActivity.class);
                intent.putExtra("searched_item", thisCard.title);
                context.startActivity(intent);
            }


        }
    }
}
