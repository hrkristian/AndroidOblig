package xyz.robertsen.androidoblig;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by kris on 23/02/18.
 */

public class Card {
    String title;
    String text;
    String[] rulings;
    Drawable image;
    int convertedManaCost;

    public Card(String title, String text, String[] rulings, Drawable image, int convertedManaCost) {
        this.title = title;
        this.text = text;
        this.rulings = rulings;
        this.image = image;
        this.convertedManaCost = convertedManaCost;
    }

    public static Card[] getExampleData(Context context) {
        Card[] cards =  {new Card(
                "Jayce",
                "He's the greatest man alive",
                "23/02-18|ya gon get rek'd".split("|"),
                context.getResources().getDrawable(R.drawable.shape_card_background, null),
                5),
                new Card(
                "Nissa",
                "She's aight",
                "23/02-18|Mana cost reduced when you point out he's high maintenance".split("|"),
                context.getResources().getDrawable(R.drawable.shape_card_background, null),
                12),
                new Card(
                "Plaguewalker",
                "Do not step in the green stuff.",
                "23/02-18|You will die form the green stuff.".split("|"),
                context.getResources().getDrawable(R.drawable.shape_card_background, null),
                3)
        };
        return cards;
    }
}
