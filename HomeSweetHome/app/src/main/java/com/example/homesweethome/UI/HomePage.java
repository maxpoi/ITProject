package com.example.homesweethome.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.HelperClasses.ArtifactAdapter;
import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.HelperClasses.HomeSweetHome;
import com.example.homesweethome.R;
import com.example.homesweethome.UI.timeline.TimelineActivity;
import com.example.homesweethome.ViewModels.ArtifactListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class HomePage extends AppCompatActivity
                  implements NavigationView.OnNavigationItemSelectedListener{

    private ArtifactListViewModel artifactListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nag_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // if (savedInstanceState != null) { return; }
        // set Recyclerview
        RecyclerView rv = (RecyclerView) findViewById(R.id.gallery);
        rv.setHasFixedSize(true);
        rv.setDrawingCacheEnabled(true);
//        RecyclerView.LayoutManager lm = new GridLayoutManager(getApplicationContext(), 3);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);

        rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        final ArtifactAdapter artifactAdapter = new ArtifactAdapter(getApplicationContext());
        rv.setAdapter(artifactAdapter);

        artifactListViewModel = new ViewModelProvider(this).get(ArtifactListViewModel.class);
        artifactListViewModel.getArtifacts().observe(this, new Observer<List<Artifact>>() {
            @Override
            public void onChanged(List<Artifact> artifacts) {
                artifactAdapter.setArtifacts(artifacts);
            }
        });

        FloatingActionButton fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddPage();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Switch darkModeSwitch = findViewById(R.id.dark_mode_switch);
        darkModeSwitch.setChecked( ((HomeSweetHome)getApplication()).useDarkMode());
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((HomeSweetHome)getApplication()).setDarkMode(isChecked);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {
            case R.id.nav_home:
                openHomePage();
                break;
            case R.id.nav_search:
                openSearchPage();
                break;
            case R.id.nav_timeline:
                openTimelinePage();
            default:
                // do nothing;
                break;
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
        intent.putExtra(DataTag.TAG.toString(), DataTag.ADD.toString());
        intent.putExtra(DataTag.ARTIFACT_ID.toString(), artifactListViewModel.getLastArtifactId()+1);
        startActivity(intent);
    }

    private void openSearchPage() {
        Intent intent = new Intent(getApplicationContext(), SearchPage.class);
        startActivity(intent);
    }

    private void openTimelinePage() {
        Intent intent = new Intent(getApplicationContext(), TimelineActivity.class);
        startActivity(intent);
    }
}
