package Classes.DB;



/**
 * Created by SM David on 21/09/2018.
 */
public interface Config {
    void setAPiUrl(String api);
    void setBarReg(String types);
    void setImgTypes(String types);
    void setVideosTypes(String types);
    void setFileSearch(boolean searchInFile);
    void setParentCheck(boolean check);
    void setSaveNoMatch(boolean check);
    void setNoMatchFolder(String folderName);
    String getAPiUrl();
    String getBarReg();
    String getImgTypes();
    String getVideosTypes();
    boolean getParentCheck();
    boolean getFileSearch();
    boolean getSaveNoMatch();
    String getNoMatchFolder();
    String getReplacedCategory(String category);
    void setReplacedCategory(String category,String replaced);
    void saveReplacedCategory();

}
