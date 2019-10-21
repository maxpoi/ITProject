package com.example.homesweethome.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homesweethome.AppDataBase.Entities.Artifact;
import com.example.homesweethome.AppDataBase.Entities.Image;
import com.example.homesweethome.AppDataBase.Entities.User;
import com.example.homesweethome.HelperClasses.ArtifactAdapter;
import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.HelperClasses.HomeSweetHome;
import com.example.homesweethome.HelperClasses.ImageProcessor;
import com.example.homesweethome.HelperClasses.SynchronizeHandler;
import com.example.homesweethome.R;
import com.example.homesweethome.UI.register.RegisterInformationActivity;
import com.example.homesweethome.UI.timeline.TimelineActivity;
import com.example.homesweethome.ViewModels.ArtifactListViewModel;
import com.example.homesweethome.ViewModels.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity
                  implements NavigationView.OnNavigationItemSelectedListener{

    private final int REQUEST_LOAD_IMAGE_CODE = 1;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ArtifactListViewModel artifactListViewModel;
    private UserViewModel userViewModel;
    private String memail;

    private ImageView portraitImage, backgroundImage;
    private Uri uriBackground;

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

        View headerLayout = navigationView.getHeaderView(0);
        portraitImage = headerLayout.findViewById(R.id.user_portrait);
        backgroundImage = headerLayout.findViewById(R.id.user_background);

        portraitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserEditPage();
            }
        });

        backgroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
//                String[] mimeTypes = {"image/jpeg", "image/png"}; // force to only upload those 2 types
                startActivityForResult(intent, REQUEST_LOAD_IMAGE_CODE);
            }
        });

        Switch darkModeSwitch = findViewById(R.id.dark_mode_switch);
        darkModeSwitch.setChecked( ((HomeSweetHome)getApplication()).useDarkMode());
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((HomeSweetHome)getApplication()).setDarkMode(isChecked);
            }
        });

        UserViewModel.UserViewModelFactory userViewModelFactory = new UserViewModel.UserViewModelFactory(getApplication(), getIntent().getStringExtra(DataTag.NEW_USER_EMAIL.toString()));
        userViewModel = new ViewModelProvider(this, userViewModelFactory).get(UserViewModel.class);

        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    memail = user.getEmail();

                    String portraitPath = user.getPortraitImagePath();
                    String bgPath = user.getBackgroundImagePath();
                    if (bgPath != null) {
                        Bitmap bg = ((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToLowBitmap(bgPath);
                        Glide.with(getApplicationContext()).asBitmap().load(bg).into(backgroundImage);
                    }

                    if (portraitPath != null) {
                        Bitmap bg = ((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToLowBitmap(portraitPath);
                        Glide.with(getApplicationContext()).asBitmap().load(bg).into(portraitImage);
                    }
                }
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
                break;
            case R.id.nav_logout:
                logout();
                break;
            default:
                // do nothing;
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_LOAD_IMAGE_CODE:
                    uriBackground = data.getData();
                    if (checkPermissions()) {
                        createImage();
                    } else {
                        ActivityCompat.requestPermissions(this, permissions, REQUEST_LOAD_IMAGE_CODE);
                    }
                    break;
                default:
                    Toast toast = Toast.makeText(getApplicationContext(), "Fail to upload, try again", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
            }
        }
    }

    private boolean checkPermissions() {

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allAllowed = true;
        switch (requestCode) {
            case REQUEST_LOAD_IMAGE_CODE:
                for (int res : grantResults)
                    allAllowed = allAllowed && (res == PackageManager.PERMISSION_GRANTED);

                if (allAllowed)
                    createImage();

                break;
            default:
                break;
        }

        if (!allAllowed) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please give permissions to access.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void createImage() {
        String bgPath = ImageProcessor.PARENT_FOLDER_PATH + ImageProcessor.BACKGROUND_IMAGE_FOLDER + ImageProcessor.BACKGROUND_IMAGE_NAME;

        // https://androidclarified.com/pick-image-gallery-camera-android/
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        // Get the cursor
        Cursor cursor = getContentResolver().query(uriBackground, filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();
        //Get the column index of MediaStore.Images.Media.DATA
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        //Gets the String value in the column
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        Image image = new Image(bgPath, bgPath, bgPath);
        image.setLowImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToLowBitmap(filePath));
        image.setMediumImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToMediumBitmap(filePath));
        image.setHighImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToHighBitmap(filePath));
        image.setOriginalPath(filePath);

        List<Image> temp = new ArrayList<>();
        temp.add(image);

        Glide.with(getApplicationContext()).load(backgroundImage).into(backgroundImage);

        User newUser = new User(userViewModel.getStaticUser());
        newUser.setBackgroundImagePath(bgPath);

        userViewModel.addUser(newUser);
        ((HomeSweetHome)getApplication()).getImageProcessor().saveImageListToLocal(temp);
    }

    private void logout(){
        SynchronizeHandler.getInstance().uploadUser(memail);
        openLoginPage();
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

    private void openLoginPage() {
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        intent.putExtra(DataTag.NEW_USER_EMAIL.toString(), memail);
        startActivity(intent);
    }

    private void openUserEditPage() {
        Intent intent = new Intent(getApplicationContext(), RegisterInformationActivity.class);
        intent.putExtra(DataTag.TAG.toString(), DataTag.EDIT.toString());
        intent.putExtra(DataTag.NEW_USER_EMAIL.toString(), memail);
        startActivity(intent);
    }


}
