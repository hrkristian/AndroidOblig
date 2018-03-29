package xyz.robertsen.androidoblig;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class HistoryActivity extends AppCompatActivity implements
        PinnedCardsFragment.OnFragmentInteractionListener,
        RecentCardsFragment.OnFragmentInteractionListener {

    private static final String TAG = HistoryActivity.class.getSimpleName();
    private static final String TAB_POSITION = "xyz.robertsen.androidoblig.HistoryActivity";
    private ArrayList<Card> sampleCards;
    private TabLayout tabLayout;
    private TabLayout.Tab recentTab, pinnedTab;
    private Fragment recentFragment;
    private Fragment pinnedFragment;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    int tabPosSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // ViewPager for TabLayout
        viewPager = findViewById(R.id.fragment_container);
        sampleCards = new ArrayList<>(Arrays.asList(Card.getExampleData(this)));

        // TabLayout
        tabLayout = findViewById(R.id.search_recent_pinned_layout);
        recentTab = tabLayout.newTab();
        recentTab.setCustomView(R.layout.search_tab_pinned);
        tabLayout.addTab(recentTab);
        pinnedTab = tabLayout.newTab();
        pinnedTab.setCustomView(R.layout.search_tab_recent);
        tabLayout.addTab(pinnedTab);

        if (savedInstanceState != null) {
            tabPosSelected = savedInstanceState.getInt(TAB_POSITION);
            TabLayout.Tab tab = tabLayout.getTabAt(tabPosSelected);
            if (tab != null) {
                tab.select();
            }
        }

        // Set fragments;
        recentFragment = RecentCardsFragment.newInstance();
        pinnedFragment = PinnedCardsFragment.newInstance();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), pinnedFragment, recentFragment);
        viewPager.setAdapter(pagerAdapter);

        // listener placed on the TabLayout for pinned and recent cards
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("onTabSelected pos" + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(TAB_POSITION, tabLayout.getSelectedTabPosition());
        super.onSaveInstanceState(outState);
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
        ((PinnedCardsFragment) pinnedFragment).addCard(card);
    }

    /**
     * Adapter for paging between fragments.
     */
    public class PagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;
        Fragment pinnedFragment, recentFragment;

        private PagerAdapter(FragmentManager fm, Fragment pinnedFragment, Fragment recentFragment) {
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
