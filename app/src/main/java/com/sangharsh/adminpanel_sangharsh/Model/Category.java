package com.sangharsh.adminpanel_sangharsh.Model;

import java.util.ArrayList;

public class Category {

    private String id, name;
    private int subcat;
    private ArrayList<SubCategory> subcategories;

    public ArrayList<SubCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<SubCategory> subcategories) {
        this.subcategories = subcategories;
    }

    public Category(String id, String name, int subcat, ArrayList<SubCategory> subcategories) {
        this.id = id;
        this.name = name;
        this.subcat = subcat;
        this.subcategories = subcategories;
    }

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

    public Category() {
    }
}
