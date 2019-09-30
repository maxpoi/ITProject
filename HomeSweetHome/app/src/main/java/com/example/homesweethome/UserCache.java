package com.example.homesweethome;

import android.content.Context;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import static java.util.Collections.sort;


public class UserCache {
    private static final UserCache ourInstance = new UserCache();
    private static ArrayList<Cell> cells;

    static UserCache getInstance() { return ourInstance; }

    private UserCache() {}

    public void setCells(ArrayList<Cell> cells) { this.cells = cells; }

    public void initialize() { this.cells = new ArrayList<>(); }

    public static ArrayList<Cell> getCells() { return cells; }

    public ArrayList<Image> getAllImages() {
        ArrayList<Image> images = new ArrayList<>();

        for(Cell cell : cells) {
            images.addAll(cell.getImages());
        }
        return images;
    }

    public static Cell getCell(int position) { return cells.get(position); }

    public ArrayList<Image> getImagesByCell(int position) { return getCell(position).getImages(); }

    public static ArrayList<Cell> sortCellsByTime() {
        if (getCells() == null){
            return null;
        }

        Collections.sort(getCells());
        return getCells();
    }

    public static ArrayList<String> getSortedDate(){
        ArrayList<String> years = new ArrayList<>();

        sortCellsByTime();
        if (cells == null){
            return null;
        }
        for(Cell cell : cells){
            years.add(cell.getDate().substring(0,4));
        }
        return years;
    }


}
