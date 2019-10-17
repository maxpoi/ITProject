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
       // setContentView(R.layout.activity_background_dialog);

        //TODO

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_background_dialog);
        button = (Button) dialog.findViewById(R.id.button_ok);
        dialog.show();

        // add button listener
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                /*
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_register_success);

                // set title
                TextView title = (TextView) dialog.findViewById(R.id.title);
                title.setText("Success!");

                // set text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Your password is reset successfully!");
                ImageView image = (ImageView) dialog.findViewById(R.id.image_title);
                image.setImageResource(R.drawable.ic_launcher_background);

                Button dialogButton = (Button) dialog.findViewById(R.id.button_return);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();*/

                Intent MessageIntent = new Intent(RegisterSuccessDialog.this, LoginPage.class);
                startActivity(MessageIntent);
            }
        });
    }

}
