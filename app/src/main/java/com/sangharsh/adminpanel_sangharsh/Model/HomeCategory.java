package com.sangharsh.adminpanel_sangharsh.Model;

public class HomeCategory {
    private String id, name;
    private int subcat;

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

    public HomeCategory(String id, String name, int subcat) {
        this.id = id;
        this.name = name;
        this.subcat = subcat;
    }

    public HomeCategory(){}
}
