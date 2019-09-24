package com.example.homesweethome;

public class Image {
    private String lowImageString;
    private String mediumImageString;
    private String highImageString;
    private byte[] lowImageByte;
    private byte[] mediumImageByte;
    private byte[] highImageByte;
    private int position;

    public Image(String lowImageString, String mediumImageString, String highImageString) {
        this.lowImageString = lowImageString;
        this.mediumImageString = mediumImageString;
        this.highImageString = highImageString;
    }

    public Image(byte[] lowImageByte, byte[] mediumImageByte, byte[] highImageByte) {
        this.lowImageByte = lowImageByte;
        this.mediumImageByte = mediumImageByte;
        this.highImageByte = highImageByte;
    }

    public byte[] getLowImageByte() { return lowImageByte; }

    public byte[] getMediumImageByte() { return mediumImageByte; }

    public byte[] getHighImageByte() { return highImageByte; }

    public void setPosition(int position) { this.position = position; }

    public String getLowImageString() { return lowImageString; }

    public String getMediumImageString() { return mediumImageString; }

    public String getHighImageString() { return highImageString; }

    public int getPosition() { return position; }
}
