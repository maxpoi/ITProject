package com.example.homesweethome.UI.register;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import com.example.homesweethome.R;
import com.example.homesweethome.UI.LoginPage;

public class RegisterSuccessDialog  extends AppCompatActivity {

    final Context context = this;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_background_dialog);
        button = (Button) dialog.findViewById(R.id.button_ok);
        dialog.show();

        // add button listener
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent MessageIntent = new Intent(RegisterSuccessDialog.this, LoginPage.class);
                startActivity(MessageIntent);
            }
        });
    }

}
