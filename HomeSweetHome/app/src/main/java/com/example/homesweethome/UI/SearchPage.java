package com.example.homesweethome.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.HelperClasses.ArtifactAdapter;
import com.example.homesweethome.R;
import com.example.homesweethome.ViewModels.ArtifactListViewModel;

import java.util.List;

public class SearchPage extends AppCompatActivity {

    private ArtifactAdapter artifactAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Artifact");

        // if (savedInstanceState != null) { return; }
        // set Recyclerview
        RecyclerView rv = (RecyclerView) findViewById(R.id.gallery);
        rv.setHasFixedSize(true);
        rv.setDrawingCacheEnabled(true);
//        RecyclerView.LayoutManager lm = new GridLayoutManager(getApplicationContext(), 3);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);

        rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        artifactAdapter = new ArtifactAdapter(getApplicationContext());
        rv.setAdapter(artifactAdapter);

        final ArtifactListViewModel artifactListViewModel = new ViewModelProvider(this).get(ArtifactListViewModel.class);
        setAdapter(artifactListViewModel.getArtifacts());

        final EditText searchBox = findViewById(R.id.search_box);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.toString().isEmpty()) {
                    setAdapter(artifactListViewModel.getArtifacts());
                } else {
                    setAdapter(artifactListViewModel.searchAllArtifacts("*" + s + "*"));
                }
            }
        });

        ImageButton searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable query =  searchBox.getText();
                if (query == null || query.toString().isEmpty()) {
                    setAdapter(artifactListViewModel.getArtifacts());
                } else {
                    setAdapter(artifactListViewModel.searchAllArtifacts("*" + query + "*"));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openHomePage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setAdapter(LiveData<List<Artifact>> artifacts) {
        artifacts.observe(this, new Observer<List<Artifact>>() {
            @Override
            public void onChanged(List<Artifact> artifacts) {
                artifactAdapter.setArtifacts(artifacts);
            }
        });
    }

    private void openHomePage() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
    }
}
