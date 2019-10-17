package com.example.homesweethome.UI.register;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.homesweethome.UI.HomePage;
import com.example.homesweethome.UI.LoginPage;
import com.example.homesweethome.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private MutableLiveData<RegisterFormState> mRegisterFormState = new MutableLiveData<>();
    //private RegisterFormState registerFormState = new RegisterFormState(null, null, null);
    EditText mTextEmail;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    Button mButtonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // return button on top left
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");

/*
        // Find the activity_toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the activity_toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);



        // Display icon in the activity_toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);*/

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
                    // store input register data into database
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>(){

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (mTextPassword.length() < 6) {
                                            mTextPassword.setError(getString(R.string.app_name));
                                        }
                                        else {
                                            Intent MessageIntent = new Intent(RegisterActivity.this, RegisterSuccessActivity.class);
                                            startActivity(MessageIntent);
                                        }
                                    }

                                    else {
                                        Intent intent = new Intent(RegisterActivity.this, RegisterFailActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
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
            String passwordInput = mTextPassword.getText().toString().trim();
            String cnfPasswordInput = mTextCnfPassword.getText().toString().trim();

            mButtonRegister.setEnabled(!emailInput.isEmpty() && ! passwordInput.isEmpty() && !cnfPasswordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
            //check validity
            registerDataChanged((mTextEmail.getText().toString()), mTextPassword.getText().toString(), mTextCnfPassword.getText().toString());
        }
    };

    // A placeholder username validation check
    private boolean isEmailAddressValid(String email) {
        if (email == null) {
            return false;
        }

        //TODO: if the email is registered already
        //TODO: if email exists

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


    public void registerDataChanged(String email, String password, String repassword) {
        if (!isEmailAddressValid(email)) {
            //registerValid = false;
            mRegisterFormState.setValue(new RegisterFormState(R.string.invalid_register_email, null, null));
        } else if (!isPasswordValid(password)) {
            //registerValid = false;
            mRegisterFormState.setValue(new RegisterFormState(null, R.string.invalid_register_password, null));
        } else if (!isCnfPasswordValid(password, repassword)) {
            //registerValid = false;
            mRegisterFormState.setValue(new RegisterFormState(null, null, R.string.invalid_register_cnf_password));
        } else {
            //registerValid = true;
            mRegisterFormState.setValue(new RegisterFormState(true));
        }
    }

    public void register(View view){
        Intent MessageIntent = new Intent(RegisterActivity.this, RegisterSuccessActivity.class);
        startActivity(MessageIntent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public void failedByEmail(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        alertDialog.setTitle("Invalid Email");
        alertDialog.setMessage("Email is invalid, please enter a valid email.");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void failedByPassword(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        alertDialog.setTitle("Invalid Password");
        alertDialog.setMessage("Password is invalid, please enter a password with length 6-20 contains only digits and letters.");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void failedByCnfPassword(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        alertDialog.setTitle("Unmatched Password");
        alertDialog.setMessage("The password you input does not match each other.");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
