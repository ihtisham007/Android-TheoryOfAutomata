package com.example.biitautomatatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateTimePatternGenerator;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Wrapper;

public class SavedGraph extends AppCompatActivity {

    String saveKey = "";
    String saveValue = "";

    TextView txtView;
    Button btn12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_graph);

        saveKey= getIntent().getExtras().getString("tutor_name");
        saveValue=getIntent().getExtras().getString("language");

        txtView = (TextView) findViewById(R.id.showOUptut);
        txtView.setText("");
        //btn12 = (Button) findViewById(R.id.asd12);

        //txtView.setText(saveKey);
        String temp = saveKey;
        //temp = readFromFile(this);

        LinearLayout lin  = (LinearLayout) findViewById(R.id.linearLayout);
        TextView txtView = (TextView) findViewById(R.id.asd1);
        //Button btn =new Button(this);
        if (temp.length()>0)
        {
            String[] arr =  temp.split("\n");
            for (int i =0;i<arr.length;i++)
            {

                if (arr[i].length()>0)
                {
                    Button btn =new Button(this);
                    btn.setText(arr[i]);
                    //btn.setLayoutParams(txtView.getLayoutParams());
                    int finalI = i;
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           Toast.makeText(SavedGraph.this, arr[finalI], Toast.LENGTH_SHORT).show();
                           String[] strArr =  arr[finalI].split(" ");
                           int indexSpace = arr[finalI].indexOf(' ');
                           String key=arr[finalI].substring(0,indexSpace);
                           String value =arr[finalI].substring(indexSpace+1,arr[finalI].length());
                           ShowGraph(key,value);
                        }
                    });
                    lin.addView(btn);
                }

            }
        }



    }

    public void GotoHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("tutor_name",saveKey);
        intent.putExtra("language",saveValue);
        startActivity(intent);
    }

    private void ShowGraph(String key,String value)
    {
        Intent intent = new Intent(SavedGraph.this, SavedGraphShow.class);

        intent.putExtra("tutor_name",saveKey);
        intent.putExtra("savedRegex",value);
        intent.putExtra("fromRegex",key);
        startActivity(intent);

    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("saveData.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}