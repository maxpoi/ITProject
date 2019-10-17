package com.example.homesweethome.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;
import com.example.homesweethome.R;
import com.example.homesweethome.UI.SingleImagePage;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private ImageButton deleteButton;

        public ViewHolder(View view) {
            super (view);
            img = (ImageView) view.findViewById(R.id.recycleview_image);
            deleteButton = (ImageButton) view.findViewById(R.id.delete_image_button);
            if (enableDeletion)
                deleteButton.setVisibility(View.VISIBLE);
        }
    }

    private List<Image> images;
    private Context context;
    private boolean enableDeletion;

    public ImageAdapter(Context context, boolean enableDeletion) {
        this.context = context;
        this.enableDeletion = enableDeletion;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycleview_image, parent, false);
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        final Image image = images.get(position);
        if (image.getLowImageBitmap() == null) {
            Bitmap high = ((HomeSweetHome) context).getImageProcessor().decodeFileToHighBitmap(image.getHighResImagePath());
            Bitmap medium = ((HomeSweetHome) context).getImageProcessor().decodeFileToMediumBitmap(image.getMediumResImagePath());
            Bitmap low = ((HomeSweetHome) context).getImageProcessor().decodeFileToLowBitmap(image.getLowResImagePath());

            image.setHighImageBitmap(high);
            image.setMediumImageBitmap(medium);
            image.setLowImageBitmap(low);
        }

        setImage(holder.img, position);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(context, SingleImagePage.class);

                String path = image.getOriginalPath() == null ? image.getHighResImagePath() : image.getOriginalPath();
                intent.putExtra(DataTag.IMAGE_PATH.toString(), path);

                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                images.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() { return images == null ? 0 : images.size(); }

    private void setImage(ImageView img, int position) {
        Bitmap imageBitmap = images.get(position).getMediumImageBitmap();
        if (imageBitmap == null) {
            String imagePath = images.get(position).getMediumResImagePath();
            imageBitmap = ((HomeSweetHome)context).getImageProcessor().decodeFileToMediumBitmap(imagePath);
        }

        Glide.with(this.context).asBitmap().load(imageBitmap).into(img);
    }

    public void setImages(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public void addImage(Image image) {
        if (images == null)
            images = new ArrayList<>();
        this.images.add(image);
        notifyItemInserted(images.size()-1);
    }

    public List<Image> getImages() { return images; }
}
