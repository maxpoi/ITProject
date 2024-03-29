package com.example.homesweethome.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.HelperClasses.SynchronizeHandler;
import com.example.homesweethome.R;
import com.example.homesweethome.UI.register.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.resources.MaterialResources;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    EditText mTextEmail;
    EditText mTextPassword;
    Button loginButton;
    Button registerButton;
    Button retrievePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        loginButton = (Button) findViewById(R.id.login_button);
        registerButton = (Button) findViewById(R.id.register_button);
        retrievePassword = (Button) findViewById(R.id.retrieve_password);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });
        retrievePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent retrievePassword = new Intent(LoginPage.this, RetrievePasswordActivity.class);
                startActivity(retrievePassword);
            }
        });


        mTextEmail = (EditText) findViewById(R.id.login_email);
        mTextPassword = (EditText) findViewById(R.id.login_password);

        mTextEmail.addTextChangedListener(afterTextChangedListener);
        mTextPassword.addTextChangedListener(afterTextChangedListener);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain("1", "1");
//            final String email = mTextEmail.getText().toString();
//            String password = mTextPassword.getText().toString();
//
//            if (!isEmailAddressValid(email)){
//                failedByEmail();
//            } else if (!isPasswordValid(password)){
//                failedByPassword();
//            } else{
//                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                            assert user != null;
//                            if(user.isEmailVerified()){
////                                SynchronizeHandler.getInstance().downloadUser(email);
////                                while(!SynchronizeHandler.getInstance().successDownLoad){}
//                                Intent MessageIntent = new Intent(LoginPage.this, HomePage.class);
//                                startActivity(MessageIntent);
//                            }
//                            else{
//                                user.sendEmailVerification();
//                                failedByNotVerified();
//                            }
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            System.out.println(task.getException().toString());
//                            failedByDismatch();
//                        }
//                    }
//                });
//
//            }

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
            String emailInput = mTextEmail.getText().toString().trim();
            String passwordInput = mTextPassword.getText().toString().trim();

            loginButton.setEnabled(!emailInput.isEmpty() && ! passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void openMain(String email, String password) {
        Intent intent = new Intent(this, HomePage.class);
        intent.putExtra(DataTag.NEW_USER_EMAIL.toString(),  email);
        intent.putExtra(DataTag.NEW_USER_PASSWORD.toString(), password);
        startActivity(intent);
    }


    private void openRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

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
            //return !password.trim().isEmpty();
        }
    }

    public void failedByEmail(){
        AlertDialog alertDialog = new AlertDialog.Builder(LoginPage.this).create();
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
        AlertDialog alertDialog = new AlertDialog.Builder(LoginPage.this).create();
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

    public void failedByDismatch(){
        AlertDialog alertDialog = new AlertDialog.Builder(LoginPage.this).create();
        alertDialog.setTitle("Dismatch Email and Password");
        alertDialog.setMessage("Email and Password are not matched, please enter again");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void failedByNotVerified(){
        AlertDialog alertDialog = new AlertDialog.Builder(LoginPage.this).create();
        alertDialog.setTitle("Account Is Not Verified");
        alertDialog.setMessage("Account Is Not Verified, We just send a " +
                "verification email to you, please go to verify it");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


}
