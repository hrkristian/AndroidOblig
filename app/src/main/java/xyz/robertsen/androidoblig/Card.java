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
    String mana;
    String cmc; // ConvertedManaCost
    String type;
    String stats;
    String power, toughness;
    String text;
    String rules;
    String imageUrl;
    String notes;
    public Card() {

    }

    public Card(Context context,
                String name, String mana, String cmc, String type, String power, String toughness,
                String text, String imageUrl, String rulings, String notes) {
        this.context = context;
        this.name = context.getResources().getString(R.string.cardTitlePlaceholder, name);
        this.mana = mana; // getter SpannableStringBuilder
        this.cmc = cmc;
        this.type = type;
        this.text = text; // getter SpannableStringBuiler
        this.power = power;
        this.toughness = toughness;
        this.stats = context.getResources().getString(R.string.cardStatsPlaceholder, power, toughness);
        this.rules = rulings;
        this.imageUrl = imageUrl;
        this.notes = notes;
    }

    /**
     * Returns the card text, spanned
     *
     * @return text
     */
    SpannableStringBuilder getSpanText() {
        return symbolParser(text);
    }

    /**
     * Returns the card's mana cost, spanned
     *
     * @return
     */
    SpannableStringBuilder getSpanManaCost() {
        return symbolParser(mana);
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
        JSONArray arrRulings = jsonCard.optJSONArray("rulings");
        String strRulings = jsonCard.optString("rulings", "");

        return new Card(
                context,
                (jsonCard.has("name")) ? jsonCard.getString("name") : "",
                (jsonCard.has("manaCost")) ? jsonCard.getString("manaCost") : "",
                (jsonCard.has("cmc")) ? jsonCard.getString("cmc") : "",
                (jsonCard.has("type")) ? jsonCard.getString("type") : "",
                (jsonCard.has("power")) ? jsonCard.getString("power") : "",
                (jsonCard.has("toughness")) ? jsonCard.getString("toughness") : "",
                (jsonCard.has("text")) ? jsonCard.getString("text") : "",
                (jsonCard.has("imageUrl")) ? jsonCard.getString("imageUrl") : "",
                (arrRulings == null) ? strRulings : deArrayalize(context, arrRulings),
                /*
                    If the response comes from the MagicOblig db, it has the "pos" attribute
                    IF the response comes from MtgApi, it doesn't, and defaults to -1
                 */
                (jsonCard.has("notes")) ? jsonCard.getString("notes") : ""
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
            put("{R}", R.drawable.ic_mana_mountain);
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
}
