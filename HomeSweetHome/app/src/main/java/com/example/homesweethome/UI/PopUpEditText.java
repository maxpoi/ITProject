package com.example.homesweethome.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.homesweethome.ArtifactDatabase.Entities.Artifact;
import com.example.homesweethome.ArtifactDatabase.Entities.Image;
import com.example.homesweethome.HelperClasses.DataTag;
import com.example.homesweethome.R;
import com.example.homesweethome.ViewModels.ArtifactViewModel;

import java.util.List;

public class PopUpEditText extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_edit_text);

        final String input = getIntent().getStringExtra(DataTag.INPUT_TEXT.toString());

        final EditText editText = findViewById(R.id.popup_edit_text);
        editText.setText(input);

        Button save = findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPage.class);
                intent.putExtra(DataTag.INPUT_TEXT.toString(), editText.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
