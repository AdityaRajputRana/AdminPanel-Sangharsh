package com.sangharsh.adminpanel_sangharsh.Model;

import java.util.ArrayList;

public class SubCategory {

    private String id;
    private String name;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;
    private int lectures;
    private ArrayList<Video> videos;

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public ArrayList<Topic> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<Topic> topics) {
        this.topics = topics;
    }

    private int top;
    private ArrayList<Topic> topics;

    public SubCategory(String id, String name, int lectures, ArrayList<Video> videos, String category, int top,
                       ArrayList<Topic> topics) {
        this.id = id;
        this.name = name;
        this.lectures = lectures;
        this.videos = videos;
        this.category = category;
        this.top = top;
        this.topics = topics;
    }

    public SubCategory() {
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

    public int getLectures() {
        return lectures;
    }

    public void setLectures(int lectures) {
        this.lectures = lectures;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }
}