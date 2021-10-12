package com.example.biitautomatatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MainActivity extends AppCompatActivity {

    TextView txtView;

    String saveKey = "";
    String saveValue = "";
    int a= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //obj.getCharSequenceExtra("language").toString();
        //Log.d("as",obj.getCharSequenceExtra("language").toString());
            if (getIntent().hasExtra("tutor_name"))
            {
                saveKey = getIntent().getExtras().getString("tutor_name");
                saveValue = getIntent().getExtras().getString("language");
            }


    }

    public void DfaTutorial(View view) {
        Intent intent = new Intent(this, DfaTutorial.class);
        intent.putExtra("tutor_name",saveKey);
        startActivity(intent);

    }

    public void NfaTutorial(View view) {
        Intent intent = new Intent(this, NfaTutorial.class);
        intent.putExtra("tutor_name",saveKey);
        startActivity(intent);
    }

    public void MealyTutorial(View view) {
        Intent intent = new Intent(this, MealyTutorial.class);
        intent.putExtra("tutor_name",saveKey);
        startActivity(intent);
    }

    public void MooreTutorial(View view) {
        Intent intent = new Intent(this, MooreTutorial.class);
        intent.putExtra("tutor_name",saveKey);
        startActivity(intent);
    }

    public void RegexTutorial(View view) {
        Intent intent = new Intent(this, RegularExpresssion.class);
        intent.putExtra("tutor_name",saveKey);
        startActivity(intent);
    }

    public void savedGraph(View view) {
        Intent intent = new Intent(this, SavedGraph.class);
        intent.putExtra("tutor_name",saveKey);
        intent.putExtra("language",saveValue);
        startActivity(intent);
    }
}