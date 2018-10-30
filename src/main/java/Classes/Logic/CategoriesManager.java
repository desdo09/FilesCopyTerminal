package Classes.Logic;

import Classes.Entities.Category;
import Classes.Entities.Product;

import java.util.*;

/**
 * Created by SM David on 30/09/2018.
 */
public class CategoriesManager {

    private Logic logic;

    private Map<Long,Category> categories;

    public CategoriesManager(Logic logic) {
        this.logic = logic;

    }

    public Map<Long,Category> getAllCategoriesMap()
    {
        if(categories != null && !categories.isEmpty())
            return categories;

        List<Product> products = logic.getProductsManager().getAllProducts();
        categories = new HashMap<>();



        if(products == null)
            return new HashMap<>();

        products.sort(Product.getComparator());

        for (Product p: products)
        {
            Category category = categories.get(p.getCategoryId());
            if( category == null)
            {
                categories.put(p.getCategoryId(),new Category(p.getCategoryId(),p.getCategory(),logic.getDb().getConfig().getReplacedCategory(p.getCategory()),1));
            }else{
                category.addToTotal(1);
            }
        }
        return categories;
    }

    public synchronized List<Category> getAllCategories()
    {


        Map<Long,Category> categories = getAllCategoriesMap();

        List<Category> ans = new ArrayList();

        for(Long id:categories.keySet())
            ans.add(categories.get(id));

        ans.sort(Category.getComparator());

        return ans;
    }


    public synchronized void refresh()
    {
        logic.getProductsManager().refresh();
        if(categories != null)
            categories.clear();
    }

    public String getReplacedCategory(String category){
        return logic.getDb().getConfig().getReplacedCategory(category);
    }
    public void setReplacedCategory(String category,String replaced){
        logic.getDb().getConfig().setReplacedCategory(category,replaced);
    }
    public void saveReplacedCategory()
    {
        logic.getDb().getConfig().saveReplacedCategory();
    }

}
