package xyz.robertsen.androidoblig;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import xyz.robertsen.androidoblig.database.SearchDatabaseOpenHelper;

/**
 * Model class for a recent search item
 *
 */

public class RecentSearchItem {
    private String searchString, user;
    private static SearchDatabaseOpenHelper dbHelper;

    public RecentSearchItem(String searchString, String user) {
        this.searchString = searchString;
        this.user = user;
    }

    public String getSearchString() {
        return searchString;
    }

    public String getUser() {
        return user;
    }

    /**
     * Gets recent search histor from the database.
     * @param context
     * @return
     */
    public static ArrayList<RecentSearchItem> getRecentSearches(Context context) {
        if (dbHelper == null) {
            dbHelper = new SearchDatabaseOpenHelper(context);
        }
        Cursor cursor = dbHelper.getRecentSearchesCursor(User.authenticatedUser.getUsr());
        ArrayList<RecentSearchItem> recentSearchItems = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String searchString = cursor.getString(
                    cursor.getColumnIndex(SearchDatabaseOpenHelper.DBSchema.RecentSearchesTable.SEARCH_STRING)
            );
            String user = cursor.getString(
                    cursor.getColumnIndex(SearchDatabaseOpenHelper.DBSchema.RecentSearchesTable.USER)
            );
            recentSearchItems.add(new RecentSearchItem(searchString, user));
            cursor.moveToNext();
        }
        cursor.close();
        return recentSearchItems;
    }

    /**
     * Adds a new RecentSearchItem to the database. Returns the sqlitedb storage id, or -1
     * if error occured.
     * @param context
     * @param searchString
     * @return
     */
    public static long addRecentSearchItem(Context context, String searchString) {
        if (dbHelper == null) {
            dbHelper = new SearchDatabaseOpenHelper(context);
        }
        return dbHelper.dbAddRecentSearch(searchString, User.authenticatedUser.getUsr());
    }

    @Override
    public String toString() {
        return "RecentSearchItem{" +
                "searchString='" + searchString + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
