package com.sangharsh.adminpanel_sangharsh.Model;

public class Banner {
    public Banner() {
    }

    private String id;
    private String imageUrl;

    public Banner(String id, String imageUrl, String redirectUrl, String category, String subcategory) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.redirectUrl = redirectUrl;
        this.category = category;
        this.subcategory = subcategory;
    }

    private String redirectUrl;
    private String category;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    private String subcategory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}