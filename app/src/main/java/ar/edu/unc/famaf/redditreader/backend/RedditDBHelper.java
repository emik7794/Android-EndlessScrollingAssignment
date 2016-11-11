package ar.edu.unc.famaf.redditreader.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class RedditDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbreddit.db";
    public static final int VERSION = 1;
    public static final String POST_TABLE = "post";
    public static final String POST_TABLE_ID = "_id";
    public static final String POST_TABLE_TITLE = "title";
    public static final String POST_TABLE_AUTHOR = "author";
    public static final String POST_TABLE_DATE = "date";
    public static final String POST_TABLE_COMMENTS= "comments";
    public static final String POST_TABLE_URLSTRING = "urlString";
    public static final String POST_TABLE_BYTESPREVIEWIMAGE = "bytesPreviewImage";


    public RedditDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSentence = "create table "
                + POST_TABLE + " ( _id integer primary key autoincrement,"
                + POST_TABLE_TITLE + " text,"
                + POST_TABLE_AUTHOR + " text,"
                + POST_TABLE_DATE + " text,"
                + POST_TABLE_COMMENTS + " text,"
                + POST_TABLE_URLSTRING + " text,"
                + POST_TABLE_BYTESPREVIEWIMAGE + " blob"
                + " );";
        sqLiteDatabase.execSQL(createSentence);

        Log.d("DB", "Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d("DB", "Database Updated");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + POST_TABLE);
        this.onCreate(sqLiteDatabase);
    }
}
