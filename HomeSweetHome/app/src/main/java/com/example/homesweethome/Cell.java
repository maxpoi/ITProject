package com.example.homesweethome;

import java.util.ArrayList;

public class Cell implements Comparable<Cell>{

    private ArrayList<Image> images;
    private String video;
    private String audio;
    private int test_src;
    private String title;
    private String date;
    private String desc;

    public Cell() {
        this.title = null;
        this.date = null;
        this.desc = null;
        this.images = new ArrayList<>();
        this.video = null;
        this.audio = null;
    }

    public Cell(String title, String date, String desc,
                ArrayList<Image> images, String video, String audio) {
        this.title = title;
        this.date = date;
        this.desc = desc;
        this.images = images;
        this.video = video;
        this.audio = audio;
    }

    public void setPosition(int position) {
        for(Image image : this.images) {
            image.setPosition(position);
        }
    }

//    public int getPosition() { return this.images.indexOf(0); }

    public ArrayList<Image> getImages() { return this.images; }
    public void addImage(Image image) { this.images.add(image); }

    public String getVideo() { return video; }
    public void setVideo(String video) { this.video = video; }

    public String getAudio() { return audio; }
    public void setAudio(String audio) { this.audio = audio; }

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

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Cell(int test_src) {
        this.test_src = test_src;
    }
    public void setTest_src(int test_src) {
        this.test_src = test_src;
    }
    public int getTest_src() {
        return this.test_src;
    }

    @Override
    public int compareTo(Cell cell) {
        return this.date.compareTo(cell.date);
    }
}
