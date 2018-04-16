package xyz.robertsen.androidoblig;

import android.app.DialogFragment;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by kris on 29/03/18.
 */

public class CardDialogFragment extends DialogFragment implements LibAPI.RequestListener {
    private static final String TAG = CardDialogFragment.class.getSimpleName();
    private static String ARG_CARD = "fragment_card";
    private Drawable cardDrawable = null;
    private Card card;
    private ImageView image;
    private TextView title, type, mana, pt, text, rulings;
    private ImageButton pinCardButton;
    private CardDialogFragment fragment = this;

    public CardDialogFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            card = (Card)getArguments().getSerializable(ARG_CARD);
    }

    @Override
    public void onResume() {
        // We want to constrain the size of the dialog away from the edges.
        if (getDialog().getWindow() != null) {
            int width, height;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                width = (int)(getResources().getDisplayMetrics().widthPixels * 0.9);
                height = (int)(getResources().getDisplayMetrics().heightPixels * 0.9);
            } else {
                width = (int)(getResources().getDisplayMetrics().widthPixels * 0.9);
                height = (int)(getResources().getDisplayMetrics().heightPixels * 0.9);
            }
            getDialog().getWindow().setLayout(width, height);
        }
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_search_card, container, false);

        findElements(v);
        populateElements();

        return v;
    }

    private void populateElements() {
        title.setText(card.name);
        mana.setText(card.mana);
        type.setText(card.type);
        pt.setText(card.power.concat("/").concat(card.toughness));
        text.setText(card.text);
        rulings.setText(card.rules);

        if (cardDrawable != null)
            image.setImageDrawable(cardDrawable);

        if (User.authenticatedUser == null) {
            pinCardButton.setVisibility(View.INVISIBLE);
        }

    }

    private void findElements(View v) {
        image = v.findViewById(R.id.imageView_search_frag_image);
        title = v.findViewById(R.id.text_search_frag_title);
        type = v.findViewById(R.id.text_search_frag_type);
        mana = v.findViewById(R.id.text_search_frag_mana);
        pt = v.findViewById(R.id.text_search_frag_pt);
        text = v.findViewById(R.id.text_search_frag_text);
        rulings = v.findViewById(R.id.text_search_frag_rulings);
        pinCardButton = v.findViewById(R.id.btn_pin_card);
        pinCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LibAPI.request(fragment, fragment.getActivity(), card, LibAPI.REQUEST.CARD_CREATE);
            }
        });
    }

    static CardDialogFragment newInstance(Card card) {
        CardDialogFragment fragment = new CardDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_CARD, card);
        fragment.setArguments(args);

        return fragment;
    }

    static CardDialogFragment newInstance(Card card, Drawable cardImage) {
        CardDialogFragment fragment = CardDialogFragment.newInstance(card);
        fragment.cardDrawable = cardImage;
        return fragment;
    }

    @Override
    public void handlePinnedCardsResponse(JSONObject response) {
        Log.d(TAG, "handlePinnedCardsResponse: " +  response.toString());
    }

    @Override
    public void handlePinnedCardsError(VolleyError error) {
        error.printStackTrace();
    }
}
