package xyz.robertsen.androidoblig;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import xyz.robertsen.androidoblig.Models.Card;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PinnedCardsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PinnedCardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PinnedCardsFragment extends Fragment implements LibAPI.CardRequestListener {

    private static final String TAG = PinnedCardsFragment.class.getSimpleName();
    ArrayList<Card> pinnedCards;
    private RecyclerView recyclerPinned;
    private PinnedCardAdapter cardAdapter;
    private ItemTouchHelper itemTouchHelper;
    private MtgApiRequestHandler requestHandler;
    int dragDirections = 0;
    int swipeDirections = ItemTouchHelper.END;
    private OnFragmentInteractionListener mListener;

    public PinnedCardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment
     *
     * @return A new instance of fragment PinnedCardsFragment.
     */
    public static PinnedCardsFragment newInstance() {
        return new PinnedCardsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemTouchHelper = getItemTouchHelper();
        requestHandler = new MtgApiRequestHandler(this.getContext());
        LibAPI.request(this, this.getContext(), new Card(), LibAPI.REQUEST.CARD_GET);

        // Fragment is retained across Activity re-creation
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pinned_cards, container, false);
        recyclerPinned = view.findViewById(R.id.recycler_pinned_cards);
        recyclerPinned.setAdapter(cardAdapter);
        recyclerPinned.setLayoutManager(new LinearLayoutManager(this.getContext()));
        itemTouchHelper.attachToRecyclerView(recyclerPinned);

        // Ensures that the CardViews in the recycler view is centered when the layout is horizontal
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRecyclerHorizontalOffsets();
        }

        // Request PinnedCardsFragment from server. Response handled in handlePinnedCardsResponse
        LibAPI.request(
                this,
                getContext(),
                new Card(),
                LibAPI.REQUEST.CARD_GET
        );
        return view;
    }

    /**
     * Sets horizontal offsets for the RecyclerView displaying the pinned cards
     */
    private void setRecyclerHorizontalOffsets() {
        recyclerPinned.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int totalWidth = parent.getWidth();
                int cardWidth = getResources().getDimensionPixelOffset(R.dimen.land_card_width);
                int sidePad = (totalWidth - cardWidth) / 2;
                sidePad = Math.max(0, sidePad);
                outRect.set(sidePad, 0, sidePad, 0);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void processPinnedCards(JSONObject response) throws JSONException {
        pinnedCards = new ArrayList<>();
        Map<Integer, Drawable> cardImages = new HashMap<>();
        JSONArray data;
        data = response.getJSONArray("cards");
        for (int i = 0; i < data.length(); i++) {
            pinnedCards.add(Card.newCard(getContext(), data.getJSONObject(i)));
        }
        cardAdapter = new PinnedCardAdapter(this.getContext(), pinnedCards, cardImages);
        recyclerPinned.setAdapter(cardAdapter);
        recyclerPinned.setLayoutManager(new LinearLayoutManager(this.getContext()));
        // Fetches images and binds them to viewholder async
        requestHandler.getImagesFromUrl(recyclerPinned, pinnedCards, cardImages);
    }

    /////////////////////////////
    /* * * * TouchHelper * * * */
    public ItemTouchHelper getItemTouchHelper() {
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(dragDirections, swipeDirections) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // Does nothing
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Right Swipe deletes the card
                int pos = viewHolder.getAdapterPosition();
                LibAPI.request(PinnedCardsFragment.this, getContext(), pinnedCards.remove(pos), LibAPI.REQUEST.CARD_DELETE);
                cardAdapter.notifyItemRemoved(pos);
            }
        });
        return itemTouchHelper;
    }

    ///////////////////////////
    /* * * * Callbacks * * * */
    @Override
    public void onCardResponse(JSONObject response) {
        try {
            if (response.has("cards")) {
                Log.d(TAG, "onCardResponse: got cards");
                processPinnedCards(response);
            }
            Log.d(TAG, "onCardResponse: " + response.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCardError(VolleyError error) {
        error.printStackTrace();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
