package com.sangharsh.adminpanel_sangharsh.Model;

import java.util.ArrayList;

public class HomeDocument {

    private ArrayList<HomeCategory> courses;
    private ArrayList<Banner> banners;

    public HomeDocument(ArrayList<Banner> banners, ArrayList courses) {
        this.banners = banners;
        this.courses = courses;
    }

    public HomeDocument() {
    }


    public ArrayList<Banner> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<Banner> banners) {
        this.banners = banners;
    }

    public ArrayList<HomeCategory> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<HomeCategory> courses) {
        this.courses = courses;
    }
}