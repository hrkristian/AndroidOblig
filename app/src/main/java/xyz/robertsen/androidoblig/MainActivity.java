package xyz.robertsen.androidoblig;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;

/**
 * Hovedklassen til AndroidOblig
 * ----------------- Dev info, fjernes før levering
 * Opprettet 22. Feb - Kristian
 * Endret ...
 * -----------------
 */
public class MainActivity extends AppCompatActivity
        implements UserFragment.OnFragmentInteractionListener, SearchView.OnQueryTextListener {

    private final static String ARG_LIFE_PLAYER1 = "life_player2";
    private final static String ARG_LIFE_PLAYER2 = "life_player2";
    private final static String ARG_ACTIONVIEW_VISIBLE = "actionView";

    private final int DEFAULT_PLAYERS = 2;
    private final int DEFAULT_LIFE = 20;
    private int life_player1, life_player2;

    View actionView;
    FloatingActionButton actionFAB;
    TransitionDrawable actionFABTransitionDrawable;

    // todo- Map istedenfor Array; implementer Comparable
    public static User[] logins = {
            new User("kristian", "pwd"),
            new User("nikolai", "pwd"),
            new User("atle", "pwd")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionView = findViewById(R.id.main_actionView);
        actionFAB = findViewById(R.id.main_fab_settings);
        actionFABTransitionDrawable = (TransitionDrawable)actionFAB.getDrawable();

        if (savedInstanceState != null) {
            life_player1 = savedInstanceState.getInt(ARG_LIFE_PLAYER1);
            life_player2 = savedInstanceState.getInt(ARG_LIFE_PLAYER2);

            if (savedInstanceState.getBoolean(ARG_ACTIONVIEW_VISIBLE))
                actionView.setVisibility(View.VISIBLE);
        } else {
            life_player1 = DEFAULT_LIFE;
            life_player2 = DEFAULT_LIFE;
        }

        ((TextView)findViewById(R.id.text_lifeCounter1)).setText(Integer.toString(life_player1));
        ((TextView)findViewById(R.id.text_lifeCounter2)).setText(Integer.toString(life_player2));


        initSearchView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_LIFE_PLAYER1, life_player1);
        outState.putInt(ARG_LIFE_PLAYER2, life_player2);

        outState.putBoolean(ARG_ACTIONVIEW_VISIBLE, actionView.getVisibility() == View.VISIBLE);
    }

    public void rollDice(View view) {
    }


    /**
     * Animerer visningen og fjerningen av app-handling knappene.
     * todo- endre bakgrunnen til FAB ved visning
     * @param view
     */
    public void spawnActionView(final View view) {
        final int TRANSITION_DURATION = 400;
        final View v = findViewById(R.id.main_actionView);
        int cx = v.getWidth();
        int cy = v.getHeight();
        float radius = (float)Math.hypot(cx, cy);

        if (v.getVisibility() == View.INVISIBLE) {
            Animator animator = ViewAnimationUtils.createCircularReveal(v, cx/2, cy/2, 0, radius);

            animator.setDuration(TRANSITION_DURATION);
            v.setVisibility(View.VISIBLE);
            animator.start();
            actionFABTransitionDrawable.startTransition(TRANSITION_DURATION);
        } else {
            Animator animator = ViewAnimationUtils.createCircularReveal(v, cx/2, cy/2, radius/2, 0);
            animator.setDuration(TRANSITION_DURATION);
            if (view != null)
                view.setClickable(false);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    v.setVisibility(View.INVISIBLE);
                    if (view != null)
                        view.setClickable(true);
                }
            });
            animator.start();
            actionFABTransitionDrawable.reverseTransition(TRANSITION_DURATION);
        }
    }

    @Override
    public void onBackPressed() {
        if ( findViewById(R.id.main_actionView).getVisibility() == View.VISIBLE )
            spawnActionView(findViewById(R.id.main_fab_settings));
        else
            super.onBackPressed();
    }

    /* Activities and fragments */
    public void spawnSearchActivity(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        spawnActivityWithFABClose(intent);
    }

    public void spawnCardActivity(View view) {
        Intent intent = new Intent(this, CardActivity.class);
        spawnActivityWithFABClose(intent);
    }
    private void spawnActivityWithFABClose(Intent intent) {
        spawnActionView(findViewById(R.id.main_fab_settings));
        startActivity(intent);
    }

    public void spawnLoginFragment(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment previous = getFragmentManager().findFragmentByTag("userLoginFragment");
        if (previous != null)
            ft.remove(previous);
        ft.addToBackStack(null);

        UserFragment uf = UserFragment.newInstance(DEFAULT_PLAYERS, R.layout.fragment_user);
        uf.show(ft, "userLoginFragment");
        spawnActionView(findViewById(R.id.main_fab_settings));
    }
    public void changeLifeRemaining(View view) {
        TextView v;
        switch (view.getId()) {
            case R.id.button_main_p1_plus:
                v = findViewById(R.id.text_lifeCounter1);
                v.setText(Integer.toString(++life_player1));
                break;
            case R.id.button_main_p1_minus:
                v = findViewById(R.id.text_lifeCounter1);
                v.setText(Integer.toString(--life_player1));
                break;
            case R.id.button_main_p2_plus:
                v = findViewById(R.id.text_lifeCounter2);
                v.setText(Integer.toString(++life_player2));
                break;
            case R.id.button_main_p2_minus:
                v = findViewById(R.id.text_lifeCounter2);
                v.setText(Integer.toString(--life_player2));
                break;
        }
    }

    /* Callbacks */
    @Override
    public void onLoginButtonPressed(String usr, String pwd) {

        for (User u : logins) {
            if (u.isMatch(usr, pwd)) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                Fragment oldFragment = getFragmentManager().findFragmentByTag("userLoginFragment");
                UserFragment newFragment = UserFragment.newInstance(DEFAULT_PLAYERS, R.layout.fragment_user_authenticated);

                ft.addToBackStack(null);

//                uf.show(ft, "userAuthenticatedFragment");

                Toast.makeText(this, "Login correct.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(this, "Login incorrect.", Toast.LENGTH_SHORT).show();
    }


    private void initSearchView() {
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = findViewById(R.id.search_main_cardSearch);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        Intent intent = new Intent(getApplicationContext(), CardActivity.class);
        intent.setAction(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, s);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
