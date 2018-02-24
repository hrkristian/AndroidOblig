package xyz.robertsen.androidoblig;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PinnedCardsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PinnedCardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PinnedCardsFragment extends Fragment {

    private static final String TAG = PinnedCardsFragment.class.getSimpleName();

    Card[] cardlist;
    ArrayList<Card> pinnedCards;

    private RecyclerView recyclerPinned;
    private SearchCardAdapter cardAdapter;

    private ItemTouchHelper itemTouchHelper;
    int dragDirections = ItemTouchHelper.UP |  ItemTouchHelper.DOWN;
    int swipeDirections = ItemTouchHelper.START | ItemTouchHelper.END;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PinnedCardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PinnedCardsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PinnedCardsFragment newInstance(String param1, String param2) {
        PinnedCardsFragment fragment = new PinnedCardsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setSampleCards();
        cardAdapter = new SearchCardAdapter(this.getContext(), pinnedCards);
        itemTouchHelper = getItemTouchHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pinned_cards, container, false);
        recyclerPinned = view.findViewById(R.id.recycler_pinned_cards);
        recyclerPinned.setAdapter(cardAdapter);
        recyclerPinned.setLayoutManager(new LinearLayoutManager(this.getContext()));
        itemTouchHelper.attachToRecyclerView(recyclerPinned);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    private void setSampleCards() {
        int randInt;
        cardlist = Card.getExampleData(this.getContext());
        pinnedCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            randInt = ThreadLocalRandom.current().nextInt(0, cardlist.length);
            pinnedCards.add(cardlist[randInt]);
        }
    }
}
