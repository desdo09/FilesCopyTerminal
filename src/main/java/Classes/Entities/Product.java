package Classes.Entities;

import java.util.Comparator;

/**
 * Created by SM David on 21/09/2018.
 */
public class Product {
    private long id;
    private String name;
    private String barcode;
    private String category;
    private long categoryId;
    private Double price;
    private String SupplierCode;

    public Product() {
    }

    public Product(long id, String name, String barcode, String category, long categoryId, Double price, String supplierCode) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.category = category;
        this.categoryId = categoryId;
        this.price = price;
        SupplierCode = supplierCode;
    }

    public Product(Product other) {
        this.id = other.id;
        this.name = other.name;
        this.barcode = other.barcode;
        this.category = other.category;
        this.categoryId = other.categoryId;
        this.price = other.price;
        this.SupplierCode = other.SupplierCode;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSupplierCode() {
        return SupplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        SupplierCode = supplierCode;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", barcode='" + barcode + '\'' +
                ", category='" + category + '\'' +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", SupplierCode='" + SupplierCode + '\'' +
                '}';
    }

    public static Comparator<Product> getComparator() {
        return new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return (int) ((o2).getId() - ((Product) o1).getId());
            }
        };

    }
}
