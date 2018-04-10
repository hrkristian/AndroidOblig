package xyz.robertsen.androidoblig;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by kris on 23/02/18.
 */

public class Card implements Serializable {
    String title;
    String text;
    String[] rulings;
    Drawable image, cropImage;
    int convertedManaCost;

    public Card(){}
    public Card(String title, String text, String[] rulings, Drawable image, Drawable cropImage, int convertedManaCost) {
        this.title = title;
        this.text = text;
        this.rulings = rulings;
        this.image = image;
        this.cropImage = cropImage;
        this.convertedManaCost = convertedManaCost;
    }

    public static Card[] getExampleData(Context context) {
        Card[] cards =  {new Card(
                "Jace, the Mind Sculptor",
                "+2: Look at the top card of target player's library. You may put that card on the bottom of that player's library.\n0: Draw three cards, then put two cards from your hand on top of your library in any order.\\n−1: Return target creature to its owner's hand.\\n−12: Exile all cards from target player's library, then that player shuffles his or her hand into his or her library.",
                "23/02-18|ya gon get rek'd".split("|"),
                context.getResources().getDrawable(R.drawable.jace_mind_sculptor, null),
                context.getResources().getDrawable(R.drawable.jace_mind_sculptor_crop, null),
                4),
                new Card(
                "Goblin Guide",
                "Haste\nWhenever Goblin Guide attacks, defending player reveals \nthe top card of his or her library. If it's a land card, \"\nthat player puts it into his or her hand.",
                "23/02-18|Mana cost reduced when you point out he's high maintenance".split("|"),
                context.getResources().getDrawable(R.drawable.goblin_guide, null),
                context.getResources().getDrawable(R.drawable.goblin_guide_crop, null),
                1),
                new Card(
                "Lightning Bolt",
                "Lightning Bolt deals 3 damage to target creature or player.",
                "23/02-18|You will die form the green stuff.".split("|"),
                context.getResources().getDrawable(R.drawable.lightning_bolt, null),
                context.getResources().getDrawable(R.drawable.lightning_bolt_crop, null),
                1)
        };
        return cards;
    }
}
