package com.example.homesweethome;

public class Image {
    private String lowImage;
    private String mediumImage;
    private String highImage;
    private int position;

    public Image(String lowImage, String mediumImage, String highImage) {
        this.lowImage = lowImage;
        this.mediumImage = mediumImage;
        this.highImage = highImage;
    }

    public void setPosition(int position) { this.position = position; }

    public String getLowImage() { return lowImage; }

    public String getMediumImage() { return mediumImage; }

    public String getHighImage() { return highImage; }

    public int getPosition() { return position; }
}
