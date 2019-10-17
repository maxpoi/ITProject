package com.example.homesweethome.UI.register;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.homesweethome.UI.LoginPage;
import com.example.homesweethome.R;

import static com.example.homesweethome.R.layout.activity_background_dialog;


public class ResetPasswordActivity extends AppCompatActivity {
    private MutableLiveData<RegisterFormState> mRegisterFormState = new MutableLiveData<>();
    EditText mTextPassword;
    EditText mTextCnfPassword;
    Button reset_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mTextPassword = (EditText) findViewById(R.id.register_password);
        mTextCnfPassword = (EditText) findViewById(R.id.register_cfn_password);
        reset_button = (Button) findViewById(R.id.button_register);

        mTextPassword.addTextChangedListener(afterTextChangedListener);
        mTextCnfPassword.addTextChangedListener(afterTextChangedListener);
        /*reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = mTextPassword.getText().toString();
                String cnfPassword = mTextCnfPassword.getText().toString();
                if (!isPasswordValid(password)){
                    failedByPassword();
                } else if (!isCnfPasswordValid(password, cnfPassword)){
                    failedByCnfPassword();
                } else{
                    password_reset();
                    // TODO: store renewed password  into database
                    //Intent MessageIntent = new Intent(ResetPasswordActivity.this, RegisterSuccessActivity.class);
                    //startActivity(MessageIntent);
                }
            }
        });*/


        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = mTextPassword.getText().toString();
                String cnfPassword = mTextCnfPassword.getText().toString();
                if (!isPasswordValid(password)){
                    failedByPassword();
                } else if (!isCnfPasswordValid(password, cnfPassword)){
                    failedByCnfPassword();
                } else{
                    password_reset();
                    // TODO: store renewed password  into database
                    //Intent MessageIntent = new Intent(ResetPasswordActivity.this, RegisterSuccessActivity.class);
                    //startActivity(MessageIntent);
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
            String passwordInput = mTextPassword.getText().toString().trim();
            String cnfPasswordInput = mTextCnfPassword.getText().toString().trim();

            reset_button.setEnabled(!passwordInput.isEmpty() && !cnfPasswordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
            //check validity
            //registerDataChanged(mTextPassword.getText().toString(), mTextCnfPassword.getText().toString());
        }
    };

    public void registerDataChanged(String password, String repassword) {
        if (!isPasswordValid(password)) {
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

    public void failedByPassword(){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invalid Password");
        builder.setMessage("Password is invalid, please enter a password with length 6-20 contains only digits and letters.");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();


        /*
        AlertDialog alertDialog = new AlertDialog.Builder(ResetPasswordActivity.this).create();
        alertDialog.setTitle("Invalid Password");
        alertDialog.setMessage("Password is invalid, please enter a password with length 6-20 contains only digits and letters.");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        final Button button = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) button.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER;
        button.setLayoutParams(positiveButtonLL);
        //positiveButtonLL.height = '';
        //button.setBackgroundColor(12);
        //button.setTextColor(60);
*/
    }

    public void failedByCnfPassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unmatched Password");
        builder.setMessage("The password you input does not match each other.");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        /*
        AlertDialog alertDialog = new AlertDialog.Builder(ResetPasswordActivity.this).create();
        alertDialog.setTitle("Unmatched Password");
        alertDialog.setMessage("The password you input does not match each other.");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();*/
    }

    public void password_reset(){

        final Dialog dialog = new Dialog(ResetPasswordActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(activity_background_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text);
        //text.setText("Message");

        ImageView image = (ImageView) dialog.findViewById(R.id.image_title);
        Drawable myDrawable = getResources().getDrawable(R.drawable.ic_launcher_background);
        //image.setImageResource(R.drawable.img_2);

        Resources resources = getResources();
        image.setImageDrawable(resources.getDrawable(R.drawable.img_2));

        Button dialogButton1 = (Button) dialog.findViewById(R.id.button_ok);
        dialogButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent MessageIntent = new Intent(getBaseContext(), LoginPage.class);
                    startActivity(MessageIntent);
                    //dialog.dismiss();
                }
        });
        dialog.show();


    /*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Successfully Reset Password");
        builder.setMessage("Your password is reset successfully.");
        builder.setPositiveButton("Return", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        */
    }
}
