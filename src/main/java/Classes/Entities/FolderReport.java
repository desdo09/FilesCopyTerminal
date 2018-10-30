package Classes.Entities;

import java.util.Date;

/**
 * Created by SM David on 22/10/2018.
 */
public class FolderReport {
    private String name;
    private Boolean cover;
    private long total;
    private Date modified;

    public FolderReport() {
    }

    public FolderReport(String name, Boolean cover, long total, Date modified) {
        this.name = name;
        this.cover = cover;
        this.total = total;
        this.modified = modified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCover() {
        return cover;
    }

    public void setCover(Boolean cover) {
        this.cover = cover;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public static String[] getHeader(){
        return new String[]{"Barcodes","Vertical","Total","Modified"};
    }

    @Override
    public String toString() {
        return "FolderReport{" +
                "name='" + name + '\'' +
                ", cover=" + cover +
                ", total=" + total +
                ", modified=" + modified +
                '}';
    }

    public String toJson(){
        return name+":{" +
                "name='" + name + '\'' +
                ", cover=" + cover +
                ", total=" + total +
                ", modified=" + modified.getTime() +
                '}';
    }
}
