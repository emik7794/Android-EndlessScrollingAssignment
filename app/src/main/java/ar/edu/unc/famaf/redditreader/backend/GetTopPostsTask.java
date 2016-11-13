package ar.edu.unc.famaf.redditreader.backend;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;


public class GetTopPostsTask extends AsyncTask<RedditDBHelper, Void, List<PostModel>> {

    @Override
    protected List<PostModel> doInBackground(RedditDBHelper... redditDBHelpers) {

        InputStream input;
        RedditDBHelper dbreddit = redditDBHelpers[0];

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL
                    ("https://www.reddit.com/top.json?limit=50").openConnection();
            conn.setRequestMethod("GET");
            input = conn.getInputStream();
            Parser parserJson = new Parser();
            Listing listing = parserJson.readJsonStream(input);
            if (listing != null) {
                SQLiteDatabase db = dbreddit.getWritableDatabase();
                dbreddit.onUpgrade(db, RedditDBHelper.VERSION, RedditDBHelper.VERSION);
                for (int i = 0; i < listing.getChildren().size(); i++) {
                    ContentValues values = new ContentValues();
                    values.put(dbreddit.POST_TABLE_TITLE, listing.getChildren().get(i).getTitle());
                    values.put(dbreddit.POST_TABLE_AUTHOR, listing.getChildren().get(i).getAuthor());
                    values.put(dbreddit.POST_TABLE_DATE, listing.getChildren().get(i).getDate());
                    values.put(dbreddit.POST_TABLE_COMMENTS, listing.getChildren().get(i).getComments());
                    values.put(dbreddit.POST_TABLE_URLSTRING, listing.getChildren().get(i).getUrlString());
                    long insertId = db.insert(dbreddit.POST_TABLE, null, values);

                }
                dbreddit.close();

                return listing.getChildren();

            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}



