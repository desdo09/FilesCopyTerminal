package Classes.DB;

import Classes.Entities.Product;

import java.util.List;

/**
 * Created by SM David on 21/09/2018.
 */
public class DB {
    private final Api api = new Api(null);
    private final Config local = new Ini();


    public DB() {
    }

    public DB(String apiUrl) {
        api.setUrl(apiUrl);
    }

    public List<Product> getProducts (){
        return api.getProducts();
    }

    public Config getConfig()
    {
        return local;
    }

    public void setApiUrl(String url)
    {
        api.setUrl(url);
    }
}
