package Classes.Entities;

/**
 * Created by SM David on 07/10/2018.
 */
public class ComboBoxItem<T> {
    private int id;
    private String name;
    private T obj;

    public ComboBoxItem(int id, String name, T obj) {
        this.id = id;
        this.name = name;
        this.obj = obj;
    }

    public ComboBoxItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ComboBoxItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return name;
    }
}
