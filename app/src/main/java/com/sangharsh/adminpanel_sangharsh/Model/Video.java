package com.sangharsh.adminpanel_sangharsh.Model;

public class Video {
    private String id, name, description, category, subcat;
    private boolean sample;
    private int time;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcat() {
        return subcat;
    }

    public void setSubcat(String subcat) {
        this.subcat = subcat;
    }

    public Video(String id, String name, String description, boolean sample, int time, String category, String subcat) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sample = sample;
        this.time = time;
        this.category = category;
        this.subcat = subcat;
    }

    public Video() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSample() {
        return sample;
    }

    public void setSample(boolean sample) {
        this.sample = sample;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}