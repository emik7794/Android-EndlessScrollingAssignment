package ar.edu.unc.famaf.redditreader.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.backend.Backend;
import ar.edu.unc.famaf.redditreader.backend.EndlessScrollListener;
import ar.edu.unc.famaf.redditreader.backend.PostsIteratorListener;
import ar.edu.unc.famaf.redditreader.model.PostModel;


/**
 * A placeholder fragment containing a simple view.
 */
public class NewsActivityFragment extends Fragment {

    public NewsActivityFragment() {
    }

    List<PostModel> postModelList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_news, container, false);
        ListView postsLV = (ListView) view.findViewById(R.id.postsLV);

        final PostAdapter adapter = new PostAdapter(getContext(), R.layout.post_row, postModelList);
        postsLV.setAdapter(adapter);

        final PostsIteratorListener listener = new PostsIteratorListener() {

            @Override
            public void nextPosts(List<PostModel> posts) {
                postModelList.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        };

        Backend.getInstance().getNextPosts(listener, getContext(), true);

        // Attach the listener to the AdapterView onCreate
        postsLV.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                Backend.getInstance().getNextPosts(listener, getContext(), false);
                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }

        });

        return view;
    }

}

