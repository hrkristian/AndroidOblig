package xyz.robertsen.androidoblig.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import xyz.robertsen.androidoblig.User;

public class CardDatabaseOpenHelper extends SQLiteOpenHelper {
    // Log tag
    private static final String TAG = CardDatabaseOpenHelper.class.getSimpleName();
    // Database information
    private static final String DATABASE_NAME = "card_organizer";
    private static final int DATABASE_VERSION = 2; // INCREMENT WHEN STRUCTURAL CHANGES

    SQLiteDatabase mWritableDatabase = null, mReadableDatabase = null;

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
    public static final class DBSchema {
        // USER_TABLE
        /////////////
        public abstract class UserTable implements BaseColumns {
            // Table name
            public static final String TABLE_NAME = "user";
            // Columns
            public static final String USER = "user";
            public static final String PASSWORD = "password";
        }

        // USER COLUMNS
        public final String[] USER_COLUMNS = {
                UserTable.USER,
                UserTable.PASSWORD
        };

        // SQL CREATE TABLE USER
        public static final String SQL_CREATE_USER =
                "CREATE TABLE " + UserTable.TABLE_NAME + " (" +
                        UserTable._ID + " INTEGER AUTO INCREMENT, " +
                        UserTable.USER + " TEXT PRIMARY KEY NOT NULL, " +
                        UserTable.PASSWORD + " TEXT NOT NULL" +
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
            public static final String UNIX_TIME = "unix_time";
        }

        // RECENT_CARDS_COLUMNS
        public static final String[] RECENT_CARDS_COLUMNS = {
                RecentCardsTable.CARD_NAME,
                RecentCardsTable.USER,
                RecentCardsTable.PINNED,
                RecentCardsTable.RULES_TEXT,
                RecentCardsTable.CONVERTED_MANA_COST,
                RecentCardsTable.UNIX_TIME
        };

        // SQL CREATE TABLE RECENT_CARDS
        public static final String SQL_CREATE_RECENT_CARDS =
                "CREATE TABLE " + RecentCardsTable.TABLE_NAME + " (" +
                        RecentCardsTable._ID + " INTEGER AUTO INCREMENT, " +
                        RecentCardsTable.CARD_NAME + " TEXT, " +
                        RecentCardsTable.USER + " TEXT, " +
                        RecentCardsTable.PINNED + " INTEGER, " +
                        RecentCardsTable.RULES_TEXT + " TEXT, " +
                        RecentCardsTable.CONVERTED_MANA_COST + " INTEGER, " +
                        RecentCardsTable.UNIX_TIME + "INTEGER," +
                        "FOREIGN KEY (" + RecentCardsTable.USER + ") REFERENCES " + UserTable.TABLE_NAME + "(" + UserTable.USER + ")" +
                        "PRIMARY KEY (" + RecentCardsTable.USER + ", " + RecentCardsTable.CARD_NAME + ")" +
                        ");";
        // SQL DROP TABLE RECENT CARDS
        public static final String SQL_DROP_RECENT_CARDS = "DROP TABLE IF EXISTS " + RecentCardsTable.TABLE_NAME + ";";

        // RECENT_SEARCHES_TABLE
        ////////////////////////
        public abstract class RecentSearchesTable implements BaseColumns {
            // Table name
            public static final String TABLE_NAME = "recent_searches";
            // Columns
            public static final String SEARCH_STRING = "search_string";
            public static final String USER = "user"; // FK -> UserTable(user)
            public static final String UNIX_TIME = "unix_time";
        }

        // RECENT SEARCHES COLUMNS
        public static final String[] RECENT_SEARCHES_COLUMNS = {
                RecentSearchesTable.SEARCH_STRING,
                RecentSearchesTable.USER,
                RecentSearchesTable.UNIX_TIME
        };

        // SQL CREATE TABLE RECENT_SEARCHES
        public static final String SQL_CREATE_RECENT_SEARCHES =
                "CREATE TABLE " + RecentSearchesTable.TABLE_NAME + " (" +
                        RecentSearchesTable._ID + " INTEGER AUTO INCREMENT PRIMARY KEY, " +
                        RecentSearchesTable.SEARCH_STRING + " TEXT, " +
                        RecentSearchesTable.USER + " TEXT, " +
                        RecentSearchesTable.UNIX_TIME + " INTEGER," +
                        "FOREIGN KEY (" + RecentSearchesTable.USER + ") REFERENCES " + UserTable.TABLE_NAME + "(" + UserTable.USER + ")" +
                        ");";
        // SQL DROP TABLE RECENT SEARCHES
        public static final String SQL_DROP_RECENT_SEARCHES = "DROP TABLE IF EXISTS " + RecentSearchesTable.TABLE_NAME + ";";
    }

    /**
     * Iterates through the database tables and table-colums, and returns a string of the found values
     *
     * @return String containing the "metadata" for the database.
     */
    public String getDatabaseMetaData() {
        StringBuilder data = new StringBuilder();
        data.append("Metadata for the ").append(DATABASE_NAME).append("database:\n");
        SQLiteDatabase db = getReadableDatabase();
        Cursor tableCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        Cursor columnCursor = null;
        if (tableCursor.moveToFirst()) {
            while (!tableCursor.isAfterLast()) {
                String tableName = tableCursor.getString(0);
                columnCursor = db.query(tableName, null, null, null, null, null, null);
                String[] columns = columnCursor.getColumnNames();
                data.append("----Table name:: ").append(tableName).append("\n");
                for (String column : columns) {
                    data.append("    |----").append(column).append("\n");
                }
                tableCursor.moveToNext();
            }
        }
        tableCursor.close();
        columnCursor.close();
        db.close();
        return data.toString();
    }

    /**
     * Seeds the user table with user data.
     * @param users - Array of users to be created
     */
    public void seedUsers(User[] users) {
        // Lazy initialization, OK for single-threaded usage
        mWritableDatabase = getWritableDatabase();

        ContentValues values;
        for (User user : users) {
            values = new ContentValues();
            values.put(
                    CardDatabaseOpenHelper.DBSchema.UserTable.USER,
                    user.getUsr());
            values.put(
                    CardDatabaseOpenHelper.DBSchema.UserTable.PASSWORD,
                    user.getPwd());
            try {
                user.setId(mWritableDatabase.insert(DBSchema.UserTable.TABLE_NAME, null, values));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mWritableDatabase.close();
    }

    /**
     * Adds a card to the Recentcards table.
     * @param searchString
     * @param user
     */
    public void dbAddRecentSearch(String searchString, User user) {
        mWritableDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.RecentSearchesTable.SEARCH_STRING, searchString);
        values.put(DBSchema.RecentSearchesTable.USER, user.getUsr());
        values.put(DBSchema.RecentSearchesTable.UNIX_TIME, System.currentTimeMillis());
        mWritableDatabase.insert(DBSchema.RecentSearchesTable.TABLE_NAME, null, values);
        mWritableDatabase.close();
    }

    /**
     * Fetches recent search data for a given user
     * @param user - The user that the query will be filtered by
     * @return Cursor that points at the first row of the resultset
     */
    public Cursor getRecentSearches(User user) {
        if (mReadableDatabase == null)
            mReadableDatabase = getReadableDatabase();

        Cursor cursor = mReadableDatabase.query(
                DBSchema.RecentSearchesTable.TABLE_NAME,
                DBSchema.RECENT_SEARCHES_COLUMNS,
                DBSchema.RecentSearchesTable.USER + "='" + user.getUsr()+"'",
                null, null, null,
                DBSchema.RecentSearchesTable.UNIX_TIME + " DESC"
        );
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getRecentCards(User user) {
        return null;
    }

    public void addRecentCard(User user) {

    }
}
