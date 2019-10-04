package com.example.homesweethome.UI.register;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.homesweethome.R;
public class RegisterFailActivity extends AppCompatActivity implements View.OnClickListener{

    Button mButton;
    TextView mText;
    Button mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_fail);

        mButton = (Button) findViewById(R.id.button_return);
        mButton2 = (Button) findViewById(R.id.button2);
        mText = (TextView) findViewById(R.id.textView1);

        mButton.setOnClickListener(this);
        mButton2.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_return:
                Intent intent = new Intent(RegisterFailActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.button2:
                Intent intent2 = new Intent(RegisterFailActivity.this, RegisterSuccessActivity.class);
                startActivity(intent2);
                break;

        }
    }
}

