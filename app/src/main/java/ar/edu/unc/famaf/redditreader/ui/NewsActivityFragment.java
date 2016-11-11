package ar.edu.unc.famaf.redditreader.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.backend.GetTopPostsTask;
import ar.edu.unc.famaf.redditreader.backend.RedditDBHelper;
import ar.edu.unc.famaf.redditreader.model.PostModel;


/**
 * A placeholder fragment containing a simple view.
 */
public class NewsActivityFragment extends Fragment {

    public NewsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        final ListView postsLV = (ListView) view.findViewById(R.id.postsLV);

        RedditDBHelper dbReddit = new RedditDBHelper(getContext());;
        SQLiteDatabase db = dbReddit.getWritableDatabase();

        if (isNetworkAvailable()) {
            RedditDBHelper[] dbRedditArray = new RedditDBHelper[1];
            dbRedditArray[0] = dbReddit;
            new GetTopPostsTask() {
                @Override
                protected void onPostExecute(List<PostModel> postModels) {
                    super.onPostExecute(postModels);
                    PostAdapter postAdapter = new PostAdapter(getContext(), R.layout.post_row, postModels);
                    postsLV.setAdapter(postAdapter);
                }
            }.execute(dbRedditArray);

        } else {
            // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            // alertDialogBuilder.setMessage("Fail internet connection");

            List<PostModel> postModelList = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT * FROM " + dbReddit.POST_TABLE, null);

            if (cursor.moveToFirst()) {
                do {
                    PostModel postModel = new PostModel();
                    postModel.setTitle(cursor.getString(1));
                    postModel.setAuthor(cursor.getString(2));
                    postModel.setDate(cursor.getString(3));
                    postModel.setComments(cursor.getLong(4));
                    postModel.setUrlString(cursor.getString(5));
                    Bitmap bitmap = getImage(cursor.getBlob(6));
                    postModelList.add(postModel);

                } while (cursor.moveToNext());
            }
            PostAdapter postAdapter = new PostAdapter(getContext(), R.layout.post_row, postModelList);
            postsLV.setAdapter(postAdapter);
        }


        return view;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Bitmap getImage(byte[] image)
    {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}

