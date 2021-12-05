package com.example.photowall.madel;

public class Image {

    private Url urls;

    public Image(Url urls) {
        this.urls = urls;
    }

    public Url getUrls() {
        return urls;
    }

    public void setUrls(Url urls) {
        this.urls = urls;
    }
}
