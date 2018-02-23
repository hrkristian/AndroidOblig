package xyz.robertsen.androidoblig;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Hovedklassen til AndroidOblig
 * ----------------- Dev info, fjernes før levering
 * Opprettet 22. Feb - Kristian
 * Endret ...
 * -----------------
 */
public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Overstyrer metoden ansvarlig for aktivitetens ActionBar
     *
     * ----------------- Dev info, fjernes før levering
     * Begynt 22. Feb - Kristian
     * Endret ...
     * Endret ...
     * -----------------
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Ment å starte en aktivitet fra menyen øverst
     * todo- meningsfullt navn; må så endre ref i action_menu_main.xml
     * @param item
     */
    public void spawnActivity1(MenuItem item) {
        Intent intent = new Intent(this, CardActivity.class);

        startActivity(intent);
    }

    /**
     * Starter SearchActivity, sender ved relevant data
     * Ment å starte et fragment fra menyen øverst
     * todo- meningsfullt navn; må så endre ref i action_menu_main.xml
     * @param item
     */
    public void spawnFragment1(MenuItem item) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
