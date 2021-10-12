package com.example.biitautomatatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class NfaTrySelf extends AppCompatActivity {

    TextView txtRegex;
    ImageView showGraph;

    Spinner setTextSpinnner;

    String nullOrNot;

    TextView txtView;
    String saveKey = "";
    String saveValue = "";
    ArrayList<String> itemToSpinner = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfa_try_self);
        showGraph = (ImageView) findViewById(R.id.imgShowGraphNFa);
        setTextSpinnner  = (Spinner) findViewById(R.id.spinner3);


        txtView = (TextView) findViewById(R.id.viewText);

        AddItemTOSpinner();
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        //nullOrNot ="Not Null";
        setTextSpinnner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*if (position==1)
                    nullOrNot = "Null";
                else*/
                    nullOrNot = "Not null";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveKey= getIntent().getExtras().getString("tutor_name");
        saveValue=getIntent().getExtras().getString("language");


    }

    public void ShowGraph(View view) {

        String getRegex = GetRegexFromUser();
        if (getRegex=="")
            Toast.makeText(this, "Input Field Can't be empty", Toast.LENGTH_SHORT).show();
        else {
            String s =null;
            String getfinalState=null;
            String startState=null;
            if (nullOrNot.equals("Null"))
            {
                 s = fromToStateFunc(getRegex);
                String[] strArr = s.split("&");
                s = strArr[0];

                String[] startarr = strArr[1].split("=");
                String[] endarr = strArr[2].split("=");

                getfinalState = startarr[1];

                startState = endarr[1];

                View view1 = new NfaCanvas(this, s, getfinalState,startState);
                Bitmap bitmap = Bitmap.createBitmap(2024/*width*/, 2024/*height*/, Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(bitmap);
                view1.draw(canvas);
                showGraph.setImageBitmap(bitmap);
            }
            else
            {
                String states = GetStatesFromRegexDFa(getRegex);
                s = fromToStateFuncDFa(getRegex);
                getfinalState = GetFinalStateWordDFa(getRegex);
                View view1 = new CanvasImage(this,s,getfinalState);
                Bitmap bitmap = Bitmap.createBitmap(2024/*width*/, 2024/*height*/, Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(bitmap);
                view1.draw(canvas);
                showGraph.setImageBitmap(bitmap);
            }

            /*try {
                SaveToFile(getRegex);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
            writeToFile(saveKey,this);

        }

    }

    private String GetRegexFromUser()
    {
        txtRegex = (TextView) findViewById(R.id.txtregexUser);
        String Rgex = txtRegex.getText().toString();
        if (Rgex.length()>0)
            return Rgex;
        else
            return "";
    }
    private  String GetStatesFromRegex(String Regex)
    {
        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("NfaTutorial");

        PyObject objreturn=pyObject.callAttr("returnNumberNFA",Regex);

        String states = objreturn.toString();

        return states;
    }
    private String fromToStateFunc(String Regex)
    {
        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("NfaTutorial");

        PyObject objreturn=pyObject.callAttr("GetStatesNFA",Regex);

        String states = objreturn.toString();

        return states;
    }
    private String GetFinalStateWord(String Regex)
    {
        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("NfaTutorial");

        PyObject objreturn=pyObject.callAttr("endStateNFA",Regex);

        String Endstates = objreturn.toString();

        return Endstates;
    }
    private String GetStartState(String Regex)
    {
        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("NfaTutorial");

        PyObject objreturn=pyObject.callAttr("StartStateNfa",Regex);

        String Endstates = objreturn.toString();

        return Endstates;
    }

    //Get States from Regex
    private  String GetStatesFromRegexDFa(String Regex)
    {
        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("RegexToStates");

        PyObject objreturn=pyObject.callAttr("returnNumber",Regex);

        String states = objreturn.toString();

        return states;
    }
    private String fromToStateFuncDFa(String Regex)
    {
        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("RegexToStates");

        PyObject objreturn=pyObject.callAttr("GetStates",Regex);

        String states = objreturn.toString();

        return states;
    }
    private String GetFinalStateWordDFa(String Regex)
    {
        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("RegexToStates");

        PyObject objreturn=pyObject.callAttr("endState",Regex);

        String Endstates = objreturn.toString();

        return Endstates;
    }

    private void AddItemTOSpinner() {
        itemToSpinner.add("With Null States");
        itemToSpinner.add("Without Null States");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemToSpinner);

        setTextSpinnner.setAdapter(adapter);
    }


    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("saveData.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void saveTofile(View view) {
        String getLanguage = GetRegexFromUser();
        if (getLanguage.length()>0)
        {
            Intent inten = new Intent(NfaTrySelf.this, MainActivity.class);
            if (saveKey.length()==0) {

                inten.putExtra("tutor_name", "Nfa"+" "+getLanguage);
                inten.putExtra("language", getLanguage);

            }
            else
            {
                saveKey += "\n"+"Nfa"+" "+getLanguage;
                saveValue += "\n"+getLanguage;

                inten.putExtra("tutor_name", saveKey);
                inten.putExtra("language", saveValue);
            }
            Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
            startActivity(inten);

        }

    }
}