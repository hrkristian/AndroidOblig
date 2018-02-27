package xyz.robertsen.androidoblig;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity implements
        PinnedCardsFragment.OnFragmentInteractionListener,
        RecentCardsFragment.OnFragmentInteractionListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private ArrayList<Card> recentCards, pinnedCards, sampleCards;

    private TabLayout tabRecentPinnedLayout;
    private TabLayout.Tab recentTab, pinnedTab;

    private Fragment recentCardsFragment;
    private Fragment pinnedCardsFragment;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        viewPager = findViewById(R.id.fragment_container);
        sampleCards = new ArrayList<>(Arrays.asList(Card.getExampleData(this)));

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
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), pinnedCardsFragment, recentCardsFragment);
        viewPager.setAdapter(pagerAdapter);
        //        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, pinnedCardsFragment).commit();


        // listener placed on the TabLayout for pinned and recent cards
        tabRecentPinnedLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("onTabSelected pos" + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
//     swapFragments(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    /**
     * Changes the fragments displayed based on the position of the tab that is clicked.
     * <p>
     * Swaps the fragments containing recycler views for pinned and recent card data. The data are
     * generated randomly from a set of 3 cards provided by the Card- class.
     *
     * @param tab
     */
    private void swapFragments(TabLayout.Tab tab) {
        int pos = tab.getPosition();
        Fragment fragment = null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (pos == 0) {
            // Save fragment data for recentCardsFragment here
            fragment = pinnedCardsFragment;
        } else {
            // Save fragment data for pinnedCardsFragment here
            fragment = recentCardsFragment;
        }
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCardsPinned(String title) {
        Card card = null;
        for (Card c : sampleCards) {
            if (c.title.equals(title)) {
                card = c;
                break;
            }
        }
        ((PinnedCardsFragment)pinnedCardsFragment).addCard(card);
    }

    /**
     * Adapter for paging between fragments.
     */

    public class PagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;
        Fragment pinnedFragment, recentFragment;

        public PagerAdapter(FragmentManager fm, Fragment pinnedFragment, Fragment recentFragment) {
            super(fm);
            mNumOfTabs = 2;
            this.pinnedFragment = pinnedFragment;
            this.recentFragment = recentFragment;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return pinnedFragment;
                case 1:
                    return recentFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
