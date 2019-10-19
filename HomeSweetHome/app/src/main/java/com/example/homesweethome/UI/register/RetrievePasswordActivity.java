package com.example.homesweethome.UI.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homesweethome.UI.LoginPage;
import com.example.homesweethome.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RetrievePasswordActivity extends AppCompatActivity implements View.OnClickListener{

    EditText mTextEmail;
    Button mButtonValidation;
    private Button dialogButton;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_validation_);
        // return button on top left
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Retrieve Password");

        mTextEmail = (EditText) findViewById(R.id.email);
        mButtonValidation = (Button) findViewById(R.id.button_retrieve);

        mTextEmail.addTextChangedListener(afterTextChangedListener);

        //mButtonValidation.setOnClickListener(this);
        mButtonValidation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_background_dialog);
                dialogButton = (Button) dialog.findViewById(R.id.button_ok);
                dialog.show();

                // add button listener
                dialogButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent MessageIntent = new Intent(RetrievePasswordActivity.this, LoginPage.class);
                        startActivity(MessageIntent);
                    }
                });
            }
        });
    }

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

    TextWatcher afterTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // ignore
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String emailInput = mTextEmail.getText().toString().trim();

            mButtonValidation.setEnabled(!emailInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
            //check validity
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_retrieve:
                Toast.makeText(this, "retrieve the password", Toast.LENGTH_SHORT).show();
                String email = mTextEmail.getText().toString();
                if (!isEmailAddressValid(email)){
                    failedByInvalidEmail();
                } else if (emailNotRegistered(email)) {
                    failedByNotRegistered();
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(RetrievePasswordActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RetrievePasswordActivity.this, "please go check your email for reset password", Toast.LENGTH_SHORT).show();
                                Intent loginIntent = new Intent(RetrievePasswordActivity.this, LoginPage.class);
                                startActivity(loginIntent);
                            }
                            else{
                                Toast.makeText(RetrievePasswordActivity.this, "email is not exist, please check and try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    Intent registerIntent = new Intent(RetrievePasswordActivity.this, ResetPasswordActivity.class);
//                    startActivity(registerIntent);
                }
                break;
            default:
                break;
        }
    }

    private boolean isEmailAddressValid(String email) {
        if (email == null) {
            return false;
        }

        //TODO: if email exists

        if (email.contains("@")) {
            return true;
        }
        else {
            return false;
            //return !email.trim().isEmpty();
        }
    }

    public boolean emailNotRegistered(String email){
        //TODO: check if the email is registered

        // Have records
         return false;

        // Not registered
        //return true;
    }

    public void failedByInvalidEmail(){
        AlertDialog alertDialog = new AlertDialog.Builder(RetrievePasswordActivity.this).create();
        alertDialog.setTitle("Invalid Email");
        alertDialog.setMessage("Input email is invalid, please enter again.");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void failedByNotRegistered(){
        AlertDialog.Builder builder = new AlertDialog.Builder(RetrievePasswordActivity.this);

        builder.setTitle("Email Not Registered");

        builder.setMessage("Input email is not registered yet, please choose to enter again " +
                "or to register.");
        builder.setPositiveButton("Enter Again", null);
        builder.setNegativeButton("Register", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent registerIntent = new Intent(RetrievePasswordActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
        builder.show();
    }

}
