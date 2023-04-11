package com.example.nasa_images;

import java.util.Date;

public class NASAObject {
    private String title, explanation, url, hdUrl;
    private Date date;

    public NASAObject() {
    }

    public NASAObject(String title, String explanation, String url, String hdUrl, Date date) {
        this.title = title;
        this.explanation = explanation;
        this.url = url;
        this.hdUrl = hdUrl;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHdUrl() {
        return hdUrl;
    }

    public void setHdUrl(String hdUrl) {
        this.hdUrl = hdUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}