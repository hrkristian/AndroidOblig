package xyz.robertsen.androidoblig;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This adapter is used in PinnedCardsFragment. It shows a list of pinned cards
 */

class PinnedCardAdapter extends RecyclerView.Adapter<PinnedCardAdapter.CardViewHolder> {

    // Tag for logging
    private static String TAG = PinnedCardAdapter.class.getSimpleName();

    private ArrayList<Card> cardArrayList;
    private LayoutInflater inflater;
    private Context context;

    /**
     * Instantiates the PinnedCardAdapter
     *
     * @param context  - The activity/context this adapter is created within
     * @param cardList - ArrayList of Cards to use with this adapter
     */
    public PinnedCardAdapter(Context context, ArrayList<Card> cardList) {
        cardArrayList = cardList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * Inflates the card_item layout and creates a CardViewHolder
     *
     * @param parent   - The parent container, the RecyclerView which has this adapter I guess.
     * @param viewType -
     * @return CardViewHolder
     */
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recycler_pinned_card_item, parent, false);
        return new CardViewHolder(itemView, this);
    }

    /**
     * Sets/binds data to the ViewHolder
     *
     * @param holder   - The CardViewHolder
     * @param position - This adapters position
     */
    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Card card = cardArrayList.get(position);
        holder.title.setText(card.name);
        holder.manaCost.setText(card.mana);
        holder.cmc.setText(String.valueOf(card.cmc));
        holder.cardCropImage.setImageDrawable(context.getResources().getDrawable(R.drawable.jace_mind_sculptor));
        holder.text.setText(card.text);
    }

    @Override
    public int getItemCount() {
        return cardArrayList == null ? 0: cardArrayList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final String TAG = CardViewHolder.class.getSimpleName();

        private final TextView title, cmc, text, manaCost;
        private final ImageView cardCropImage;
        final PinnedCardAdapter cardAdapter;

        public CardViewHolder(View itemView, PinnedCardAdapter adapter) {
            super(itemView);
            title = itemView.findViewById(R.id.pinned_card_title);
            manaCost = itemView.findViewById(R.id.pinned_card_manaCost);
            cmc = itemView.findViewById(R.id.pinned_card_cmc_value);
            cardCropImage = itemView.findViewById(R.id.pinned_card_image);
            text = itemView.findViewById(R.id.search_carditem_text);
            cardAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        /**
         * If search_pin view is clicked, save card to PinnedCardsFragment.
         * If The holder is clicked, open SearchActivity and search for the given card title.
         *
         * @param view - The view that is clicked (either pin button or ViewHolder.
         */
        @Override
        public void onClick(final View view) {
            int viewID = view.getId();
            int adapterPos = getAdapterPosition();
            Card thisCard = cardArrayList.get(adapterPos);
//            if (viewID == RecentSearchesTable.id.search_pin_btn) {
//                view.animate().alpha(0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        view.setVisibility(View.GONE);
//                    }
//                });
//                ((HistoryActivity)context).onCardsPinned(thisCard.name);
//            } else {
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra("search_string", thisCard.name);
            context.startActivity(intent);
        }
    }
}
