package com.example.homesweethome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<Image> images;
    private Context context;
    private String class_name;      // this is used to check where the adapter is used, so that it can jump to next activity accordingly
    private int viewID;
    private int layoutID;
    private int imageResolution;

    public ImageAdapter(Context context, String class_name, ArrayList<Image> images, int viewID, int layoutID) {
        this.context = context;
        this.images = images;
        this.viewID = viewID;
        this.layoutID = layoutID;
        this.class_name = class_name;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutID, parent, false);
        return new ImageAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setImage(holder.img, position);
        // to do
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (class_name.equals(SingleArtifactPage.class.getName())
                        || class_name.equals(AddPage.class.getName()))
                {
                    intent = new Intent(context, SingleImagePage.class);
                    intent.putExtra("cell", images.get(position).getPosition());
                    intent.putExtra("image", position);
                } else {
                    intent = new Intent(context, SingleArtifactPage.class);
                    intent.putExtra("position", images.get(position).getPosition());
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public ViewHolder(View view) {
            super (view);
            img = (ImageView) view.findViewById(viewID);
        }
    }


    private void setImage(ImageView img, int position) {
//        String image;
        byte[] image;
        if (img != null) {
            switch (this.imageResolution) {
                case -1:
//                    image = this.images.get(position).getMediumImageString();
                    image = this.images.get(position).getMediumImageByte();
                    break;
                case 0:
//                    image = this.images.get(position).getMediumImageString();
                    image = this.images.get(position).getMediumImageByte();
                    break;
                case 1:
//                    image = this.images.get(position).getMediumImageString();
                    image = this.images.get(position).getMediumImageByte();
                    break;
                default:
                    // should never happen
                    image = null;
                    break;
            }

            Bitmap bitmap = ImageProcessor.getInstance().restoreImage(image);
            Glide.with(this.context).asBitmap().load(bitmap).into(img);
        }
    }

    public void setImages(ArrayList<Image> images) { this.images = images; }

    // resolution: -1 -> low; 0 -> medium; 1 -> high
    public void setImageResolution(int resolution) { this.imageResolution = resolution; }
}
