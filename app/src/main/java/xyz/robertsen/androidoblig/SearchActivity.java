package xyz.robertsen.androidoblig;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements PinnedCardsFragment.OnFragmentInteractionListener, RecentCardsFragment.OnFragmentInteractionListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private Card[] cardlist;
    private ArrayList<Card> recentCards, pinnedCards;

    private TabLayout tabRecentPinnedLayout;
    private TabLayout.Tab recentTab, pinnedTab;

    private Fragment recentCardsFragment;
    private Fragment pinnedCardsFragment;


//





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // TabLayout
        tabRecentPinnedLayout = findViewById(R.id.search_recent_pinned_layout);
        recentTab = tabRecentPinnedLayout.newTab();
        recentTab.setCustomView(R.layout.search_tab_pinned);
        tabRecentPinnedLayout.addTab(recentTab);

        pinnedTab = tabRecentPinnedLayout.newTab();
        pinnedTab.setCustomView(R.layout.search_tab_recent);
        tabRecentPinnedLayout.addTab(pinnedTab);

        // Set fragments;
        recentCardsFragment = new RecentCardsFragment();
        pinnedCardsFragment = new PinnedCardsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, pinnedCardsFragment).commit();


        // listener placed on the TabLayout for pinned and recent cards
        tabRecentPinnedLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("onTabSelected pos" + tab.getPosition());
                swapFragments(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    /**
     * Changes the fragments displayed based on the position of the tab that is clicked.
     *
     * Swaps the fragments containing recycler views for pinned and recent card data. The data are
     * generated randomly from a set of 3 cards provided by the Card- class.
     * @param tab
     */
    private void swapFragments(TabLayout.Tab tab) {
        int pos = tab.getPosition();
        Fragment fragment = null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (pos == 0) {
            fragment = pinnedCardsFragment;
        } else {
            fragment = recentCardsFragment;
        }
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
