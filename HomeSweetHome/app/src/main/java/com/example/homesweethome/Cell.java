package com.example.homesweethome;

public class Cell {
    private String src;
    private int test_src;
    private String title;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Cell(int test_src) {
        this.test_src = test_src;
    }

    public int getTest_src() {
        return this.test_src;
    }

    public void setTest_src(int test_src) {
        this.test_src = test_src;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
