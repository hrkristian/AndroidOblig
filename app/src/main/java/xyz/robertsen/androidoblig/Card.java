package xyz.robertsen.androidoblig;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kris on 23/02/18.
 */

public class Card implements Serializable {
    Context context;

    String name;
    SpannableStringBuilder mana;
    String cmc; // ConvertedManaCost
    String type;
    String stats;
    SpannableStringBuilder text;
    String rules;
    String imageUrl;

    public Card() {

    }

    public Card(Context context,
                String name, String mana, String cmc, String type, String power, String toughness,
                String text, String imageUrl, String rulings) {
        this.context = context;


        this.name = context.getResources().getString(R.string.cardTitlePlaceholder, name);
        this.mana = symbolParser(mana);
        this.cmc = cmc;
        this.type = context.getResources().getString(R.string.cardTypePlaceholder, type);
        this.text = symbolParser(text);
        this.stats = context.getResources().getString(R.string.cardStatsPlaceholder, power, toughness);
        this.rules = rulings;
        this.imageUrl = imageUrl;
    }

    /**
     * Returns the String representation of the
     *
     * @param context
     * @param rulings
     * @return
     * @throws JSONException
     */
    public static String deArrayalize(Context context, JSONArray rulings) throws JSONException {
        StringBuilder builder = new StringBuilder();
        if (rulings != null)
            for (int i = 0; i < rulings.length(); i++)
                builder.append(context.getResources().getString(
                        R.string.cardRulingsItem,
                        rulings.getJSONObject(i).getString("date"),
                        rulings.getJSONObject(i).getString("text")));
        return builder.toString();
    }

    /**
     * Takes an jsonObject, deserializes it and returns a Card
     *
     * @param context
     * @param jsonCard
     * @return Card
     * @throws JSONException
     */
    public static Card newCard(Context context, JSONObject jsonCard) throws JSONException {
        return new Card(
                context,
                (jsonCard.has("name")) ? jsonCard.getString("name") : "",
                (jsonCard.has("manaCost")) ? jsonCard.getString("manaCost") : "",
                (jsonCard.has("cmc")) ? jsonCard.getString("cmc") : "",
                (jsonCard.has("type")) ? jsonCard.getString("type") : "",
                (jsonCard.has("power")) ? jsonCard.getString("power") : "",
                (jsonCard.has("toughness")) ? jsonCard.getString("toughness") : "",
                (jsonCard.has("text")) ? jsonCard.getString("text") : "",
                (jsonCard.has("imageUrl")) ? jsonCard.getString("imageUrl") : "defImageUrl",
                (jsonCard.has("rulings")) ? deArrayalize(context, jsonCard.getJSONArray("rulings")) : null
        );
    }

    private SpannableStringBuilder symbolParser(String source) {
        SpannableStringBuilder bulider = new SpannableStringBuilder("");

        for (int i = 0; i < source.length(); i++) {
            if (source.charAt(i) == '{') {
                if (manaSymbolMap.containsKey(source.substring(i, i + 3))) {
                    Drawable image = context.getResources().getDrawable(manaSymbolMap.get(source.substring(i, i + 3)), null);
                    image.setBounds(0, 0, 30, 30);
                    bulider.append(";", new ImageSpan(image), 0);
                    i += 2;
                }
            } else {
                bulider.append(source.charAt(i));
            }
        }
        return bulider;
    }


    private class Ruling {
        String date, text;

        private Ruling(String date, String text) {
            this.date = date;
            this.text = text;
        }
    }

    static final Map<String, Integer> manaSymbolMap = Collections.unmodifiableMap(new HashMap<String, Integer>() {
        {
            put("{0}", R.drawable.ic_mana_0);
            put("{1}", R.drawable.ic_mana_1);
            put("{2}", R.drawable.ic_mana_2);
            put("{3}", R.drawable.ic_mana_3);
            put("{4}", R.drawable.ic_mana_4);
            put("{5}", R.drawable.ic_mana_5);
            put("{6}", R.drawable.ic_mana_6);
            put("{7}", R.drawable.ic_mana_7);
            put("{8}", R.drawable.ic_mana_8);
            put("{9}", R.drawable.ic_mana_9);
            put("{G}", R.drawable.ic_mana_forest);
            put("{U}", R.drawable.ic_mana_island);
            put("{B}", R.drawable.ic_mana_swamp);
            put("{RecentSearchesTable}", R.drawable.ic_mana_mountain);
            put("{W}", R.drawable.ic_mana_plains);
            put("{X}", R.drawable.ic_mana_x);
            put("{T}", R.drawable.ic_mana_tap);
        }
    });

    @Override
    public String toString() {
        return "Card{" +
                "context=" + context +
                ", name='" + name + '\'' +
                ", mana=" + mana +
                ", cmc='" + cmc + '\'' +
                ", type='" + type + '\'' +
                ", stats='" + stats + '\'' +
                ", text=" + text +
                ", rules='" + rules + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    /*
        public Card(Context context,
                    String name, String mana, String cmc, String type, String power, String toughness,
                    String text, String imageUrl, JSONArray rules) {
            this.context = context;
    */
    public static ArrayList<Card> getSampleCards(Context c) throws JSONException {
        ArrayList<Card> cards = new ArrayList<>();
        String cardName = "";
        for (int i = 0; i < cardNames.length; i++) {
            cards.add(new Card(
                    c,
                    cardNames[i],
                    "{2}{U}{U}",
                    "4",
                    "Jace",
                    "3",
                    "3",
                    "+2: Look at the top card of target player's library. You may put that card on the bottom of that player's library\n+0: Draw three cards, then put two cards from your hand on top of your library in any order.\n−1: Return target creature to its owner's hand.\n−12: Exile all cards from target player's library, then that player shuffles his or her hand into his or her library.",
                    "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=413599&type=card",
                    "def")
            );
        }
        return cards;
    }

    private static String[] cardNames = {
            "Jace Beleren",
            "Goblin Guide",
            "Nissa",
            "Goblin Grenade",
            "Inkmoth",
            "Gurmag Angler",
            "Setessan",
            "Hero's Downfall",
            "Mordi",
            "Monastery Swiftspear",
            "Tormod",
            "Island",
            "Mountain",
            "Plains",
            "Forest",
            "Swamp"
    };
}
