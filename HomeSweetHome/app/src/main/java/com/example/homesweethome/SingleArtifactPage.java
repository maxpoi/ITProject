package com.example.homesweethome;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SingleArtifactPage extends AppCompatActivity{

    private String src;
    private int test_src;
    private ImageView img;
    private boolean hasAudio = true;

    // test varaibles
    private ArrayList<Cell> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_artifact_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("AddPage a new artifact");
        getSupportActionBar().setSubtitle("Change/delete this subtitle if needed");

        Intent intent = getIntent();
        test_src = intent.getIntExtra("id", R.drawable.img_1);
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");

//        img = (ImageView) findViewById(R.id.image);
//        Glide.with(getApplicationContext()).load(test_src).into(img);
        images = new ArrayList<>();
        images.add(new Cell(test_src));
        test();

        TextView title_content = findViewById(R.id.title);
        title_content.setText(title);

        TextView desc_content = findViewById(R.id.description);
        desc_content.setText(desc);

//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), com.example.homesweethome.SingleImagePage.class);
//                i.putExtra("id", test_src);
//                startActivity(i);
//            }
//        });

        final FloatingActionButton edit_button = (FloatingActionButton) findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddPage.class);
                startActivity(i);
            }
        });

        final FloatingActionButton audio_play = (FloatingActionButton) findViewById(R.id.audio_play);

        if (this.hasAudio)
            audio_play.show();
        else
            audio_play.hide();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void test() {
        images.add(new Cell(R.drawable.img_1));
        images.add(new Cell(R.drawable.img_2));
        images.add(new Cell(R.drawable.img_3));
        images.add(new Cell(R.drawable.img_4));
        images.add(new Cell(R.drawable.img_5));

        RecyclerView rv = (RecyclerView) findViewById(R.id.gallery);
        rv.setHasFixedSize(true);
        rv.setItemViewCacheSize(100);
        rv.setDrawingCacheEnabled(true);
        rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(lm);

        ImageAdapter ia = new ImageAdapter(getApplicationContext(), this.getClass().getName(), this.images, R.id.recycleview_image, R.layout.recycleview_image);
        rv.setAdapter(ia);

    }
}
