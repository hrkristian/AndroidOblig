package xyz.robertsen.androidoblig;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import xyz.robertsen.androidoblig.database.CardDatabaseOpenHelper;

/**
 * Hovedklassen til AndroidOblig
 */
public class MainActivity extends AppCompatActivity
        implements  UserFragment.userFragmentListener,
                    SearchView.OnQueryTextListener,
                    User.ValidationListener,
                    LibAPI.RequestListener {

    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private final static String ARG_LIFE_PLAYER1 = "life_player2";
    private final static String ARG_LIFE_PLAYER2 = "life_player2";
    private final static String ARG_ACTIONVIEW_VISIBLE = "actionView";

    private final int DEFAULT_PLAYERS = 2;
    private final int DEFAULT_LIFE = 20;
    private int life_player1, life_player2;

    View actionView;
    FloatingActionButton actionFAB;
    TransitionDrawable actionFABTransitionDrawable;
    FragmentManager fragmentManager;
    EventActions eventActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionView = findViewById(R.id.main_actionView);
        actionFAB = findViewById(R.id.main_fab_settings);
        fragmentManager = getFragmentManager();
        eventActions = new EventActions(this);

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
        init_FABDrawable();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_LIFE_PLAYER1, life_player1);
        outState.putInt(ARG_LIFE_PLAYER2, life_player2);

        outState.putBoolean(ARG_ACTIONVIEW_VISIBLE, actionView.getVisibility() == View.VISIBLE);
    }

    /**
     * Performs a dice roll (1-6) and displays the result as a Toast.
     * @param view the event-view
     */
    public void rollDice(View view) {
        eventActions.rollDice();
    }

    /**
     * Animates the showing and hiding of the "ActionView"
     * todo- remove ability to click through the ActionView || switch it out with a Fragment
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
            actionFABTransitionDrawable.startTransition(0);
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
            actionFABTransitionDrawable.reverseTransition(0);
        }
    }

    /**
     * Changes the remaining life of a player based on which view fires the event
     * @param view the event view
     */
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

    /* ------------ Interface methods ----------- */
    @Override
    public boolean onQueryTextSubmit(String s) {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        intent.setAction(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, s);

        eventActions.spawnActivity(intent);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    /* ------------ Activities and fragments ----------- */
    public void spawnSearchActivity(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        eventActions.spawnActivity(intent);
    }

    final private static String FRAGMENT_USER = "userFragment";
    public void spawnUserFragment(View view) {
        if (User.isAuthenticated())
            eventActions.spawnUserFragment(R.layout.fragment_user_authenticated);
        else
            eventActions.spawnUserFragment(R.layout.fragment_user);
    }

    /* ------------ Callbacks ----------- */
    @Override
    public void onUserFragmentLoginButtonPressed(String usr, String pwd) {
        try {
            User.authenticateUser(usr, pwd, this, this);
        } catch(IllegalStateException e) {
            Toast.makeText(
                    this,
                    "Error! User already authenticated.\nThis should not happen.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
    @Override
    public void onUserFragmentCancelButtonPressed(DialogFragment fragment) {
        fragmentManager.popBackStack();
    }

    @Override
    public void handleValidationResponse(JSONObject response) {
        String message;
        try {
            if (!response.has("login")) {
                Log.i("JSON Response", response.toString(2));
                return;
            }

            if (response.getString("login").equals("username"))
                message = "Login Failed: Wrong username";
            else if (response.getString("login").equals("password"))
                message = "Login Failed: Wrong password";
            else {
                message = "Login Succeeded";
                User.setAuthenticatedUser(new User(
                    response.getString("usr"),
                    response.getString("fornavn"),
                    response.getString("etternavn")
                ));
            }
        } catch (JSONException e) {
            message = "JSON Exception while authenticating";
            e.printStackTrace();
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        eventActions.spawnUserFragment(R.layout.fragment_user_authenticated);
    }
    @Override
    public void handlePinnedCardsResponse(JSONObject response) {
        try {
            Log.i("Pin Response", "Pin response received");
            Log.i("Pin Response", response.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handlePinnedCardsError(VolleyError error) {
        Log.i("Pinned Error", error.toString());
    }


    /* ------------ Init methods ----------- */
    private void init_FABDrawable() {
        actionFABTransitionDrawable = new TransitionDrawable( new Drawable[]{
                getResources().getDrawable(R.drawable.icon_expand),
                getResources().getDrawable(R.drawable.icon_collapse)
        });
        actionFAB.setImageDrawable(actionFABTransitionDrawable);
        actionFABTransitionDrawable.setCrossFadeEnabled(true);
    }

    private void initSearchView() {
        SearchView searchView = findViewById(R.id.search_main_cardSearch);
        searchView.setOnQueryTextListener(this);
    }

    private class EventActions {
        Context c;
        private EventActions(Context c) {
            this.c = c;
        }
        private void rollDice() {
            LibAPI.request(
                    (LibAPI.RequestListener)c,
                    c,
                    new Card(),
                    LibAPI.REQUEST.CARD_GET);
            spawnActionView(findViewById(R.id.main_fab_settings));
            String diceText = "Terningkast: "
                    .concat( Integer.toString((int)(Math.random() * 6 + 1)) );
            Toast.makeText(c, diceText, Toast.LENGTH_SHORT).show();
        }
        private void spawnActivity(Intent intent) {
            spawnActionView(findViewById(R.id.main_fab_settings));
            startActivity(intent);
        }
        private void spawnUserFragment(int layoutId) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            UserFragment previous = (UserFragment)fragmentManager.findFragmentByTag(FRAGMENT_USER);
            if (previous != null)
                fragmentManager.popBackStack();

            ft.addToBackStack(null);

            UserFragment uf = UserFragment.newInstance(DEFAULT_PLAYERS, layoutId);
            uf.show(ft, FRAGMENT_USER);
            spawnActionView(findViewById(R.id.main_fab_settings));
        }
    }
}
