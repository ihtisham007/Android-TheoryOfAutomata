package com.example.biitautomatatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class NfaTutorial extends AppCompatActivity {

    String saveKey = "";
    String saveValue = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfa_tutorial);

        saveKey=getIntent().getExtras().getString("tutor_name");
        saveValue=getIntent().getExtras().getString("language");
        Log.d("key",saveKey);
    }

    public void openTryNFA(View view) {
        Intent intent = new Intent(this, NfaTrySelf.class);
        //intent.putExtra("tutor_name","Nfa");

        intent.putExtra("tutor_name",saveKey);
        intent.putExtra("language",saveValue);
        startActivity(intent);
    }
}