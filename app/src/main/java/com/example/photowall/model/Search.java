package com.example.photowall.madel;

import java.util.ArrayList;

public class Search {

    private ArrayList<Image> results;

    public Search(ArrayList<Image> results) {
        this.results = results;
    }

    public ArrayList<Image> getResults() {
        return results;
    }

    public void setResults(ArrayList<Image> results) {
        this.results = results;
    }
}
