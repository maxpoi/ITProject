package com.example.homesweethome.UI.register;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.UI.HomePage;
import com.example.homesweethome.UI.LoginPage;
import com.example.homesweethome.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText mTextEmail;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    Button mButtonRegister;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // return button on top left
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");


        mTextEmail = (EditText) findViewById(R.id.email);
        mTextPassword = (EditText) findViewById(R.id.register_password);
        mTextCnfPassword = (EditText) findViewById(R.id.register_cfn_password);
        mButtonRegister = (Button) findViewById(R.id.button_register);

        mTextEmail.addTextChangedListener(afterTextChangedListener);
        mTextPassword.addTextChangedListener(afterTextChangedListener);
        mTextCnfPassword.addTextChangedListener(afterTextChangedListener);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mTextEmail.getText().toString();
                String password = mTextPassword.getText().toString();
                String cnfPassword = mTextCnfPassword.getText().toString();
                if (!isEmailAddressValid(email)){
                    failedByEmail();
                } else if (!isPasswordValid(password)){
                    failedByPassword();
                } else if (!isCnfPasswordValid(password, cnfPassword)){
                    failedByCnfPassword();
                } else{

                    openRegisterInformationActivity(email, password);
                }
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

    // jump to login page
    private void openLoginPage() {
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
    }

    // jump to user information page
    private void openRegisterInformationActivity(String email, String password) {
        String action = "create";
        Intent intent = new Intent(getApplicationContext(), RegisterInformationActivity.class);
        intent.putExtra(DataTag.NEW_USER_EMAIL.toString(),  email);
        intent.putExtra(DataTag.NEW_USER_PASSWORD.toString(), password);
        intent.putExtra(DataTag.TAG.toString(), action);
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
            String passwordInput = mTextPassword.getText().toString().trim();
            String cnfPasswordInput = mTextCnfPassword.getText().toString().trim();

            mButtonRegister.setEnabled(!emailInput.isEmpty() && ! passwordInput.isEmpty() && !cnfPasswordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    // A placeholder username validation check
    private boolean isEmailAddressValid(String email) {
        if (email == null) {
            return false;
        }

        if (email.contains("@")) {
            return true;
        }
        else {
            return false;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }
        if (6 <= password.length() && password.length() <= 20){
            for (int i = 0; i < password.length(); i++) {
                // checks whether the character is a letter or a digit if it is not a letter ,it will return false
                if ((!Character.isLetter(password.charAt(i))&& !Character.isDigit(password.charAt(i)))) {

                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    // A placeholder confirmation password validation check
    private boolean isCnfPasswordValid(String password, String repassword) {
        return password.equals(repassword) && password != null && repassword != null;
    }

    // dialog failed by email
    public void failedByEmail(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        alertDialog.setTitle(Html.fromHtml("<font color='#af0404'>Invalid Email</font>"));
        alertDialog.setMessage(Html.fromHtml("<font color='#272121'>Email is invalid, please enter a valid email.</font>"));

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    // dialog failed by password
    public void failedByPassword(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        alertDialog.setTitle(Html.fromHtml("<font color='#af0404'>Invalid Password</font>"));
        alertDialog.setMessage(Html.fromHtml("<font color='#272121'>Password is invalid, please enter a password with length 6-20 contains only digits and letters.</font>"));

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    // dialog falied by confirming password
    public void failedByCnfPassword(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        alertDialog.setTitle(Html.fromHtml("<font color='#af0404'>Unmatched Password</font>"));
        alertDialog.setMessage(Html.fromHtml("<font color='#272121'>The password you input does not match each other.</font>"));

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
