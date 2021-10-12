package com.example.biitautomatatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class regextrySelf extends AppCompatActivity {

    TextView getRegex;
    TextView txtoutput;

    String key="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regextry_self);
        getRegex = (TextView) findViewById(R.id.txtregexUser);
        txtoutput = (TextView) findViewById(R.id.outputText);
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        key  = getIntent().getExtras().getString("tutor_name");
    }

    public void ValidateRegex(View view) {

        String getUserRegex = getRegex.getText().toString();

        String getRandomWOrds=GetFinalStateWord(getUserRegex);
        txtoutput.setText(getRandomWOrds);
    }

    private String GetFinalStateWord(String Regex)
    {
        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("RegexToStates");

        PyObject objreturn=pyObject.callAttr("RandomRegex",Regex);

        String Endstates = objreturn.toString();

        return Endstates;
    }

    public void saveGraph(View view) {
        String getUserRegex = getRegex.getText().toString();
        if (getUserRegex.length()>0)
        {
            Intent inten = new Intent(regextrySelf.this, MainActivity.class);
            inten.putExtra("tutor_name", key+"\n"+"Regex "+getUserRegex);
            Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
            startActivity(inten);
        }
    }
}