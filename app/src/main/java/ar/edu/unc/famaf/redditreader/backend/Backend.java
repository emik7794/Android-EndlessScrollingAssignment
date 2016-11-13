package ar.edu.unc.famaf.redditreader.backend;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;


public class Backend {
    private static Backend ourInstance = new Backend();

    public static Backend getInstance() {
        return ourInstance;
    }

    private Backend() {
    }

    private int offset = 0; // lo utilizo para la consulta a la db como argumento del LIMIT.

    public void getNextPosts(final PostsIteratorListener listener, final Context context, boolean savePosts) {

        RedditDBHelper dbReddit = new RedditDBHelper(context);

        // savePosts lo uso como flag para determinar que traiga los post y los persista una sola vez
        if (isNetworkAvailable(context) && savePosts) {
            RedditDBHelper[] dbRedditArray = new RedditDBHelper[1];
            dbRedditArray[0] = dbReddit;
            new GetTopPostsTask() {
                @Override
                protected void onPostExecute(List<PostModel> postModels) {
                    super.onPostExecute(postModels);
                }
            }.execute(dbRedditArray);
        }


        SQLiteDatabase db = dbReddit.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + dbReddit.POST_TABLE
                + " LIMIT " + String.valueOf(offset) + "," + "5", null);

        offset = offset + 5;

        List<PostModel> postModelList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                PostModel postModel = new PostModel();
                postModel.setTitle(cursor.getString(1));
                postModel.setAuthor(cursor.getString(2));
                postModel.setDate(cursor.getString(3));
                postModel.setComments(cursor.getLong(4));
                postModel.setUrlString(cursor.getString(5));
                postModelList.add(postModel);

            } while (cursor.moveToNext());
        }
        cursor.close();
        dbReddit.close();

        listener.nextPosts(postModelList);

    }


    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
