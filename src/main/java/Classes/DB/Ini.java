package Classes.DB;

import java.io.*;
import java.util.Properties;

/**
 * Created by SM David on 28/09/2018.
 */
public class Ini implements Config {

    private Properties configProperties = new Properties();
    private Properties categoryProperties = new Properties();
    private File config = new File("config.ini");
    private File category = new File("categories.ini");

    public Ini() {
        try {

            if (!config.exists()) {
                if (!config.createNewFile())
                    throw new Exception("File creation failed");
            }
            if (!category.exists()) {
                if (!category.createNewFile())
                    throw new Exception("File creation failed");
            }
            configProperties.load(new FileInputStream(config));
            if(configProperties.isEmpty())
            {
                setBarReg("^(\\d{10})");
                setImgTypes("jpg;png;bmp;cr2;tiff");
                setVideosTypes("avi;mov;mp4;wmv;");
            }
            categoryProperties.load(new FileInputStream(category));
            System.out.println("Config ini file = " + config.getAbsolutePath());
            System.out.println("Categories ini file = " + category.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAPiUrl() {

        return (configProperties.getProperty("api") == null || configProperties.getProperty("api").isEmpty()?"http://api.4jz.eu/api/Presta/GetProductDetail":configProperties.getProperty("api"));
    }

    public void setAPiUrl(String api) {
        try {
            configProperties.setProperty("api", api);
            configProperties.store(new FileOutputStream(config), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setBarReg(String types) {
        try {
            configProperties.setProperty("bar_reg", types);
            configProperties.store(new FileOutputStream(config), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImgTypes(String types) {
        try {
            configProperties.setProperty("img_types", types);
            configProperties.store(new FileOutputStream(config), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setVideosTypes(String types) {
        try {
            configProperties.setProperty("vid_types", types);
            configProperties.store(new FileOutputStream(config), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFileSearch(boolean searchInFile){
        try {
            configProperties.setProperty("search_file", ((searchInFile)?"1":"0"));
            configProperties.store(new FileOutputStream(config), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setParentCheck(boolean check)
    {
        try {
            configProperties.setProperty("folder_parent", ((check)?"1":"0"));
            configProperties.store(new FileOutputStream(config), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSaveNoMatch(boolean check) {
        try {
            configProperties.setProperty("save_noMach", ((check)?"1":"0"));
            configProperties.store(new FileOutputStream(config), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setNoMatchFolder(String folderName) {
        try {
            configProperties.setProperty("noMatch_folder", folderName);
            configProperties.store(new FileOutputStream(config), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBarReg() {
        return configProperties.getProperty("bar_reg");
    }

    public String getImgTypes(){
        return configProperties.getProperty("img_types");
    }

    public String getVideosTypes(){
        return configProperties.getProperty("vid_types");
    }

    public boolean getParentCheck(){
        return configProperties.getProperty("folder_parent") != null && configProperties.getProperty("folder_parent").equals("1");
    }

    public boolean getFileSearch(){
        return configProperties.getProperty("search_file") != null && configProperties.getProperty("search_file").equals("1");
    }

    @Override
    public boolean getSaveNoMatch() {
        return configProperties.getProperty("save_noMach") != null && configProperties.getProperty("save_noMach").equals("1");
    }

    @Override
    public String getNoMatchFolder() {
        return configProperties.getProperty("noMatch_folder") != null ? configProperties.getProperty("noMatch_folder") : "noBarcode";
    }

    public String getReplacedCategory(String category) {
        return categoryProperties.getProperty(category);
    }

    public void setReplacedCategory(String category, String replaced) {
        String prev = getReplacedCategory(category);
        if ((prev == null || prev.isEmpty()) && (replaced == null || replaced.isEmpty()))
            return;

        categoryProperties.setProperty(category, replaced);

    }

    public void saveReplacedCategory() {
        try {
            categoryProperties.store(new FileOutputStream(category), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
