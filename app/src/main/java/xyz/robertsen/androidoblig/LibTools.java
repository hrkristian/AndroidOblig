package xyz.robertsen.androidoblig;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by kris on 29/03/18.
 */

public class LibTools {
    /**
     * Do not use.
     * @param URL The URL where the image is located
     * @param imageView The view to update
     */
    private void getImageFromURL(final String URL, final ImageView imageView) {
        new Thread() {
            public void run(){
                try {
                    URL url = new URL(URL);
                    InputStream stream = (InputStream)url.getContent();
                    imageView.setImageDrawable(Drawable.createFromStream(stream, null));
                } catch (IOException e) {
                    // Catching IOException handles both URL, InputStream,
                    // and createFromStream exceptions
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
