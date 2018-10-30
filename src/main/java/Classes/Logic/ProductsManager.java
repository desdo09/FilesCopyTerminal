package Classes.Logic;

import Classes.DB.DB;
import Classes.Entities.Product;

import java.util.List;

/**
 * Created by SM David on 30/09/2018.
 */
public class ProductsManager {

    private Logic logic;
    private DB db;
    private List<Product> products;

    public ProductsManager(Logic logic) {
        this.logic = logic;
        db = this.logic.getDb();
    }

    public List<Product> getAllProducts()
    {
        if(products == null || products.isEmpty())
        {
            db.setApiUrl(logic.getConfig().getAPiUrl());
            products = db.getProducts();
        }

        return products;
    }

    public void refresh()
    {
        if(products != null)
            products.clear();
    }


}
