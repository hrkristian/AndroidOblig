package xyz.robertsen.androidoblig;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Hovedklassen til AndroidOblig
 * ----------------- Dev info, fjernes før levering
 * Opprettet 22. Feb - Kristian
 * Endret ...
 * -----------------
 */
public class MainActivity extends AppCompatActivity implements UserFragment.OnFragmentInteractionListener {

    private final int DEFAULT_PLAYERS = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void spawnLoginFragment(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment previous = getFragmentManager().findFragmentByTag("userLoginFragment");
        if (previous != null)
            ft.remove(previous);
        ft.addToBackStack(null);

        UserFragment uf = UserFragment.newInstance(DEFAULT_PLAYERS);
        uf.show(ft, "userLoginFragment");
    }


    public void rollDice(View view) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

    public void spawnSearchActivity(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);

    }

    public void spawnCardActivity(View view) {
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);
    }
}
