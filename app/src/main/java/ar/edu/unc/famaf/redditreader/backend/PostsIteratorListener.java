package ar.edu.unc.famaf.redditreader.backend;


import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

public interface PostsIteratorListener {
    void nextPosts(List<PostModel> posts);
}
