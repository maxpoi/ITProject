package com.example.homesweethome.UI.register;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.homesweethome.AppDataBase.Entities.Image;
import com.example.homesweethome.AppDataBase.Entities.User;
import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.HelperClasses.HomeSweetHome;
import com.example.homesweethome.HelperClasses.ImageProcessor;
import com.example.homesweethome.HelperClasses.SynchronizeHandler;
import com.example.homesweethome.R;
import com.example.homesweethome.UI.HomePage;
import com.example.homesweethome.UI.LoginPage;
import com.example.homesweethome.ViewModels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class RegisterInformationActivity extends AppCompatActivity {
    private final int REQUEST_LOAD_IMAGE_CODE = 1;

    private EditText name;
    private EditText dob_year;
    private EditText dob_month;
    private EditText dob_day;
    private EditText gender;
    private EditText intro;
    private Button finish_button;
    private Button head_protrait_button;
    private ImageView head_protrait;
    private String tag;
    private UserViewModel userViewModel;

    private Uri uriImage;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        // return button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User Information");
        getSupportActionBar().setSubtitle("Fields with a '*' must be filled");

        name = (EditText) findViewById(R.id.name);
        dob_year = (EditText) findViewById(R.id.edit_date_year);
        dob_month = (EditText) findViewById(R.id.edit_date_month);
        dob_day = (EditText) findViewById(R.id.edit_date_day);
        gender = (EditText) findViewById(R.id.sex);
        intro = (EditText) findViewById(R.id.self_intro);
        finish_button = (Button) findViewById(R.id.next);
        head_protrait_button = (Button) findViewById(R.id.upload_image_button);
        head_protrait = (ImageView) findViewById(R.id.head_portrait);

        name.addTextChangedListener(afterTextChangedListener);
        dob_year.addTextChangedListener(afterTextChangedListener);
        dob_month.addTextChangedListener(afterTextChangedListener);
        dob_day.addTextChangedListener(afterTextChangedListener);
        gender.addTextChangedListener(afterTextChangedListener);
        intro.addTextChangedListener(afterTextChangedListener);

        UserViewModel.UserViewModelFactory userViewModelFactory = new UserViewModel.UserViewModelFactory(getApplication(), getIntent().getStringExtra(DataTag.NEW_USER_EMAIL.toString()));
        userViewModel = new ViewModelProvider(this, userViewModelFactory).get(UserViewModel.class);
        String tag = getIntent().getStringExtra(DataTag.TAG.toString());

        if (userViewModel.getUser()!=null&&!tag.equals(DataTag.CREATE.toString())){
            userViewModel.getUser().observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        String date = user.getDOB();
                        String[] dates = date.split("/");
                        dob_year.setText(dates[0]);
                        dob_month.setText(dates[1]);
                        dob_day.setText(dates[2]);
                        name.setText(user.getUserName());
                        gender.setText(user.getGender());
                        intro.setText(user.getDesc());


                    }
                }
            });
        }







        head_protrait_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
//                String[] mimeTypes = {"image/jpeg", "image/png"}; // force to only upload those 2 types
                startActivityForResult(intent, REQUEST_LOAD_IMAGE_CODE);
            }
        });




        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name_ = name.getText().toString();
                final String dob_year_ = dob_year.getText().toString();
                final String dob_month_ = dob_month.getText().toString();
                final String dob_day_ = dob_day.getText().toString();
                final String gender_ = gender.getText().toString();
                final String intro_ = intro.getText().toString();
                final String email_ = getIntent().getStringExtra(DataTag.NEW_USER_EMAIL.toString());
                final String password_ = getIntent().getStringExtra(DataTag.NEW_USER_PASSWORD.toString());

                if (!isNameValid(name_)){
                    failedByName();
                }else if (!isYearValid(dob_year_)||!isMonthValid(dob_month_)||!isDayValid(dob_month_, dob_day_)){
                    failedByDate();
                }else if (!isGenderValid(gender_)){
                    failedByGender();
                }else if (!isIntroValid(intro_)){
                    failedByIntro();
                }else{
                    // store input register data into database
                    String tag = getIntent().getStringExtra(DataTag.TAG.toString());
                    if(tag!=null && tag.equals(DataTag.EDIT.toString())) {
                        saveToDatabase(email_, name_, dob_year_,
                                dob_month_, dob_day_, gender_, intro_);
                        openHomePage();
                        return;
                    }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email_, password_)
                            .addOnCompleteListener(RegisterInformationActivity.this, new OnCompleteListener<AuthResult>(){

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        if (checkPermissions()) {
//                                            // do nothing
                                        } else {
                                            System.out.println("requset permission");
                                            ActivityCompat.requestPermissions(RegisterInformationActivity.this, permissions, REQUEST_LOAD_IMAGE_CODE);
                                        }

                                        saveToDatabase(email_, name_, dob_year_,
                                                dob_month_, dob_day_, gender_, intro_);
                                        SynchronizeHandler.getInstance().uploadUser(email_);


                                        final Dialog dialog = new Dialog(context);
                                        dialog.setContentView(R.layout.activity_background_dialog);

                                        // set title
                                        TextView title = (TextView) dialog.findViewById(R.id.title);
                                        title.setText("Register Success!");
                                        title.setTextSize(20);

                                        // set text, image and button
                                        TextView text = (TextView) dialog.findViewById(R.id.text);
                                        text.setText("You have registered a new account. Please login now.");
                                        text.setTextSize(17);
                                        ImageView image = (ImageView) dialog.findViewById(R.id.image_title);
                                        image.setImageResource(R.mipmap.ic_timeline_button_white_round);

                                        Button dialogButton = (Button) dialog.findViewById(R.id.button_ok);
                                        // if button is clicked, close the custom dialog
                                        dialogButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent MessageIntent = new Intent(RegisterInformationActivity.this, LoginPage.class);
                                                startActivity(MessageIntent);

                                            }
                                        });

                                        dialog.show();


                                    }

                                    else {
                                        failedByInternet();
                                    }
                                }
                            });
                }
            }
        });
    }

    TextWatcher afterTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // ignore
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nameInput = name.getText().toString().trim();
            String dobYearInput = dob_year.getText().toString().trim();
            String dobMonthInput = dob_month.getText().toString().trim();
            String dobDayInput = dob_day.getText().toString().trim();
            String genderInput = gender.getText().toString().trim();

            finish_button.setEnabled(!nameInput.isEmpty() && ! dobDayInput.isEmpty() && !dobMonthInput.isEmpty( )&& !dobYearInput.isEmpty()&& !genderInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                String tag = getIntent().getStringExtra(DataTag.TAG.toString());
                if (tag!=null && tag.equals(DataTag.EDIT.toString()))
                    openHomePage();
                else
                    openLoginPage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openLoginPage() {
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
    }


    private boolean isNameValid(String name){
        return (name.length() <= 50 && name.length() >= 1);
    }

    private boolean isYearValid(String year){
        return year.length()==4;
    }

    private boolean isMonthValid(String month){
        return Integer.parseInt(month)>=1&&Integer.parseInt(month)<=12;
    }

    private boolean isDayValid(String month_, String day_){
        int month = Integer.parseInt(month_);
        int day = Integer.parseInt(day_);
        ArrayList<Integer> months_30 = new ArrayList<Integer>();
        months_30.add(4);
        months_30.add(6);
        months_30.add(9);
        months_30.add(11);

        ArrayList<Integer> months_31 = new ArrayList<Integer>();
        months_31.add(1);
        months_31.add(3);
        months_31.add(5);
        months_31.add(7);
        months_31.add(8);
        months_31.add(10);
        months_31.add(12);

        if (month==2){return day>=1&&day<=28;}
        else if (months_30.contains(month)){return day>=1&&day<=30;}
        else if (months_31.contains(month)){return day>=1&&day<=31;}
        else{return true;}
    }

    private boolean isGenderValid(String gender){
        if (gender.equals("Female")||gender.equals("Male")){
            return true;
        }
        else{return false;}
    }

    private boolean isIntroValid(String intro){
        return (intro.length()>=0&&intro.length()<=150);
    }

    private void failedByName(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterInformationActivity.this).create();
        alertDialog.setTitle(Html.fromHtml("<font color='#af0404'>Name Invalid</font>"));
        alertDialog.setMessage(Html.fromHtml("<font color='#272121'>Name should be between 1 and 50 characters. Please enter again.</font>"));

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void failedByGender(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterInformationActivity.this).create();
        alertDialog.setTitle(Html.fromHtml("<font color='#af0404'>Gender Invalid</font>"));
        alertDialog.setMessage(Html.fromHtml("<font color='#272121'>Gender should be one of 'Male' and 'Female'. Please enter again.</font>"));

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void failedByDate(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterInformationActivity.this).create();
        alertDialog.setTitle(Html.fromHtml("<font color='#af0404'>Date of Birth Invalid</font>"));
        alertDialog.setMessage(Html.fromHtml("<font color='#272121'>Date of birth is entered inappropriately. (Form should be YYYY-MM-DD). Please enter again.</font>"));

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void failedByIntro(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterInformationActivity.this).create();
        alertDialog.setTitle(Html.fromHtml("<font color='#af0404'>Personalized Signature Invalid</font>"));
        alertDialog.setMessage(Html.fromHtml("<font color='#272121'>Length of personalized signature should be between 1 and 300 characters. Please enter again.</font>"));

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void failedByInternet(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterInformationActivity.this).create();
        alertDialog.setTitle(Html.fromHtml("<font color='#af0404'>Register Fail</font>"));
        alertDialog.setMessage(Html.fromHtml("<font color='#272121'>Internet is busy, email is failed to be sent. Please try again.</font>"));

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_LOAD_IMAGE_CODE:
                    uriImage = data.getData();
                    if (checkPermissions()) {
                        // TODO: put image as head portrait into database
                        head_protrait.setVisibility(View.VISIBLE);
                        Glide.with(this.context).asBitmap().load(uriImage).into(head_protrait);
                        //createImage();
                    } else {
                        ActivityCompat.requestPermissions(this, permissions, REQUEST_LOAD_IMAGE_CODE);
                    }
                    break;
                /*
                case REQUEST_INPUT_CODE:
                    String input = data.getStringExtra(DataTag.INPUT_TEXT.toString());
                    desc.setText(input);
                    break;*/
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

    private void saveToDatabase(String email_, String username, String dob_year_, String dob_month_, String dob_day_, String gender_, String intro_){

        String imagePath = ImageProcessor.PARENT_FOLDER_PATH +
        ImageProcessor.PORTRAIT_IMAGE +
        ImageProcessor.PORTRAIT_NAME +
        ImageProcessor.IMAGE_TYPE;

        //System.out.println("saving to database");


        // https://androidclarified.com/pick-image-gallery-camera-android/
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        // Get the cursor
        Cursor cursor = getContentResolver().query(uriImage, filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();
        //Get the column index of MediaStore.Images.Media.DATA
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        //Gets the String value in the column
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        System.out.println("file path is : " + filePath);

        List<Image> portrait = new ArrayList<>();


        Image portraitImage = new Image(imagePath, imagePath, imagePath);
        portraitImage.setLowImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToLowBitmap(filePath));
        portraitImage.setMediumImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToLowBitmap(filePath));
        portraitImage.setHighImageBitmap(((HomeSweetHome)getApplication()).getImageProcessor().decodeFileToLowBitmap(filePath));

        portrait.add(portraitImage);

        ((HomeSweetHome)getApplication()).getImageProcessor().saveImageListToLocal(portrait);


        User newUser = new User(
                email_,
                username,
                dob_year_ +
                        DataTag.DATE_SEPERATOR.toString() +
                        dob_month_ + DataTag.DATE_SEPERATOR.toString() +
                        dob_day_,
                gender_,
                intro_,
                imagePath,
                null);
        ((HomeSweetHome)getApplication()).getRepository().addUser(newUser);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allAllowed = true;
        switch (requestCode) {
            case REQUEST_LOAD_IMAGE_CODE:
                for (int res : grantResults)
                    allAllowed = allAllowed && (res == PackageManager.PERMISSION_GRANTED);
        }

        if (!allAllowed) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please give permissions to access.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void openHomePage() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
    }
}