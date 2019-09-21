package com.example.homesweethome;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity
                  implements NavigationView.OnNavigationItemSelectedListener{

    private ArrayList<Cell> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nag_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                openAddPage();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        test();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_home) {
            openHomePage();
            // destroy current home activity to reduce memory usage
            // or maybe can just close the navigationView?
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openHomePage() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
    }

    private void openAddPage() {
        Intent intent = new Intent(getApplicationContext(), AddPage.class);
        startActivity(intent);
    }

    private void test() {
        this.images = new ArrayList<>();
        images.add(new Cell(R.drawable.img_1));
        images.add(new Cell(R.drawable.img_2));
        images.add(new Cell(R.drawable.img_3));
        images.add(new Cell(R.drawable.img_4));
        images.add(new Cell(R.drawable.img_5));
        images.add(new Cell(R.drawable.img_1));
        images.add(new Cell(R.drawable.img_2));
        images.add(new Cell(R.drawable.img_3));
        images.add(new Cell(R.drawable.img_4));
        images.add(new Cell(R.drawable.img_5));
        images.add(new Cell(R.drawable.img_1));
        images.add(new Cell(R.drawable.img_2));
        images.add(new Cell(R.drawable.img_3));
        images.add(new Cell(R.drawable.img_4));
        images.add(new Cell(R.drawable.img_5));
        images.add(new Cell(R.drawable.img_1));
        images.add(new Cell(R.drawable.img_2));
        images.add(new Cell(R.drawable.img_3));
        images.add(new Cell(R.drawable.img_4));
        images.add(new Cell(R.drawable.img_5));
        images.add(new Cell(R.drawable.img_1));
        images.add(new Cell(R.drawable.img_2));
        images.add(new Cell(R.drawable.img_3));
        images.add(new Cell(R.drawable.img_4));
        images.add(new Cell(R.drawable.img_5));
        images.add(new Cell(R.drawable.img_1));
        images.add(new Cell(R.drawable.img_2));
        images.add(new Cell(R.drawable.img_3));
        images.add(new Cell(R.drawable.img_4));
        images.add(new Cell(R.drawable.img_5));
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

        RecyclerView.LayoutManager lm = new GridLayoutManager(getApplicationContext(), 3);
        rv.setLayoutManager(lm);

        ImageAdapter ia = new ImageAdapter(getApplicationContext(), this.getClass().getName(), this.images, R.id.recycleview_image, R.layout.recycleview_image);
        rv.setAdapter(ia);
    }
}
