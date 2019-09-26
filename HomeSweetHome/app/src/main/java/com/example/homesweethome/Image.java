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

    public Image() {
        this.lowImageByte = null;
        this.mediumImageByte = null;
        this.highImageByte = null;
    }

    public byte[] getLowImageByte() { return lowImageByte; }
    public byte[] getMediumImageByte() { return mediumImageByte; }
    public byte[] getHighImageByte() { return highImageByte; }

    public void setLowImageByte(byte[] image) { this.lowImageByte = image; }
    public void setMediumImageByte(byte[] mediumImageByte) { this.mediumImageByte = mediumImageByte; }
    public void setHighImageByte(byte[] highImageByte) { this.highImageByte = highImageByte; }

    public String getLowImageString() { return lowImageString; }
    public String getMediumImageString() { return mediumImageString; }
    public String getHighImageString() { return highImageString; }

    public void setPosition(int position) { this.position = position; }
    public int getPosition() { return position; }
}
