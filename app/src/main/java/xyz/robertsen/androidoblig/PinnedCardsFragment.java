package xyz.robertsen.androidoblig;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
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
import java.util.Collections;

import xyz.robertsen.androidoblig.database.CardDatabaseOpenHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PinnedCardsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PinnedCardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PinnedCardsFragment extends Fragment implements LibAPI.RequestListener {

    private static final String TAG = PinnedCardsFragment.class.getSimpleName();
    ArrayList<Card> pinnedCards;
    private RecyclerView recyclerPinned;
    private PinnedCardAdapter cardAdapter;
    private ItemTouchHelper itemTouchHelper;
    int dragDirections = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    int swipeDirections = ItemTouchHelper.START | ItemTouchHelper.END;
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
        cardAdapter = new PinnedCardAdapter(this.getContext(), pinnedCards);
        itemTouchHelper = getItemTouchHelper();

        // Fragment is retained across Activity re-creation
        setRetainInstance(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
//        LibAPI.request(
//                this,
//                getContext(),
//                new Card(),
//                LibAPI.REQUEST.CARD_GET
//        );
        return view;
    }

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

    public ItemTouchHelper getItemTouchHelper() {
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(dragDirections, swipeDirections) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(pinnedCards, from, to);
                cardAdapter.notifyItemMoved(from, to);

                // TODO: Query database, change card indexes
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                pinnedCards.remove(pos);
                cardAdapter.notifyItemRemoved(pos);
            }
        });
        return itemTouchHelper;
    }

    public void onNoAuthentication() {
    }

    @Override
    public void handlePinnedCardsResponse(JSONObject response) {
        pinnedCards = new ArrayList<>();
        try {
            Log.d(TAG, response.toString(2));
            JSONArray data = response.getJSONArray("cards");
            for (int i = 0; i < data.length(); i++) {
                pinnedCards.add(Card.newCard(getContext(), data.getJSONObject(i)));
                cardAdapter = new PinnedCardAdapter(this.getContext(), pinnedCards);
                recyclerPinned.setAdapter(cardAdapter);
                recyclerPinned.setLayoutManager(new LinearLayoutManager(this.getContext()));
            }
            Log.d(TAG, pinnedCards.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handlePinnedCardsError(VolleyError error) {

    }

    ////////////////////////////////////////////////
    //  START: Implements User.IsAuthenticatedTasks
    ////////////////////////////////////////////////
//    /**
//     * Interface defined in User.IsAuthenticatedTasks
//     */
//    @Override
//    public void notAuthenticated() {
//
//    }
//    /**
//     * Interface defined in User.IsAuthenticatedTasks
//     */
//    @Override
//    public void isAuthenticated() {
//
//    }
    ////////////////////////////////////////////////
    //  END: Implements User.IsAuthenticatedTasks
    ////////////////////////////////////////////////


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void addCard(Card card) {
        pinnedCards.add(card);
        cardAdapter.notifyItemInserted(pinnedCards.size());
    }
}
