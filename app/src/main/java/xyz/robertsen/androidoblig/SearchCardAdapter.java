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

    // Tag for logging
    private static String TAG = SearchCardAdapter.class.getSimpleName();

    private final boolean isRecentSearches;
    private ArrayList<Card> cardArrayList;
    private LayoutInflater inflater;
    private Context context;

    /**
     * Instantiates the SearchCardAdapter
     * @param context - The activity/context this adapter is created within
     * @param cardList - ArrayList of Cards to use with this adapter
     * @param isRecentSearches - If this adapter is contained within RecentCardsFragment of PinnedCardsFragment
     */
    public SearchCardAdapter(Context context, ArrayList<Card> cardList, boolean isRecentSearches) {
        Log.d(TAG, "Instantiate SearchCardAdapter");
        cardArrayList = cardList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.isRecentSearches = isRecentSearches;
    }

    /**
     * Inflates the card_item layout and creates a CardViewHolder
     * @param parent - The parent container, the RecyclerView which has this adapter I guess.
     * @param viewType -
     * @return CardViewHolder
     */
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.search_recycler_carditem, parent, false);
        return new CardViewHolder(itemView, this);
    }

    /**
     * Sets/binds data to the ViewHolder
     * @param holder - The CardViewHolder
     * @param position - This adapters position
     */
    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Card card = cardArrayList.get(position);
        holder.title.setText(card.title);
        holder.cmc.setText(String.valueOf(card.convertedManaCost));
        holder.cardCropImage.setImageDrawable(card.cropImage);
        holder.text.setText(card.text);

        // Not ideal, sets visibility for the pin icon.
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

        private boolean pinned;
        private final TextView title, cmc, text;
        private final ImageView cardCropImage;
        private final ImageButton searchPin;
        final SearchCardAdapter cardAdapter;

        public CardViewHolder(View itemView, SearchCardAdapter adapter) {
            super(itemView);
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
         * If search_pin view is clicked, save card to PinnedCardsFragment.
         * If The holder is clicked, open CardActivity and search for the given card title.
         * @param view - The view that is clicked (either pin button or ViewHolder.
         */
        @Override
        public void onClick(final View view) {
            int viewID = view.getId();
            int adapterPos = getAdapterPosition();
            Card thisCard = cardArrayList.get(adapterPos);
            if (viewID == R.id.search_pin_btn) {
                pinned = true;
                view.animate().alpha(0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });
                ((SearchActivity)context).onCardsPinned(thisCard.title);
            } else {
                Intent intent = new Intent(context, CardActivity.class);
                intent.putExtra("searched_item", thisCard.title);
                context.startActivity(intent);
            }
        }
    }
}
