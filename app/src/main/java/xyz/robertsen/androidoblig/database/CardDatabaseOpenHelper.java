package xyz.robertsen.androidoblig.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by gitsieg on 19.03.18.
 */

public class CardDatabaseOpenHelper extends SQLiteOpenHelper {
    // Log tag
    private static final String TAG = CardDatabaseOpenHelper.class.getSimpleName();
    // Database information
    private static final String DATABASE_NAME = "card_organizer";
    private static final int DATABASE_VERSION = 1; // INCREMENT WHEN STRUCTURAL CHANGES

    public CardDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBSchema.SQL_CREATE_USER);
        db.execSQL(DBSchema.SQL_CREATE_RECENT_CARDS);
        db.execSQL(DBSchema.SQL_CREATE_RECENT_SEARCHES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrades db from version " + oldVersion + " to " + newVersion + ", deletes all data.");
        db.execSQL(DBSchema.SQL_DROP_RECENT_SEARCHES);
        db.execSQL(DBSchema.SQL_DROP_RECENT_CARDS);
        db.execSQL(DBSchema.SQL_DROP_USER);
        onCreate(db);
    }

    /**
     * Database schema for the card_organizer database
     * TABLES: UserTable, RecentCardsTable, RecentSearchesTable
     */
    public final class DBSchema {
        // USER_TABLE
        /////////////
        public abstract class UserTable implements BaseColumns {
            // Table name
            public static final String TABLE_NAME = "user";
            // Columns
            public static final String USER = "user";
            public static final String PASSWORD = "password";
        }

        // SQL CREATE TABLE USER
        public static final String SQL_CREATE_USER =
                "CREATE TABLE " + UserTable.TABLE_NAME + " (" +
                    UserTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UserTable.USER + " TEXT, " +
                    UserTable.PASSWORD + " TEXT " +
                ");";
        // SQL DROP TABLE USER
        public static final String SQL_DROP_USER = "DROP TABLE IF EXISTS " + UserTable.TABLE_NAME + ";";


        // RECENT_CARDS_TABLE
        /////////////////////
        public abstract class RecentCardsTable implements BaseColumns {
            // Table name
            public static final String TABLE_NAME = "recent_cards";
            // Columns
            public static final String CARD_NAME = "name";
            public static final String USER = "user";   // FK -> UserTable(user)
            public static final String PINNED = "pinned";
            public static final String RULES_TEXT = "rules_text";
            public static final String CONVERTED_MANA_COST = "cmc";
        }
        // SQL CREATE TABLE RECENT_CARDS
        public static final String SQL_CREATE_RECENT_CARDS =
                "CREATE TABLE " + RecentCardsTable.TABLE_NAME + " (" +
                    RecentCardsTable._ID + " INTEGER AUTO INCREMENT, " +
                    RecentCardsTable.CARD_NAME + " TEXT, " +
                    RecentCardsTable.USER + " TEXT, " +
                    RecentCardsTable.PINNED + " INTEGER, " +
                    RecentCardsTable.RULES_TEXT + " TEXT, " +
                    RecentCardsTable.CONVERTED_MANA_COST + " INTEGER, " +
                    "FOREIGN KEY (" + RecentCardsTable.USER + ") REFERENCES " + UserTable.TABLE_NAME + "("+UserTable.USER+")" +
                    "PRIMARY KEY (" + RecentCardsTable.USER +", " + RecentCardsTable.CARD_NAME +")" +
                ");";
        // SQL DROP TABLE RECENT CARDS
        public static final String SQL_DROP_RECENT_CARDS ="DROP TABLE IF EXISTS " + RecentCardsTable.TABLE_NAME + ";";

        // RECENT_SEARCHES_TABLE
        ////////////////////////
        public abstract class RecentSearchesTable implements BaseColumns {
            // Table name
            public static final String TABLE_NAME = "recent_searches";
            // Columns
            public static final String SEARCH_STRING = "search_string";
            public static final String USER = "user"; // FK -> UserTable(user)
        }
        // SQL CREATE TABLE RECENT_SEARCHES
        public static final String SQL_CREATE_RECENT_SEARCHES =
                "CREATE TABLE " + RecentSearchesTable.TABLE_NAME + " (" +
                    RecentSearchesTable._ID + " INTEGER AUTO INCREMENT PRIMARY KEY, " +
                    RecentSearchesTable.SEARCH_STRING + " TEXT, " +
                    RecentSearchesTable.USER + " TEXT, " +
                    "FOREIGN KEY (" + RecentSearchesTable.USER + ") REFERENCES " + UserTable.TABLE_NAME + "("+UserTable.USER+")" +
                ");";
        // SQL DROP TABLE RECENT SEARCHES
        public static final String SQL_DROP_RECENT_SEARCHES = "DROP TABLE IF EXISTS " + RecentSearchesTable.TABLE_NAME + ";";
    }

    /**
     * Iterates through the database tables and table-colums, and returns a string of the found values
     * @return String containing the "metadata" for the database.
     */
    public String getDatabaseMetaData() {
        StringBuilder data = new StringBuilder();
        data.append("Metadata for the ").append(DATABASE_NAME).append("database:\n");
        SQLiteDatabase db = getReadableDatabase();
        Cursor tableCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (tableCursor.moveToFirst()) {
            while (!tableCursor.isAfterLast()) {
                String tableName = tableCursor.getString(0);
                Cursor columnCursor = db.query(tableName, null, null, null, null, null, null);
                String[] columns = columnCursor.getColumnNames();
                data.append("----Table name:: ").append(tableName).append("\n");
                for (String column : columns) {
                    data.append("    |----").append(column ).append("\n");
                }
                tableCursor.moveToNext();
            }
        }
        tableCursor.close();
        db.close();
        return data.toString();
    }
}
