package xyz.robertsen.androidoblig;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

        // Find and create recyclerview
//        Log.d(TAG, "Should create RecyclerView");
//        cardAdapter = new SearchCardAdapter(this, recentCards);
//        recyclerSearches = findViewById(R.id.recycler_searchCard);
//        recyclerSearches.setAdapter(cardAdapter);
//        recyclerSearches.setLayoutManager(new LinearLayoutManager(this));




        // TabLayout
        tabRecentPinnedLayout = findViewById(R.id.search_recent_pinned_layout);
        recentTab = tabRecentPinnedLayout.newTab();
        recentTab.setCustomView(R.layout.search_tab_pinned);
        tabRecentPinnedLayout.addTab(recentTab);

//        recentTab.setCustomView(findViewById(R.id.recycler_searchCard));

        pinnedTab = tabRecentPinnedLayout.newTab();
        pinnedTab.setCustomView(R.layout.search_tab_recent);
        tabRecentPinnedLayout.addTab(pinnedTab);

        // Set fragments;
        recentCardsFragment = new RecentCardsFragment();
        pinnedCardsFragment = new PinnedCardsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, pinnedCardsFragment).commit();

        tabRecentPinnedLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("onTabSelected pos" + tab.getPosition());
                swapFragments(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                System.out.println("onTabUnselected pos" + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                System.out.println("onTabReselected pos" + tab.getPosition());
            }
        });
    }

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
