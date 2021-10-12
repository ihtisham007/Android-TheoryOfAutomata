package com.example.biitautomatatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MooreTutorial extends AppCompatActivity {

    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moore_tutorial);
        key = getIntent().getExtras().getString("tutor_name");
    }

    public void openMooreTrySelf(View view) {
        Intent intent = new Intent(this, MooreTrySelf.class);
        intent.putExtra("tutor_name",key);
        startActivity(intent);
    }
}