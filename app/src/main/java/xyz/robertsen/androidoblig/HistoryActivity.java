package xyz.robertsen.androidoblig;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

import xyz.robertsen.androidoblig.database.SearchDatabaseOpenHelper;

public class HistoryActivity extends AppCompatActivity implements
        PinnedCardsFragment.OnFragmentInteractionListener,
        SearchHistoryFragment.OnFragmentInteractionListener {

    public static User authUser;
    private static final String TAG = HistoryActivity.class.getSimpleName();
    private static final String TAB_POSITION = "xyz.robertsen.androidoblig.HistoryActivity";
    private SearchDatabaseOpenHelper dbHelper;
    private TabLayout tabLayout;
    private TabLayout.Tab recentTab, pinnedTab;
    private Fragment searchHistoryFragment;
    private Fragment pinnedCardFragment;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    int tabPosSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dbHelper = new SearchDatabaseOpenHelper(this);
        // Sets up the basic views and fragments for this activity
        //-- Checks if state saved, sets selected tab pos to the saved instance tab pos //
        init_tabs();
        // Checks for authenticated user

        String username = User.authenticatedUser.getUsr();
        // Check if local version of user exist, if not, create one mathcing authenticated user
        if (!dbHelper.sqliteUserExists(username)) {
            System.out.println("CREATING USER " + username);
            dbHelper.sqliteCreateUser(username);
        } else {
            System.out.println("DID NOT CREATE USER " + username);
        }
        // Set up fragments, fragments handles requests data via LibAPI
        baseActivitySetup();

        if (savedInstanceState != null) {
            tabPosSelected = savedInstanceState.getInt(TAB_POSITION);
            TabLayout.Tab tab = tabLayout.getTabAt(tabPosSelected);
            if (tab != null) {
                tab.select();
            }
        }
    }


    /**
     * Save relevant state information and sends it's data to the next instance of this activity
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(TAB_POSITION, tabLayout.getSelectedTabPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Adapter for paging between fragments.
     */
    class PagerAdapter extends FragmentPagerAdapter  {
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

    private void init_tabs() {
        //--  Sets up TabLayout --//
        tabLayout = findViewById(R.id.search_recent_pinned_layout);
        pinnedTab = tabLayout.newTab();
        recentTab = tabLayout.newTab();
        tabLayout.addTab(pinnedTab);
        tabLayout.addTab(recentTab);
        pinnedTab.setCustomView(R.layout.history_pinned_tab);
        recentTab.setCustomView(R.layout.history_recent_tab);

    }

    void baseActivitySetup() {
        // ViewPager for TabLayout
        viewPager = findViewById(R.id.fragment_container);
        // sampleCards = new ArrayList<>(Arrays.asList(Card.getExampleData(this)));


        // Request instances of fragments, and adapts them to conform with ViewPager interface
        searchHistoryFragment = SearchHistoryFragment.newInstance();
        pinnedCardFragment = PinnedCardsFragment.newInstance();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), pinnedCardFragment, searchHistoryFragment);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: new pos " + position);
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                if (tab != null) {
                    tab.select();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        // Changes currently displayed item in the viewpagers, ∕∕ swaps between the displayed frags.
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
}
