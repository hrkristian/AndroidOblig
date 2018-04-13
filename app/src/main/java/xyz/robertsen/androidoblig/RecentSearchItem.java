package xyz.robertsen.androidoblig;

import android.database.Cursor;

import java.util.ArrayList;

import xyz.robertsen.androidoblig.database.CardDatabaseOpenHelper;

/**
 * Created by gitsieg on 04.04.18.
 */

public class RecentSearchItem {
    String searchString, user;

    public RecentSearchItem(String searchString, String user) {
        this.searchString = searchString;
        this.user = user;
    }

    /**
     * Take a Cursor as a parameter. Returns an ArrayList of the users recent searches.
     *
     * @param cursor
     * @return ArrayList<RecentSearchItem>
     */
    public static ArrayList<RecentSearchItem> getRecentSearchesFromCursor(Cursor cursor) {
        ArrayList<RecentSearchItem> recentSearchItems = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String searchString = cursor.getString(
                    cursor.getColumnIndex(CardDatabaseOpenHelper.DBSchema.RecentSearchesTable.SEARCH_STRING)
            );
            String user = cursor.getString(
                    cursor.getColumnIndex(CardDatabaseOpenHelper.DBSchema.RecentSearchesTable.USER)
            );
            recentSearchItems.add(new RecentSearchItem(searchString, user));
            cursor.moveToNext();
        }
        cursor.close();
        return recentSearchItems;
    }

    @Override
    public String toString() {
        return "RecentSearchItem{" +
                "searchString='" + searchString + '\'' +
                ", user='" + user + '\'' +
                '}';
    }

    public static ArrayList<RecentSearchItem> generateSampleRecentSearches() {
        ArrayList<RecentSearchItem> items = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            items.add(new RecentSearchItem(
                    "Goblin Guide",
                    "nikolai"
            ));
        }
        return items;
    }
}
