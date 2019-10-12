package com.example.homesweethome.UI.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homesweethome.UI.LoginPage;
import com.example.homesweethome.R;
import com.google.firebase.auth.FirebaseAuth;

public class RetrievePasswordActivity extends AppCompatActivity implements View.OnClickListener{

    EditText mTextEmail;
    Button mButtonValidation;
    Button mButtonLogin;
    Button mButtonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);


        mTextEmail = (EditText) findViewById(R.id.email);
        mButtonValidation = (Button) findViewById(R.id.button_retrieve);
        mButtonLogin = (Button) findViewById(R.id.return_to_login);
        mButtonRegister = (Button) findViewById(R.id.return_to_register);

        mTextEmail.addTextChangedListener(afterTextChangedListener);

        mButtonValidation.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);
        mButtonRegister.setOnClickListener(this);

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
            case R.id.return_to_login:
                Toast.makeText(this, "jump to login page", Toast.LENGTH_SHORT).show();

                // TODO: Jump to login page
                Intent loginIntent = new Intent(RetrievePasswordActivity.this, LoginPage.class);
                startActivity(loginIntent);
                break;
            case R.id.button_retrieve:
                Toast.makeText(this, "retrieve the password", Toast.LENGTH_SHORT).show();
                String email = mTextEmail.getText().toString();
                if (!isEmailAddressValid(email)){
                    failedByInvalidEmail();
                } else if (emailNotRegistered(email)) {
                    failedByNotRegistered();
                } else {
//                    FirebaseAuth.getInstance().
                    Intent registerIntent = new Intent(RetrievePasswordActivity.this, EmailValidationActivity.class);
                    startActivity(registerIntent);
                }
                break;
            case R.id.return_to_register:
                Toast.makeText(this, "jump to register page", Toast.LENGTH_SHORT).show();
                Intent registerIntent = new Intent(RetrievePasswordActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
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
