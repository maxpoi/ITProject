package com.example.homesweethome;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.util.Collections.sort;

public class UserCache{
    private static final UserCache ourInstance = new UserCache();
    private ArrayList<Cell> cells;

    static UserCache getInstance() {
        return ourInstance;
    }

    private UserCache() {}

    public void setCells(ArrayList<Cell> cells) { this.cells = cells; }

    public ArrayList<Cell> getCells() { return this.cells; }

    public ArrayList<Image> getAllImages() {
        ArrayList<Image> images = new ArrayList<>();

        for(Cell cell : cells) {
            images.addAll(cell.getImages());
        }
        return images;
    }

    public Cell getCell(int position) { return cells.get(position); }

    public ArrayList<Image> getImagesByCell(int position) { return getCell(position).getImages(); }

    public ArrayList<Cell> sortCellsByTime() {
        Collections.sort(getCells());
        return getCells();
    }



}
