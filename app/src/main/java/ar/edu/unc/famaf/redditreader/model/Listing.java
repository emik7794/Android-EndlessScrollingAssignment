package ar.edu.unc.famaf.redditreader.model;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class Listing {

    private String before;
    private String after;
    private List<PostModel> children = new ArrayList<>();

    @Nullable
    public String getBefore() {
        return before;
    }

    @Nullable
    public void setBefore(String before) {
        this.before = before;
    }

    @Nullable
    public String getAfter() {
        return after;
    }

    @Nullable
    public void setAfter(String after) {
        this.after = after;
    }

    @Nullable
    public List<PostModel> getChildren() {
        return children;
    }

    @Nullable
    public void setChildren(List<PostModel> children) {
        this.children = children;
    }

}

