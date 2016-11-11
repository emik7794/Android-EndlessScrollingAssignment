package ar.edu.unc.famaf.redditreader.backend;

import java.net.MalformedURLException;
import java.net.URL;
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

    public List<PostModel> getTopPosts() {
        List<PostModel> postModelsList = new ArrayList<>();

        PostModel postModel1 = new PostModel();
        postModel1.setTitle("Este es el titulo numero 1");
        postModel1.setAuthor("r/author1");
        postModel1.setDate("Hace 1 hora");
        postModel1.setComments(10);
        postModel1.setUrlString("https://yt3.ggpht.com/-v0soe-ievYE/AAAAAAAAAAI/AAAAAAAAAAA/OixOH_h84Po/s900-c-k-no-mo-rj-c0xffffff/photo.jpg");

        PostModel postModel2 = new PostModel();
        postModel2.setTitle("Este es el titulo numero 2");
        postModel2.setAuthor("r/author2");
        postModel2.setDate("Hace 2 horas");
        postModel2.setComments(20);
        postModel2.setUrlString("http://www.hotel-r.net/im/hotel/fr/casa-2.png");

        PostModel postModel3 = new PostModel();
        postModel3.setTitle("Este es el titulo numero 3");
        postModel3.setAuthor("r/author3");
        postModel3.setDate("Hace 3 horas");
        postModel3.setComments(30);
        postModel3.setUrlString("http://www.mundoperro.net/wp-content/uploads/consejos-perro-feliz-verano-400x300.jpg");

        PostModel postModel4 = new PostModel();
        postModel4.setTitle("Este es el titulo numero 4");
        postModel4.setAuthor("r/author4");
        postModel4.setDate("Hace 4 horas");
        postModel4.setComments(40);
        postModel4.setUrlString("http://fm.cnbc.com/applications/cnbc.com/resources/img/editorial/2016/03/01/103432838-03_CHIRON_34-front_WEB.530x298.jpg?v=1456848597");

        PostModel postModel5 = new PostModel();
        postModel5.setTitle("Este es el titulo numero 5");
        postModel5.setAuthor("r/author5");
        postModel5.setDate("Hace 5 horas");
        postModel5.setComments(50);
        postModel5.setUrlString("http://misanimales.com/wp-content/uploads/2015/02/gato-2.jpg");

        postModelsList.add(postModel1);
        postModelsList.add(postModel2);
        postModelsList.add(postModel3);
        postModelsList.add(postModel4);
        postModelsList.add(postModel5);

        return postModelsList;
    }
}
