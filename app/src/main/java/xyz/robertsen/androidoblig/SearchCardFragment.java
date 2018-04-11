package xyz.robertsen.androidoblig;

import android.app.DialogFragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kris on 29/03/18.
 */

public class SearchCardFragment extends DialogFragment {
    private static String ARG_CARD = "fragment_card";

    private Card card;
    private ImageView image;
    private TextView title, type, mana, pt, text, rulings;

    public SearchCardFragment() {

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
                width = (int)(getResources().getDisplayMetrics().widthPixels * 0.8);
                height = (int)(getResources().getDisplayMetrics().heightPixels * 0.6);
            } else {
                width = (int)(getResources().getDisplayMetrics().widthPixels * 0.6);
                height = (int)(getResources().getDisplayMetrics().heightPixels * 0.8);
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
        image.setImageDrawable(getResources().getDrawable(R.drawable.icon_2)); // TODO
        title.setText(card.name);
        text.setText(card.text);
        rulings.setText(card.rules);
    }

    private void findElements(View v) {
        image = v.findViewById(R.id.imageView_search_frag_image);
        title = v.findViewById(R.id.text_search_frag_title);
        type = v.findViewById(R.id.text_search_frag_type);
        mana = v.findViewById(R.id.text_search_frag_mana);
        pt = v.findViewById(R.id.text_search_frag_pt);
        text = v.findViewById(R.id.text_search_frag_text);
        rulings = v.findViewById(R.id.text_search_frag_rulings);
    }

    static SearchCardFragment newInstance(Card card) {
        SearchCardFragment fragment = new SearchCardFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_CARD, card);
        fragment.setArguments(args);

        return fragment;
    }

}
