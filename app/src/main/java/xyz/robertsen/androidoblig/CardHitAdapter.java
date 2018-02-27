package xyz.robertsen.androidoblig;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by gitsieg on 26.02.18.
 */

public class CardHitAdapter extends RecyclerView.Adapter<CardHitAdapter.CardHitHolder> {

    Context context;
    ArrayList<Card> cardArrayList;
    LayoutInflater inflater;

    public CardHitAdapter(Context context, ArrayList<Card> cardArrayList) {
        this.context = context;
        this.cardArrayList = cardArrayList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public CardHitAdapter.CardHitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_cardhit_item, parent, false);
        return new CardHitHolder(view, this);
    }

    @Override
    public void onBindViewHolder(CardHitAdapter.CardHitHolder holder, int position) {
        Card card = cardArrayList.get(position);
        holder.image.setImageDrawable(card.image);
        holder.title.setText(card.title);
        holder.text.setText(card.text);
    }

    @Override
    public int getItemCount() {
        return cardArrayList.size();
    }

    public class CardHitHolder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView title, text;
        private CardHitAdapter adapter;

        public CardHitHolder(View itemView, CardHitAdapter adapter) {
            super(itemView);

            image = itemView.findViewById(R.id.card_cardhits_image);
            title = itemView.findViewById(R.id.card_cardhits_title);
            text = itemView.findViewById(R.id.card_cardhits_text);
            this.adapter = adapter;
        }
    }
}