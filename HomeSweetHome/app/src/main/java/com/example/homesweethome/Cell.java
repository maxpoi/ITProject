package com.example.homesweethome;

import java.util.ArrayList;

public class Cell {

    private ArrayList<String> imgs;
    private String video;
    private String audio;
    private int test_src;
    private String title;
    private String date;
    private String desc;

    public Cell(String title, String date, String desc, ArrayList<String> imgs, String video, String audio) {
        this.title = title;
        this.date = date;
        this.desc = desc;
        this.imgs = imgs;
        this.video = video;
        this.audio = audio;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTest_src(int test_src) {
        this.test_src = test_src;
    }

    public ArrayList<String> getImgs() { return imgs; }
 
    public void setImgs(ArrayList<String> img) { this.imgs = imgs; }

    public void addImg(String img) { this.imgs.add(img); }

    public String getImg(int position) { return this.imgs.get(position); }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Cell(int test_src) {
        this.test_src = test_src;
    }

    public int getTest_src() {
        return this.test_src;
    }
}
