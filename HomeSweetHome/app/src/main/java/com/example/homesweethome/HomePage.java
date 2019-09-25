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
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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

        CustomTest customTest = new CustomTest(getApplicationContext());
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
//        UserCache.getInstance().setCells(cells);
        String[] folders = getFilesDir().list();
        if (folders == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Don't have read permission", Toast.LENGTH_SHORT);
            toast.show();
            return ;
        }

        int numFolders = folders.length;
        UserCache userCache = UserCache.getInstance();
        // if never add an artifact before
        if (numFolders == 0) {
            userCache.initialize();
            return ;
        }

//        int numCells;
//        // if just open the app, which means there is no cache
//       if (userCache.getCells() == null) {
//           numCells = 0;
//       } else {
//           numCells = userCache.getCells().size();
//       }
        // if cache is exactly the same, just return
        // todo
//        if (numFolders == numCells) { return ; }

        // otherwise
        ArrayList<Cell> cells = new ArrayList<>();
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
