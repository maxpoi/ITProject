package com.example.homesweethome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain();
            }
        });

        final Button registerButtobn = findViewById(R.id.register_button);
        registerButtobn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });
    }

    private void openMain() {
        Intent intent = new Intent(this, com.example.homesweethome.Home.class);
        startActivity(intent);
    }

    private void openGrid() {
        Intent intent = new Intent(this, com.example.homesweethome.Grid_image.class);
        startActivity(intent);
    }

    private void openRegister() {
        Intent intent = new Intent(getApplicationContext(), com.example.homesweethome.Register.class);
        startActivity(intent);
    }
}
