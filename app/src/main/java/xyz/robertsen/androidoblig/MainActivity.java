package xyz.robertsen.androidoblig;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Toast;

/**
 * Hovedklassen til AndroidOblig
 * ----------------- Dev info, fjernes før levering
 * Opprettet 22. Feb - Kristian
 * Endret ...
 * -----------------
 */
public class MainActivity extends AppCompatActivity implements UserFragment.OnFragmentInteractionListener {

    private final int DEFAULT_PLAYERS = 2;

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

    }


    public void rollDice(View view) {
    }


    /**
     * Animerer visningen og fjerningen av app-handling knappene.
     * todo- endre bakgrunnen til FAB ved visning
     * @param view
     */
    public void spawnActionView(final View view) {
        final View v = findViewById(R.id.main_actionView);
        int cx = v.getWidth();
        int cy = v.getHeight();
        float radius = (float)Math.hypot(cx, cy);

        if (v.getVisibility() == View.INVISIBLE) {
            Animator animator = ViewAnimationUtils.createCircularReveal(v, cx/2, cy/2, 0, radius);

            animator.setDuration(400);
            v.setVisibility(View.VISIBLE);
            animator.start();
        } else {
            Animator animator = ViewAnimationUtils.createCircularReveal(v, cx/2, cy/2, radius/2, 0);
            animator.setDuration(400);
            view.setClickable(false);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    v.setVisibility(View.INVISIBLE);
                    view.setClickable(true);
                }
            });
            animator.start();
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
        startActivityWithFABClose(intent);
    }

    public void spawnCardActivity(View view) {
        Intent intent = new Intent(this, CardActivity.class);
        startActivityWithFABClose(intent);
    }
    private void startActivityWithFABClose(Intent intent) {
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
}
