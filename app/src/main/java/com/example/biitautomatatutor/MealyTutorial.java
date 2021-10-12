package com.example.biitautomatatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MealyTutorial extends AppCompatActivity {

    String key="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mealy_tutorial);
        key = getIntent().getExtras().getString("tutor_name");
    }

    public void openMealyTrySelf(View view) {
        Intent intent = new Intent(this, MealyTrySelf.class);
        intent.putExtra("tutor_name",key);
        startActivity(intent);
    }
}