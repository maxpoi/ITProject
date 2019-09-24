package com.example.homesweethome;

import android.content.Context;

import java.util.ArrayList;

public class CustomTest{

    private int[] testData = new int[] {R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, R.drawable.img_4, R.drawable.img_5};

    private Context context;
    public CustomTest(Context context) { this.context = context; }

    public Cell createCell() {
        ImageProcessor imageProcessor = ImageProcessor.getInstance();

        ArrayList<Image> images = new ArrayList<>();
        for (int i : testData) {
//            images.add(new Image(imageProcessor.createLowImg(context, i), imageProcessor.encodeMediumImage(context, i), imageProcessor.encodeLargeImage(context, i)));
//            images.add(new Image(null, imageProcessor.encodeMediumImage(context, i), null));
            images.add(new Image(null, imageProcessor.encodeMediumImageByte(context, i), null));
        }

        return new Cell(null, null, null, images, null, null);
    }

    public ArrayList<Cell> createCells() {
        int i=0;
        ArrayList<Cell> cells = new ArrayList<>();

        while(i<4) {
            Cell cell = createCell();
            cell.setPosition(i);
            cell.setDesc("This is a test description. Let's see how long it can be. " +
                    "Let's see how long it can be.Let's see how long it can be.Let's see how long it can be." +
                    "Let's see how long it can be.Let's see how long it can be.Let's see how long it can be." +
                    "Let's see how long it can be.Let's see how long it can be.Let's see how long it can be." +
                    "Let's see how long it can be.Let's see how long it can be.Let's see how long it can be." +
                    "Let's see how long it can be.Let's see how long it can be.");
            cell.setTitle("Homemade Coffee");
            cells.add(cell);
            i++;
        }

        return cells;
    }
}
