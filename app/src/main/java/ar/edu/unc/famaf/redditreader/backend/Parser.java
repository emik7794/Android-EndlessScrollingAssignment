package ar.edu.unc.famaf.redditreader.backend;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.util.JsonToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;

import java.util.TimeZone;


public class Parser {

    public Listing readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        try {
            return readDataObject(reader);
        } finally {
            reader.close();
        }
    }

    public Listing readDataObject(JsonReader reader) throws IOException{
        Listing listing = new Listing();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals("data")) {
                listing = readData(reader);
            }else{
                reader.skipValue();
            }
        }
        reader.endObject();
        return listing;
    }


    public Listing readData(JsonReader reader) throws IOException{
        String after = null;
        String before = null;
        List<PostModel> children = new ArrayList<>();
        Listing listing = new Listing();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("children") && reader.peek() != JsonToken.NULL) {
                reader.beginArray();
                while (reader.hasNext()) {
                    children.add(readChildrenArray(reader));
                }
                reader.endArray();
            } else if (name.equals("after") && reader.peek() != JsonToken.NULL) {
                after = reader.nextString();
            } else if (name.equals("after") && reader.peek() != JsonToken.NULL) {
                before = reader.nextString();
            } else {
                reader.skipValue();
            }
        }

        listing.setChildren(children);
        listing.setAfter(after);
        listing.setBefore(before);

        return listing;
    }


    public PostModel readChildrenArray(JsonReader reader) throws IOException{
        PostModel postModel = new PostModel();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals("data")) {
                reader.beginObject();
                postModel = readDataPost(reader);
                reader.endObject();
            }else{
                reader.skipValue();
            }
        }
        reader.endObject();

        return  postModel;
    }

    public  PostModel readDataPost (JsonReader reader) throws  IOException {

        PostModel postModel = new PostModel();
        String title = null;
        String author = null;
        long num_comments = -1;
        String thumbnail = null;
        long date_ms = -1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String date = null;


        while (reader.hasNext()) {
            String name = reader.nextName();
            JsonToken jsonToken = reader.peek();

            if (name.equals("title") && jsonToken != JsonToken.NULL) {
                title = reader.nextString();
            } else if(name.equals("author") && jsonToken != JsonToken.NULL){
                author = reader.nextString();
            } else if (name.equals("num_comments")&& jsonToken != JsonToken.NULL){
                num_comments = reader.nextLong();
            } else  if (name.equals("thumbnail") && jsonToken != JsonToken.NULL) {
                thumbnail = reader.nextString();
            } else if (name.equals("created") && jsonToken != JsonToken.NULL){
                date_ms = reader.nextLong();
                dateFormat.getTimeZone().getOffset(date_ms);
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                date = dateFormat.format(date_ms);
            }
            else {
                reader.skipValue();
            }
        }

        postModel.setTitle(title);
        postModel.setAuthor(author);
        postModel.setComments(num_comments);
        postModel.setUrlString(thumbnail);
        postModel.setDate(date);

        return  postModel;
    }
}


