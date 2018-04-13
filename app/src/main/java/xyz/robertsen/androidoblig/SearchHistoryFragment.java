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

import java.util.ArrayList;
import java.util.Collections;

import xyz.robertsen.androidoblig.database.CardDatabaseOpenHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchHistoryFragment extends Fragment {

    private static final String TAG = SearchHistoryFragment.class.getSimpleName();
    CardDatabaseOpenHelper dbHelper;
    ArrayList<RecentSearchItem> recentSearchItems;
    private RecyclerView recyclerRecent;
    private HistoryAdapter cardAdapter;
    int dragDirections = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    int swipeDirections = ItemTouchHelper.START | ItemTouchHelper.END;
    private ItemTouchHelper itemTouchHelper;
    OnFragmentInteractionListener mListener;

    public SearchHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method for creating a new instance of this fragment
     *
     * @return A new instance of SearchHistoryFragment
     */
    public static SearchHistoryFragment newInstance() {
        return new SearchHistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO Testing
        dbHelper = new CardDatabaseOpenHelper(this.getContext());
//        for (int i = 0; i < 20; i++) {
//            Log.d(TAG, dbHelper.dbAddRecentSearch("Nikolai strings " + i , HistoryActivity.authUser.getUsername()) + "");
//        }
        itemTouchHelper = getItemTouchHelper();

        // Fragment is retained across Activity re-creation
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        // TODO Replace with User.authenticatedUser
        recentSearchItems = RecentSearchItem.getRecentSearches(getContext());
        cardAdapter = new HistoryAdapter(this.getContext(), recentSearchItems);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent_searches, container, false);
        recyclerRecent = view.findViewById(R.id.recycler_recent_cards);
        recyclerRecent.setAdapter(cardAdapter);
        recyclerRecent.setLayoutManager(new LinearLayoutManager(this.getContext()));
        itemTouchHelper.attachToRecyclerView(recyclerRecent);

        // Ensures that the CardViews in the recycler view is centered when the layout is horizontal
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRecyclerHorizontalOffsets();
        }
        return view;
    }

    private void setRecyclerHorizontalOffsets() {
        recyclerRecent.addItemDecoration(new RecyclerView.ItemDecoration() {
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

    /**
     * Returns a Item
     *
     * @return ItemTouchHelper for use on a RecyclerView
     */
    public ItemTouchHelper getItemTouchHelper() {

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(dragDirections, swipeDirections) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(recentSearchItems, from, to);
                cardAdapter.notifyItemMoved(from, to);

                // TODO: Query database, change card indexes
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                recentSearchItems.remove(pos);
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

        void onCardsPinned(String title);
    }
}
