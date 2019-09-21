package com.example.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterSuccessActivity extends AppCompatActivity {

    Button mButton;
    TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);

        mButton = (Button) findViewById(R.id.button_return);
        mText = (TextView) findViewById(R.id.textView1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO：Page should jump to login page (or automatic login to home page)

                Intent registerIntent = new Intent(RegisterSuccessActivity.this, MainActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}
