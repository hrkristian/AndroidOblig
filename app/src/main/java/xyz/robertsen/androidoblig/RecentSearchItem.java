package xyz.robertsen.androidoblig;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import xyz.robertsen.androidoblig.database.CardDatabaseOpenHelper;

/**
 * Created by gitsieg on 04.04.18.
 */

public class RecentSearchItem {
        String searchString, user;
        long time;

        public RecentSearchItem(String searchString, long time, String user) {
            this.searchString = searchString;
            this.user = user;
            this.time = time;
        }

    /**
     * Take a Cursor as a parameter. Returns an ArrayList of the users recent searches.
     * @param cursor
     * @return ArrayList<RecentSearchItem>
     */
    public static ArrayList<RecentSearchItem> getRecentSearches(Cursor cursor) {
            ArrayList<RecentSearchItem> recentSearchItems = new ArrayList<>();

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String searchString = cursor.getString(
                        cursor.getColumnIndex(CardDatabaseOpenHelper.DBSchema.RecentSearchesTable.SEARCH_STRING)
                );
                long time = cursor.getInt(
                        cursor.getColumnIndex(CardDatabaseOpenHelper.DBSchema.RecentSearchesTable.UNIX_TIME)
                );
                String user = cursor.getString(
                        cursor.getColumnIndex(CardDatabaseOpenHelper.DBSchema.RecentCardsTable.USER)
                );
                recentSearchItems.add(new RecentSearchItem(searchString, time, user));
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
                ", time=" + time +
                '}';
    }
}
