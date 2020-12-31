package com.example.myapp;

public class Video {

    private String title;
    private String descriptionn;
    private String url;
    private String thumbnail;

    public Video(String title, String descriptionn, String videoUrl, String thumbnail) {
        this.title = title;
        this.descriptionn = descriptionn;
        this.url = videoUrl;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getDescriptionn() {
        return descriptionn;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

}
