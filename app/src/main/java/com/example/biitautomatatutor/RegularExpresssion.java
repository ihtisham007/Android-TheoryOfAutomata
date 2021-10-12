package com.example.biitautomatatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegularExpresssion extends AppCompatActivity {

    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_expresssion);
        key = getIntent().getExtras().getString("tutor_name");
    }

    public void openRegexTrySelf(View view) {
        Intent intent = new Intent(this, regextrySelf.class);
        intent.putExtra("tutor_name",key);
        startActivity(intent);
    }
}