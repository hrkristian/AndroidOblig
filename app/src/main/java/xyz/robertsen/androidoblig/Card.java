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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kris on 23/02/18.
 */

public class Card {
    Context context;

    String name;
    SpannableStringBuilder mana;
    String type;
    String stats;
    String cmc; // ConvertedManaCost
    SpannableStringBuilder text;
    String rules;
    String imageUrl;

    public Card(Context context,
                String name, String mana, String cmc, String type, String power, String toughness,
                String text, String imageUrl, JSONArray rules) {
        this.context = context;


        this.name = context.getResources().getString(R.string.cardTitlePlaceholder, name);
        this.mana = symbolParser("Mana: ", mana);
        this.cmc = context.getResources().getString(R.string.cardTitlePlaceholder, cmc);
        this.type = context.getResources().getString(R.string.cardTypePlaceholder, type);
        this.text = symbolParser("", text);
        this.stats = context.getResources().getString(R.string.cardStatsPlaceholder, power, toughness);

        this.imageUrl = imageUrl;
        try {
            StringBuilder builder = new StringBuilder();
            if (rules != null)
                for (int i = 0; i < rules.length(); i++)
                    builder.append(context.getResources().getString(
                            R.string.cardRulingsItem,
                            rules.getJSONObject(i).getString("date"),
                            rules.getJSONObject(i).getString("text")));
            this.rules = builder.toString();
        } catch (JSONException e) {
            System.out.println("Error in Card:\n");
            e.printStackTrace();
        }
    }

    private SpannableStringBuilder symbolParser(String start, String source) {
        SpannableStringBuilder builder = new SpannableStringBuilder(start);

        for (int i = 0; i < source.length(); i++) {
            if (manaSymbolMap.containsKey(source.substring(i, i + 3))) {
                Drawable image = context.getResources().getDrawable(manaSymbolMap.get(source.substring(i, i + 3)), null);
                image.setBounds(0, 0, 50, 50);
                builder.append(";", new ImageSpan(image), 0);
                i += 2;
            } else {
                builder.append(source.charAt(i));
            }
        }
        return builder;
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
}
