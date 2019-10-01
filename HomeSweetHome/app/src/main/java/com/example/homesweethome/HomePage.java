package com.example.homesweethome;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity
                  implements NavigationView.OnNavigationItemSelectedListener{

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
                finish();
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

        storeUserCache();
        setImages();
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

    // to do
    private void storeUserCache() {
        String[] folders = getFilesDir().list();
        if (folders == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Don't have read permission", Toast.LENGTH_SHORT);
            toast.show();
            return ;
        }

        // if never add an artifact before
        int numFolders = folders.length;
        if (numFolders == 0) { return ; }

        // if has cache already, read from cache.
        UserCache userCache = UserCache.getInstance();
        ArrayList<Cell> cells = userCache.getCells();
        if (cells.size() != 0) { return ; }

        // otherwise, initialize the cache for this session.
        cells = new ArrayList<>();
        for (int i = 0; i<numFolders; i++) {
            File cellFolder = new File(getFilesDir(), Integer.toString(i));

            Cell cell = new Cell();
            cell.setTitle(readFileString(cellFolder, "title"));
            cell.setDate(readFileString(cellFolder, "date"));
            cell.setDesc(readFileString(cellFolder, "desc"));

//            File lowImageFolder = new File(cellFolder, "low_image");
            File mediumImageFolder = new File(cellFolder, "medium_image");
//            File highImageFolder = new File(cellFolder, "high_image");

            // read images
            // now assume only 1 resolution will be used for the entire session
            for (int j=0; j<mediumImageFolder.list().length; j++) {
                Image image = new Image();
                String id = Integer.toString(j);
                byte[] mediumImage = readFileByte(mediumImageFolder, id);
//              byte[] lowImage = readFileByte(lowImageFolder, "low_image");
//              byte[] highImage = readFileByte(highImageFolder, "low_image");
                image.setMediumImageByte(mediumImage);
                image.setPosition(i);
                cell.addImage(image);
            }
            cells.add(cell);
        }
        userCache.setCells(cells);
    }

    private String readFileString(File folder, String filename) {
        File file = new File(folder, filename);

        try {
            byte[] output = new byte[(int)file.length()];
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(output);
            inputStream.close();
            return new String(output);
        } catch (IOException e) {
            return null;
        }
    }

    private byte[] readFileByte(File folder, String filename) {
        File file = new File(folder, filename);

        try {
            byte[] output = new byte[(int)file.length()];
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(output);
            inputStream.close();
            return output;
        } catch (IOException e) {
            return null;
        }
    }

    private void setImages() {
        RecyclerView rv = (RecyclerView) findViewById(R.id.gallery);
        rv.setHasFixedSize(true);
        rv.setDrawingCacheEnabled(true);

        RecyclerView.LayoutManager lm = new GridLayoutManager(getApplicationContext(), 3);
        rv.setLayoutManager(lm);

        ImageAdapter ia = new ImageAdapter(getApplicationContext(), this.getClass().getName(), UserCache.getInstance().getAllImages(), R.id.recycleview_image, R.layout.recycleview_image);
        ia.setImageResolution(-1);
        rv.setAdapter(ia);
    }
}
