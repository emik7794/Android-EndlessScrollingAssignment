package ar.edu.unc.famaf.redditreader.backend;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class PreviewImageDBHelper {

    private RedditDBHelper dbHelper;
    private SQLiteDatabase readableDataBase;
    private SQLiteDatabase writableDataBase;

    public PreviewImageDBHelper(Context context) {
        this.dbHelper = new RedditDBHelper(context);
        this.readableDataBase = dbHelper.getReadableDatabase();
        this.writableDataBase = dbHelper.getWritableDatabase();
    }

    public Bitmap getImage(String urlPreviewImage) {

        Bitmap bitmap = null;
        Cursor cursor = readableDataBase.rawQuery("SELECT * FROM "
                + RedditDBHelper.POST_TABLE
                + " WHERE " + RedditDBHelper.POST_TABLE_URLSTRING
                + " = '" + urlPreviewImage + "'"
                + ";", null);

        if (cursor.moveToFirst() &&
                !cursor.isNull(cursor.getColumnIndex(RedditDBHelper.
                        POST_TABLE_BYTESPREVIEWIMAGE))) {

            byte[] image = cursor.getBlob(cursor.
                    getColumnIndex(RedditDBHelper.POST_TABLE_BYTESPREVIEWIMAGE));

            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        }
        cursor.close();
        dbHelper.close();
        return bitmap;
    }

    public void setImage(Bitmap bitmap, String urlPreviewImage) {

        ContentValues values = new ContentValues();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] image = stream.toByteArray();

        values.put(RedditDBHelper.POST_TABLE_BYTESPREVIEWIMAGE , image);
        writableDataBase.update(RedditDBHelper.POST_TABLE ,values,
                RedditDBHelper.POST_TABLE_URLSTRING + " = '" + urlPreviewImage + "'", null);
    }

}
