package com.example.homesweethome;

import android.content.Context;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class UserCache {
    private static final UserCache ourInstance = new UserCache();
    private ArrayList<Cell> cells;

    static UserCache getInstance() { return ourInstance; }
    private UserCache() { this.cells = new ArrayList<>(); }

    public ArrayList<Cell> getCells() { return this.cells; }
    public void setCells(ArrayList<Cell> cells) { this.cells = cells; }

    public Cell getCell(int position) { return cells.get(position); }
    public void addCell(Cell cell) { this.cells.add(cell); }

    public ArrayList<Image> getImagesByCell(int position) { return getCell(position).getImages(); }
    public ArrayList<Image> getAllImages() {
        ArrayList<Image> images = new ArrayList<>();

        for(Cell cell : cells) {
            images.addAll(cell.getImages());
        }
        return images;
    }

}
