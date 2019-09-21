package com.example.homesweethome;

public class Cell {

    private String img;
    private String video;
    private String audio;
    private int test_src;
    private String title;
    private String date;
    private String desc;

    public Cell(String title, String date, String desc, String img, String video, String audio) {
        this.title = title;
        this.date = date;
        this.desc = desc;
        this.img = img;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

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
