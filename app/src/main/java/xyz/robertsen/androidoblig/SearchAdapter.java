package xyz.robertsen.androidoblig;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;
import java.util.Map;

import xyz.robertsen.androidoblig.Models.Card;

/**
 * Created by gitsieg on 26.02.18.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = SearchAdapter.class.getSimpleName();

    /**
     * Class-variables
     */
    private Context context;
    private List<Card> cards;
    private Map<Integer, Drawable> cardImages;
    private LayoutInflater mInflater;

    /**
     * Constructor
     *
     * @param context
     * @param cards
     */
    public SearchAdapter(Context context, List<Card> cards, Map<Integer, Drawable> cardImages) {
        this.context = context;
        this.cards = cards;
        this.cardImages = cardImages;
        this.mInflater = LayoutInflater.from(context);
    }

    /**
     * Creating view, returning viewholder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public SearchAdapter.SearchHitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_cardhit_item, parent, false);
        return new SearchHitHolder(view, this);
    }

    /**
     * Binds the view
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SearchHitHolder) holder).image.setImageDrawable(cardImages.get(position));
        ((SearchHitHolder) holder).title.setText(cards.get(position).name);
        ((SearchHitHolder) holder).text.setText(cards.get(position).getSpanText());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    /**
     * SearchHitHolder-class
     * <p>
     * Class variables
     */
    public class SearchHitHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,
            MtgApiRequestHandler.OnImageReceivedListener {

        public final ImageView image;
        final TextView title, text;
        public SearchAdapter adapter;

        public SearchHitHolder(View itemView, SearchAdapter adapter) {
            super(itemView);

            image = itemView.findViewById(R.id.card_cardhits_image);
            title = itemView.findViewById(R.id.card_cardhits_title);
            text = itemView.findViewById(R.id.card_cardhits_text);
            this.adapter = adapter;
            itemView.setOnClickListener(this);

        }

        ///////////////////////
        /* * * CallBacks * * */
        @Override
        public void onClick(View view) {
            // TODO Spawn CardDialogFragment
            int adapterPos = getAdapterPosition();
            Card thisCard = cards.get(adapterPos);
            DialogFragment cardDialogFragment = CardDialogFragment.newInstance(thisCard, image.getDrawable());
            Log.d(TAG, "onClick: ");
            FragmentTransaction ft = ((AppCompatActivity)context).getFragmentManager().beginTransaction();
            cardDialogFragment.show(ft, "cardDialog");
        }
        @Override
        public void updateViewHolder(Drawable drawable) {
            image.setImageDrawable(drawable);
        }
    }


}
