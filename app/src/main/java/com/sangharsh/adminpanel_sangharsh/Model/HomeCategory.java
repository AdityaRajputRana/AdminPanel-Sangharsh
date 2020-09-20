package com.sangharsh.adminpanel_sangharsh.Model;

public class HomeCategory {
    private String id, name;
    private int subcat;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private int price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubcat() {
        return subcat;
    }

    public void setSubcat(int subcat) {
        this.subcat = subcat;
    }

    public HomeCategory(String id, String name, int subcat, int price) {
        this.id = id;
        this.name = name;
        this.subcat = subcat;
        this.price = price;
    }

    public HomeCategory(){}
}
