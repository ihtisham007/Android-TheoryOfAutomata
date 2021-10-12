package com.example.biitautomatatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MooreTrySelf extends AppCompatActivity {

    TextView inputTxt;
    TextView txtStates;
    TextView outputViewText;

    ImageView imgViewForGraph;

    Spinner getSpinner;

    ArrayList<String> uniqueWords = new ArrayList<String>();
    ArrayList<String> FinalSt = new ArrayList<String>();
    Map<String, String> dictionaryWords = new HashMap<String, String>();

    ArrayList<String> itemToSpinner = new ArrayList<String>();

    Map<String, String> valificationFromInput = new HashMap<String, String>();
    Map<String, String> valificationFromStates = new HashMap<String, String>();
    Map<String,String> valificationFromOutput =  new HashMap<String,String>();

    String getTopic = null;

    String key ="";
    int pos =-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moore_try_self);

        inputTxt = (TextView) findViewById(R.id.txtInputUser);


        imgViewForGraph = (ImageView) findViewById(R.id.imgShowGraphMoore);

        getSpinner = (Spinner) findViewById(R.id.spinnerMoore);

        AddItemTOSpinner();
        key = getIntent().getExtras().getString("tutor_name");
        txtStates = (TextView) findViewById(R.id.txtViewStates);
        outputViewText = (TextView) findViewById(R.id.txtViewOutput);

        txtStates.setText("");
        outputViewText.setText("");

        getSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getTopic = GetTopicfromLIst(position);
                AddToDictionaryState(getTopic);
                pos = position;
                FinalSt.add("-1");
                View v = new MooreCanvasImage(MooreTrySelf.this, dictionaryWords, FinalSt, getTopic);
                Bitmap bitmap = Bitmap.createBitmap(2024/*width*/, 2024/*height*/, Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(bitmap);
                v.draw(canvas);
                imgViewForGraph.setImageBitmap(bitmap);
                txtStates.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void ShowGraph(View view) {

        if (ShowOutputOrNot())
        {
            String getStates=inputToStates(inputTxt.getText().toString());
            String[] arrString = getStates.split(",");
            arrString[0]="q0 "+arrString[0];
            if (getTopic.matches("1's Complement"))
                arrString[1] ="1  "+arrString[1];
            else
                arrString[1] ="0  "+arrString[1];
            txtStates.setText(arrString[0]+"\n\n\n"+arrString[1]);
        }
    }

    private Boolean ShowOutputOrNot()
    {
        String getText = inputTxt.getText().toString();
        if (getText.length()>0)
            return true;
        return false;
    }
    private void AddItemTOSpinner() {
        //itemToSpinner.add("Beep on b (aab)");
        itemToSpinner.add("1's Complement");
        itemToSpinner.add("2's Complement");
        itemToSpinner.add("1 Add in input");
        itemToSpinner.add("Increment 2 in input");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemToSpinner);

        getSpinner.setAdapter(adapter);
    }
    private String GetTopicfromLIst(int position) {
        return itemToSpinner.get(position);
    }

    private void AddToDictionaryState(String selectSpinner) {
        if (selectSpinner.equals("Beep on b (aab)")) {
            dictionaryWords.put("0", "1,a/0;0,b/0");
            dictionaryWords.put("1", "2,a/0;0,b/0");
            dictionaryWords.put("2", "3,b/1;2,a/0");
            dictionaryWords.put("3", "0,b/0;1,a/0");
            AddToinputStatesoutput(0);
        } else if (selectSpinner.equals("1 Add in input")) {
            dictionaryWords.put("0", "1,0/1;0,1/0");
            dictionaryWords.put("1", "2,0/0;1,1/1");
            dictionaryWords.put("2", "1,1/1;2,0/0");
            AddToinputStatesoutput(2);
        } else if (selectSpinner.equals("Increment 2 in input")) {
            dictionaryWords.put("0", "1,0/0;2,1/1");
            dictionaryWords.put("1", "3,0/1;1,1/0;");
            dictionaryWords.put("2", "3,0/1;1,1/0;");
            dictionaryWords.put("3", "4,0/0;3,1/1;");
            dictionaryWords.put("4", "3,1/1;4,0/0;");
            AddToinputStatesoutput(3);
        }
        else if(selectSpinner.equals("1's Complement"))
        {
            dictionaryWords.put("0", "1,1/0;0,0/1;");
            dictionaryWords.put("1", "0,0/1;1,1/0;");
            AddToinputStatesoutput(1);
        }
        else if(selectSpinner.equals("2's Complement"))
        {
            dictionaryWords.put("0", "1,1/1;0,0/0;");
            dictionaryWords.put("1", "2,1/0;1,0/1;");
            dictionaryWords.put("2", "1,0/1;2,1/0;");
            AddToinputStatesoutput(4);
        }

    }

    private void AddToinputStatesoutput(int pos) {

        if (pos==0)
        {
            valificationFromInput.put("0", "a,b");
            valificationFromStates.put("0", "1,0");

            valificationFromInput.put("1", "a,b");
            valificationFromStates.put("1", "2,0");

            valificationFromInput.put("2", "b,a");
            valificationFromStates.put("2", "3,2");

            valificationFromInput.put("3", "a,b");
            valificationFromStates.put("3", "1,0");

            valificationFromOutput.put("0", "0,0");
            valificationFromOutput.put("1", "0,0");
            valificationFromOutput.put("2", "1,0");
            valificationFromOutput.put("3", "0,0");
        }
        else if (pos == 1){
            valificationFromInput.put("0", "1,0");
            valificationFromStates.put("0", "1,0");

            valificationFromInput.put("1", "0,1");
            valificationFromStates.put("1", "0,1");

            valificationFromOutput.put("0", "0,1");
            valificationFromOutput.put("1", "1,0");
        }
        else if (pos==2)
        {
            valificationFromInput.put("0", "0,1");
            valificationFromStates.put("0", "1,0");

            valificationFromInput.put("1", "0,1");
            valificationFromStates.put("1", "2,1");

            valificationFromInput.put("2", "1,0");
            valificationFromStates.put("2", "1,2");

            valificationFromOutput.put("0", "1,0");
            valificationFromOutput.put("1", "0,1");
            valificationFromOutput.put("2", "1,0");
        }
        else if(pos==3)
        {
            valificationFromInput.put("0", "1,0");
            valificationFromStates.put("0", "2,1");

            valificationFromInput.put("1", "0,1");
            valificationFromStates.put("1", "3,1");

            valificationFromInput.put("2", "0,1");
            valificationFromStates.put("2", "3,1");

            valificationFromInput.put("3", "0,1");
            valificationFromStates.put("3", "4,3");

            valificationFromInput.put("4", "1,0");
            valificationFromStates.put("4", "3,4");

            valificationFromOutput.put("0", "1,0");
            valificationFromOutput.put("1", "1,0");
            valificationFromOutput.put("2", "1,0");
            valificationFromOutput.put("3", "0,1");
            valificationFromOutput.put("4", "1,0");

        }
        else if (pos==4)
        {
            valificationFromInput.put("0", "1,0");
            valificationFromStates.put("0", "1,0");

            valificationFromInput.put("1", "1,0");
            valificationFromStates.put("1", "2,1");

            valificationFromInput.put("2", "0,1");
            valificationFromStates.put("2", "1,2");

            valificationFromOutput.put("0", "1,0");
            valificationFromOutput.put("1", "0,1");
            valificationFromOutput.put("2", "1,0");
        }

    }

    private  String inputToStates(String inputS)
    {
        if (getTopic.equals("1 Add in input") || getTopic.equals("Increment 2 in input"))
            inputS=GetReverseString(inputS);


        String states="";

        String output = " ";
        int i=0;
        int j = 0;
        int strItera = 0;
        while (i<inputS.length())
        {
            String input= valificationFromInput.get(j+"");
            String state = valificationFromStates.get(j+"");
            String outPut1 = valificationFromOutput.get(j+"");
            if(input.indexOf(",")>-1)
            {
                String[] inputarr = input.split(",");
                String[] statesarr = state.split(",");
                String[] outputStr = outPut1.split(",");
                String matinput = inputS.charAt(strItera)+"";
                if(matinput.equals(inputarr[0]))
                {
                    states+="q"+statesarr[0]+" ";
                    j = Integer.parseInt(statesarr[0]);
                    i+=1;
                    output +=outputStr[0]+"  ";
                }
                else
                {
                    states+="q"+statesarr[1]+" ";
                    j = Integer.parseInt(statesarr[1]);
                    output +=outputStr[1]+"  ";
                    i+=1;
                }
            }

            strItera +=1;
        }

        return states+","+output;
    }
    private String GetReverseString(String Strinput)
    {
        String revStrin ="";
        for(int i =Strinput.length()-1;i>-1;i--)
        {
            revStrin+=Strinput.charAt(i)+"";
        }
        return revStrin;
    }

    public void saveFile(View view) {
        if (pos!=-1) {
            getTopic = GetTopicfromLIst(pos);
            key += "\nMoore" + " " + getTopic;
            Intent inten = new Intent(MooreTrySelf.this, MainActivity.class);
            inten.putExtra("tutor_name", key);
            Toast.makeText(this, "saved Successfully!", Toast.LENGTH_SHORT).show();
            startActivity(inten);
        }
    }
}