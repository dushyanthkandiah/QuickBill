package Models;

/**
 * Created by Dushyanth on 2018-12-04.
 */

public class ClassProducts {
    private int id, pages;
    private String name, type;
    private Double litres, quantity, price;

    public ClassProducts(int id, int pages, String name, String type, Double litres, Double quantity, Double price) {
        this.id = id;
        this.pages = pages;
        this.name = name;
        this.type = type;
        this.litres = litres;
        this.quantity = quantity;
        this.price = price;
    }

    public ClassProducts() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLitres() {
        return litres;
    }

    public void setLitres(Double litres) {
        this.litres = litres;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
