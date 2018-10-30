package Classes.Entities;

import java.util.Comparator;

/**
 * Created by SM David on 30/09/2018.
 */
public class Category{
    private long id;
    private String name;
    private String replaced;
    private int total;

    public Category() {
    }

    public Category(long id, String name, String replaced) {
        this.id = id;
        this.name = name;
        this.replaced = replaced;
    }

    public Category(long id, String name, String replaced, int total) {
        this.id = id;
        this.name = name;
        this.replaced = replaced;
        this.total = total;
    }

    public Category(Category other) {
        this.id = other.id;
        this.name = other.name;
        this.replaced = other.replaced;
        this.total = other.total;
    }

    public void copy(Category other) {
        this.id = other.id;
        this.name = other.name;
        this.replaced = other.replaced;
        this.total = other.total;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReplaced() {
        return replaced;
    }

    public void setReplaced(String replaced) {
        this.replaced = replaced;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void addToTotal(int toAdd)
    {
        total += toAdd;
    }

    public String getFinalCategory()
    {
        return (this.getReplaced() != null && !this.getReplaced().isEmpty() ? this.getReplaced() : this.getName());
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", replaced='" + replaced + '\'' +
                ", total=" + total +
                '}';
    }



    public static Comparator<Category> getComparator() {
        return new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return ((Category) o1).getName().compareTo(((Category)o2).getName());
            }
        };

    }
}
