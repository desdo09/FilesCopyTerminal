package Classes.Logic;

import Classes.DB.Config;
import Classes.DB.DB;
import Classes.Entities.ComboBoxItem;
import Classes.Entities.Interfaces.ThreeConsumer;
import Classes.Entities.Product;
import Classes.FolderManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by SM David on 21/09/2018.
 */
public class Logic {

    private static Logic instance;

    public static Logic getInstance() {
        if (instance == null)
            instance = new Logic();
        return instance;
    }

    private Logic() {

    }


    private Product products;

    private DB db = new DB();

    private FolderManager fm =new FolderManager();

    private ProductsManager pm = new ProductsManager(this);

    private CategoriesManager cm = new CategoriesManager(this);


    protected DB getDb() {
        return db;
    }

    public Config getConfig(){
        return db.getConfig();
    }

    public List<Path> getFiles(String directory, boolean recursive) throws IOException {
        return fm.getFiles(directory,recursive);
    }
    public List<Path> getFiles(String directory, boolean recursive, String regex) throws IOException {
        return fm.getFiles(directory,recursive,regex);
    }

    public List<Path> getFolders(String directory, boolean recursive, String regex) throws IOException {
        return fm.getFolders(directory,recursive,regex);
    }

    public void copyFiles(List<Path> paths, String destPath, boolean replace) throws IOException {
        fm.copyFiles(paths, destPath, null,replace);
    }

    public void copyFiles(List<Path> paths, String destPath, String regex,boolean replace) throws IOException {


        fm.copyFiles(paths, destPath, regex,replace);
    }

    public void copyFiles(List<Path> paths,String dest, Map<String, String> destPathMap, String regex, boolean replace) throws IOException {

        fm.copyFiles(paths, dest,destPathMap, regex,replace);
    }

    public void setOperationListener(ThreeConsumer<Path,Path,String> operationListener) {
       fm.setOperationListener(operationListener);
    }

    public void setOperationProgress(Consumer<Double> operationProgress) {
        fm.setOperationProgress(operationProgress);
    }

    public void setOperationFile(Consumer<String> operationFile) {
        fm.setOperationFile(operationFile);
    }

    public ProductsManager getProductsManager() {
        return pm;
    }

    public CategoriesManager getCategoriesManager() {
        return cm;
    }

    public List<ComboBoxItem<String>> getFilesType()
    {
        List<ComboBoxItem<String>> options = new ArrayList<>();
        options.add(new ComboBoxItem<>(1,"All"));
        options.add(new ComboBoxItem<>(2,"BarCode"));
        options.add(new ComboBoxItem<>(3,"BarCode Image file"));
        options.add(new ComboBoxItem<>(4,"BarCode Video file"));
        options.add(new ComboBoxItem<>(5,"Image files"));
        options.add(new ComboBoxItem<>(6,"Video files"));
        return options;
    }
}
