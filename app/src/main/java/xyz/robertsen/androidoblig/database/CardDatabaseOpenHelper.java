package xyz.robertsen.androidoblig.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import xyz.robertsen.androidoblig.HistoryActivity;
import xyz.robertsen.androidoblig.User;


public class CardDatabaseOpenHelper extends SQLiteOpenHelper {
    // Log tag
    private static final String TAG = CardDatabaseOpenHelper.class.getSimpleName();
    // Database information
    private static final String DATABASE_NAME = "card_organizer";
    private static final int DATABASE_VERSION = 3; // INCREMENT WHEN STRUCTURAL CHANGES

    SQLiteDatabase mWritableDatabase = null, mReadableDatabase = null;

    public CardDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBSchema.SQL_CREATE_USER);
        db.execSQL(DBSchema.SQL_CREATE_RECENT_SEARCHES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrades db from version " + oldVersion + " to " + newVersion + ", deletes all data.");
        db.execSQL(DBSchema.SQL_DROP_RECENT_SEARCHES);
        db.execSQL(DBSchema.SQL_DROP_USER);
        onCreate(db);
    }

    /**
     * Database schema for the card_organizer database
     * TABLES: UserTable, RecentSearchesTable, RecentSearchesTable
     */
    public static final class DBSchema {
        // USER_TABLE
        /////////////
        public abstract class UserTable implements BaseColumns {
            // Table name
            public static final String TABLE_NAME = "user";
            // Columns
            public static final String USER = "user";
        }

        // USER COLUMNS
        public static final String[] USER_COLUMNS = {
                UserTable.USER,
        };

        // SQL CREATE TABLE USER
        public static final String SQL_CREATE_USER =
                "CREATE TABLE " + UserTable.TABLE_NAME + " (" +
                        UserTable._ID + " INTEGER AUTO INCREMENT, " +
                        UserTable.USER + " TEXT PRIMARY KEY NOT NULL " +
                ");";
        // SQL DROP TABLE USER
        public static final String SQL_DROP_USER = "DROP TABLE IF EXISTS " + UserTable.TABLE_NAME + ";";


        // RECENT_SEARCHES_TABLE
        /////////////////////
        public abstract class RecentSearchesTable implements BaseColumns {
            // Table name
            public static final String TABLE_NAME = "recent_searches";
            // Columns
            public static final String USER = "user";   // FK -> UserTable(user)
            public static final String SEARCH_STRING = "name";
        }

        // RECENT_CARDS_COLUMNS
        public static final String[] RECENT_SEARCHES_COLUMNS = {
                RecentSearchesTable.USER,
                RecentSearchesTable.SEARCH_STRING
        };

        // SQL CREATE TABLE RECENT_CARDS
        public static final String SQL_CREATE_RECENT_SEARCHES =
                "CREATE TABLE " + RecentSearchesTable.TABLE_NAME + " (" +
                        RecentSearchesTable._ID + " INTEGER AUTO INCREMENT, " +
                        RecentSearchesTable.USER + " TEXT, " +
                        RecentSearchesTable.SEARCH_STRING + " TEXT, " +
                        "FOREIGN KEY (" + RecentSearchesTable.USER + ") REFERENCES " + UserTable.TABLE_NAME + "(" + UserTable.USER + ")" +
                        "PRIMARY KEY (" + RecentSearchesTable.USER + ", " + RecentSearchesTable._ID + ")" +
                        ");";
        // SQL DROP TABLE RECENT CARDS
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
    public boolean sqliteUserExists(String userName) {
        mReadableDatabase = getReadableDatabase();
        Cursor cursor = mReadableDatabase.query(
                DBSchema.UserTable.TABLE_NAME,
                DBSchema.USER_COLUMNS,
                DBSchema.RecentSearchesTable.USER + "='" + userName + "'",
                null, null, null, null
        );
        cursor.moveToFirst();
        Log.d(TAG, cursor.toString() + " number of rows in cursor" + cursor.getCount());
        return cursor.getCount()>0;
    }

    public boolean sqliteCreateUser(String userName) {
        Log.d(TAG, "sqliteCreateUser(String userName) wants to create user " + userName);
        mWritableDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.UserTable.USER, userName);
        long id;
        try {
            id = mWritableDatabase.insert(DBSchema.UserTable.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Log.d(TAG, "GOT ID " + id);
        return true;
    }

//    /**
//     * Seeds the user table with user data.
//     *
//     * @param users - Array of users to be created
//     */
//    public void seedUsers(User[] users) {
//        // Lazy initialization, OK for single-threaded usage
//        mWritableDatabase = getWritableDatabase();
//
//        ContentValues values;
//        for (User user : users) {
//            values = new ContentValues();
//            values.put(
//                    CardDatabaseOpenHelper.DBSchema.UserTable.USER,
//                    user.getUsr());
//            values.put(
//                    CardDatabaseOpenHelper.DBSchema.UserTable.PASSWORD,
//                    user.getPwd());
//            try {
//                user.setId(mWritableDatabase.insert(DBSchema.UserTable.TABLE_NAME, null, values));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        mWritableDatabase.close();
//    }
    /**
     * Adds a card to the Recentcards table.
     *
     * @param searchString
     * @param userName
     * */

    public long dbAddRecentSearch(String searchString, String userName) {
        mWritableDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.RecentSearchesTable.SEARCH_STRING, searchString);
        values.put(DBSchema.RecentSearchesTable.USER, userName);
        long id = mWritableDatabase.insert(DBSchema.RecentSearchesTable.TABLE_NAME, null, values);
        mWritableDatabase.close();
        return id;
    }

    /**
     * Fetches recent search data for a given user
     *
     * @param userName - The username that the query will be filtered by
     * @return Cursor that points at the first row of the resultset
     */
    public Cursor getRecentSearchesCursor(String userName) {
        mReadableDatabase = getReadableDatabase();
        Cursor cursor = mReadableDatabase.query(
                DBSchema.RecentSearchesTable.TABLE_NAME,
                DBSchema.RECENT_SEARCHES_COLUMNS,
                DBSchema.RecentSearchesTable.USER + "='" + userName + "'",
                null, null, null,
                DBSchema.RecentSearchesTable._ID + " DESC"
        );
        cursor.moveToFirst();
        mReadableDatabase.close();
        return cursor;
    }
}
