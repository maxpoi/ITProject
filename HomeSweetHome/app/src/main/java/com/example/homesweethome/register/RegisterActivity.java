package com.example.homesweethome.register;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.homesweethome.Client;
import com.example.homesweethome.LoginPage;
import com.example.homesweethome.R;

public class RegisterActivity extends AppCompatActivity {
    private MutableLiveData<RegisterFormState> mRegisterFormState = new MutableLiveData<>();
    //private RegisterFormState registerFormState = new RegisterFormState(null, null, null);
    EditText mTextEmail;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    Button mButtonRegister;
    TextView mTextViewLogin;
    TextView mTextViewForgetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
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
        mTextViewLogin = (TextView) findViewById(R.id.textView1);
        mTextViewForgetPassword = (TextView) findViewById(R.id.textView2);

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
                } else if (!isEmailAddressExist(email)){
                    failedByEmailExisting();
                }else if (!isPasswordValid(password)){
                    failedByPassword();
                    //Intent MessageIntent = new Intent(RegisterActivity.this, RegisterFailActivity.class);
                    //startActivity(MessageIntent);
                } else if (!isCnfPasswordValid(password, cnfPassword)){
                    failedByCnfPassword();
                    //Intent MessageIntent = new Intent(RegisterActivity.this, RegisterFailActivity.class);
                    //startActivity(MessageIntent);
                } else{
                    // TODO: store input register data into database
                    Client.getInstance().createAccount(email,email,password);

                    Intent MessageIntent = new Intent(RegisterActivity.this, RegisterSuccessActivity.class);
                    startActivity(MessageIntent);
                }
            }
        });



        // Jump to forget-password page
        mTextViewForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(RegisterActivity.this, RetrievePasswordActivity.class);
                startActivity(registerIntent);
            }
        });
        // Jump to forget-password page
        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(RegisterActivity.this, LoginPage.class);
                startActivity(registerIntent);
            }
        });


        /*passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });*/



    }
/*
    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
*/

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


    private boolean isEmailAddressExist(String email) {
        if (email == null) {
            return false;
        }
        if (Client.getInstance().checkAccountExist(email).equals("false")) {
            return true;
        }
        else {
            return false;
        }
    }

    // A placeholder username validation check
    private boolean isEmailAddressValid(String email) {
        /*if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return true;
        }
        else {
            return false;
        }*/
        return true;
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
            //return !password.trim().isEmpty();
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
        alertDialog.setMessage("???Email is invalid, please enter a valid email.");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void failedByEmailExisting(){
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        alertDialog.setTitle("Email Existed");
        alertDialog.setMessage("Email is existed, please retrieve password or change to a new email.");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //Intent registerIntent = new Intent(RegisterActivity.this, RetrievePasswordActivity.class);
                        //startActivity(registerIntent);
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
