package com.example.homesweethome.UI.register;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homesweethome.R;
import com.example.homesweethome.UI.LoginPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class RegisterInformationActivity extends AppCompatActivity {
    private EditText name;
    private EditText dob_year;
    private EditText dob_month;
    private EditText dob_day;
    private EditText gender;
    private EditText intro;
    private Button finishButton;


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
        finishButton = (Button) findViewById(R.id.next);

        name.addTextChangedListener(afterTextChangedListener);
        dob_year.addTextChangedListener(afterTextChangedListener);
        dob_month.addTextChangedListener(afterTextChangedListener);
        dob_day.addTextChangedListener(afterTextChangedListener);
        gender.addTextChangedListener(afterTextChangedListener);
        intro.addTextChangedListener(afterTextChangedListener);


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name_ = name.getText().toString();
                String dob_year_ = dob_year.getText().toString();
                String dob_month_ = dob_month.getText().toString();
                String dob_day_ = dob_day.getText().toString();
                String gender_ = gender.getText().toString();
                String intro_ = intro.getText().toString();

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
                    //TODO: store input register data into database
            //        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            //                .addOnCompleteListener(RegisterInformationActivity.this, new OnCompleteListener<AuthResult>(){

            //                    @Override
            //                    public void onComplete(@NonNull Task<AuthResult> task) {
            //                        if (!task.isSuccessful()) {
            //                            // there was an error
            //                            if (mTextPassword.length() < 6) {
            //                                mTextPassword.setError(getString(R.string.app_name));
            //                            }
            //                            else {
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
                                            image.setImageResource(R.drawable.ic_launcher_background);

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

                 //                       }
                 //                   }

                //                    else {
                //                        failedByInternet();
                //                        finish();
                 //                   }
                 //               }
                 //           });


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

            finishButton.setEnabled(!nameInput.isEmpty() && ! dobDayInput.isEmpty() && !dobMonthInput.isEmpty( )&& !dobYearInput.isEmpty()&& !genderInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
            //check validity
            //registerDataChanged((mTextEmail.getText().toString()), mTextPassword.getText().toString(), mTextCnfPassword.getText().toString());
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
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


    public boolean isNameValid(String name){
        return (name.length() <= 50 && name.length() >= 1);
    }

    public boolean isYearValid(String year){
        return year.length()==4;
    }

    public boolean isMonthValid(String month){
        return Integer.parseInt(month)>=1&&Integer.parseInt(month)<=12;
    }

    public boolean isDayValid(String month_, String day_){
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

    public boolean isGenderValid(String gender){
        if (gender.equals("Female")||gender.equals("Male")){
            return true;
        }
        else{return false;}
    }

    public boolean isIntroValid(String intro){
        return (intro.length()>=0&&intro.length()<=150);
    }

    public void failedByName(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterInformationActivity.this).create();
        alertDialog.setTitle("Name Invalid");
        alertDialog.setMessage("Name should be between 1 and 50 characters. Please enter again.");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void failedByGender(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterInformationActivity.this).create();
        alertDialog.setTitle("Gender Invalid");
        alertDialog.setMessage("Gender should be one of 'Male' and 'Female'. Please enter again.");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void failedByDate(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterInformationActivity.this).create();
        alertDialog.setTitle("Date of Birth Invalid");
        alertDialog.setMessage("Date of birth is entered inappropriately. (Form should be YYYY-MM-DD). Please enter again.");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void failedByIntro(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterInformationActivity.this).create();
        alertDialog.setTitle("Self introduction Invalid");
        alertDialog.setMessage("Length of self introduction should be between 1 and 300 characters. Please enter again.");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void failedByInternet(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterInformationActivity.this).create();
        alertDialog.setTitle("Register Fail");
        alertDialog.setMessage("Internet is busy, email is failed to be sent. Please try again.");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void register(View view){
        Intent MessageIntent = new Intent(RegisterInformationActivity.this, RegisterSuccessActivity.class);
        startActivity(MessageIntent);
    }

}