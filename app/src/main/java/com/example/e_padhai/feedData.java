package com.example.e_padhai;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class feedData {

    private String text;
    private String time;
    private String date;
    private String author;

    private List<String> urlList;

public feedData(){}

    public feedData(String text, String time, String date, String author, List<String> urlList) {
        this.text = text;
        this.time = time;
        this.date = date;
        this.author = author;
        this.urlList = urlList;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("text", text);
        result.put("time", time);
        result.put("date", date);
        result.put("author", author);
        result.put("urlList", urlList);


        return result;
    }
}
