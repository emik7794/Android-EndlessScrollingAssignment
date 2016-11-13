package ar.edu.unc.famaf.redditreader.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;

import ar.edu.unc.famaf.redditreader.backend.PreviewImageDBHelper;
import ar.edu.unc.famaf.redditreader.backend.RedditDBHelper;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class PostAdapter extends ArrayAdapter<PostModel> {

    private List<PostModel> postList = null;

    public PostAdapter(Context context, int textViewResourseId, List<PostModel> postList) {
        super(context, textViewResourseId);
        this.postList = postList;
    }


    protected class DownloadPreviewImageAsyncTask extends AsyncTask<URL, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... urls){
            URL url = urls[0];
            Bitmap bitmap = null;
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is,null,null);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public int getPosition(PostModel item) {
        return postList.indexOf(item);
    }

    @Override
    public PostModel getItem(int position) {
        return postList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.post_row, null);
        }
        if( convertView.getTag() == null) {
            TextView titleTV = (TextView) convertView.findViewById(R.id.titleTV);
            TextView authorTV = (TextView) convertView.findViewById(R.id.authorTV);
            TextView commentsTV = (TextView) convertView.findViewById(R.id.commentsTV);
            TextView dateTV = (TextView) convertView.findViewById(R.id.dateTV);
            ImageView asyncPreviewIV = (ImageView) convertView.findViewById(R.id.asyncPreviewIV);
            ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            viewHolder = new ViewHolder(titleTV, authorTV, commentsTV, dateTV, asyncPreviewIV, progressBar);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PostModel pm = postList.get(position);

        viewHolder.titleTV.setText(pm.getTitle());
        viewHolder.authorTV.setText(pm.getAuthor());
        viewHolder.commentsTV.setText(String.valueOf(pm.getComments()));
        viewHolder.dateTV.setText(pm.getDate());


        final String urlPreviewImage = pm.getUrlString();
        Bitmap bitmapPreviewImage = new PreviewImageDBHelper(getContext()).getImage(urlPreviewImage);

        if(bitmapPreviewImage == null) { // Si la imagen no esta en la db la descargo.

            URL url = null;
            try {
                url = new URL(pm.getUrlString());

                URL[] urlArray = new URL[1];
                urlArray[0] = url;
                new DownloadPreviewImageAsyncTask() {
                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);

                        viewHolder.progressBar.setVisibility(ProgressBar.GONE);
                        if (bitmap != null) {
                            viewHolder.asyncPreviewIV.setImageBitmap(bitmap);
                            new PreviewImageDBHelper(getContext()).setImage(bitmap, urlPreviewImage);
                        } else {
                            viewHolder.asyncPreviewIV.setImageResource(R.drawable.reddit_icon);
                        }

                    }
                }.execute(urlArray);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                viewHolder.asyncPreviewIV.setImageResource(R.drawable.reddit_icon);
            }

        } else { // Obtengo la imagen de la db
            viewHolder.progressBar.setVisibility(View.GONE);
            viewHolder.asyncPreviewIV.setImageBitmap(bitmapPreviewImage);
        }

        return convertView;
    }

    private class ViewHolder {
        public final TextView titleTV;
        public final TextView authorTV;
        public final TextView commentsTV;
        public final TextView dateTV;
        public final ImageView asyncPreviewIV;
        public final ProgressBar progressBar;

        public ViewHolder(TextView titleTV, TextView authorTV, TextView commentsTV,
                          TextView dateTV, ImageView asyncPreviewIV, ProgressBar progressBar) {

            this.titleTV = titleTV;
            this.authorTV = authorTV;
            this.commentsTV = commentsTV;
            this.dateTV = dateTV;
            this.asyncPreviewIV = asyncPreviewIV;
            this.progressBar = progressBar;
        }

    }

}
