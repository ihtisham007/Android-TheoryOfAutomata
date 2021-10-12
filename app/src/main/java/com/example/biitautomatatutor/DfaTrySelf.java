package com.example.biitautomatatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import java.util.Map;

public class DfaTrySelf extends AppCompatActivity {


    TextView txtRegex;
    ImageView showGraph;

    //array for words
    ArrayList<String>  ar = new ArrayList<String>();

    //Array for storing [ ] * +
     ArrayList<String> Symbol = new ArrayList<String>();

     //
    String finalStates= null;

    String key="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfa_try_self);
        showGraph = (ImageView) findViewById(R.id.imgShowGraph);
       /* if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }*/
        key = getIntent().getExtras().getString("tutor_name");
    }

    public void ShowGraph(View view) {


        String getRegex = GetRegexFromUser();
        if (getRegex=="")
            Toast.makeText(this, "Input Field Can't be empty", Toast.LENGTH_SHORT).show();
        else
        {

            String a = getRegex;
            String fromToWordStates= "";
            if(a.contains("(") && a.contains(")") &&( a.contains("*") || a.contains("+")) && a.contains("|"))
            {
                int index = a.indexOf("(");
                if(index>0);
                {
                    a = a.substring(0,index);
                    SaveToArr(a);
                    fromToWordStates=ArrayListToStringStates(a);

                }
                if (index==0)
                {

                }

            }
            else if(firstLastarebracNpipe(getRegex))
            {
                String getRegextemp =  getRegex.substring(1,getRegex.length()-2);
                SaveToArr(getRegextemp);
                fromToWordStates=ArrayListToStringStates(getRegextemp);
                String lastState =null;
                if (getRegextemp.length()==1)
                {
                    String lastChar = "";
                    String firstChar = getRegextemp.charAt(0)+"";
                    if(firstChar.matches("a"))
                        lastChar = "b";
                    else if(firstChar.matches("b"))
                        lastChar = "a";
                    lastState = getRegextemp.length()+":1,"+firstChar+",D "+lastChar+" "+";";

                }
                else
                {
                    String firstChar = getRegextemp.charAt(0)+"";
                    String lastChar = getRegextemp.charAt(getRegextemp.length()-1)+"";
                    lastState = getRegextemp.length()+":1,"+firstChar+",D "+lastChar+" "+";";
                }
                fromToWordStates += lastState;
                a = getRegextemp;
            }
            else if (SecondNumberFBracket(getRegex))
            {
                String regex = getRegex;

                int fbrac = regex.indexOf("(");
                System.out.println(fbrac);
                int lbrac = regex.indexOf(")");
                String forSimple = null;
                String startBrack = regex.substring(fbrac, lbrac+2);

                String fromToWordStates1 =null;
                String secondStr = null;
                if(lbrac==regex.length()-2)
                {

                    String StartsubStr = regex.substring(0, fbrac);
                    SaveToArr(StartsubStr);
                    fromToWordStates1=ArrayListToStringStates(StartsubStr);

                    secondStr = SecondCondOFBrack(regex,fbrac,lbrac);

                }
                else{
                    //System.out.println("last words  "+regex);
                }
                String a1= regex.replace("(", "");
                a1 = a1.replace(")", "");
                a1 = a1.replace("*", "");
                a1 = a1.replace("+", "");
                a =a1;
                fromToWordStates =fromToWordStates1+secondStr;
            }
            else
            {
                SaveToArr(a);
                fromToWordStates=ArrayListToStringStates(a);
                String uniqWords = "";

                fromToWordStates +=a.length()+":"+a.length()+",a b;";
            }
            getRegex = a;
            //SaveToArr(getRegex);
            //String fromToWordStates=ArrayListToStringStates(getRegex);

            GetFinalState(getRegex);
            View v = new DfaCanvasGraph(this,fromToWordStates,finalStates);
            Bitmap bitmap = Bitmap.createBitmap(2024/*width*/, 2024/*height*/, Bitmap.Config.ARGB_8888);

            Canvas canvas =  new Canvas(bitmap);
            v.draw(canvas);
            showGraph.setImageBitmap(bitmap);


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
/*

*/
    //New Dfa Setting
    //Saving symbol that used further
    private  void insertSymbol()
    {
        Symbol.add("(");
        Symbol.add(")");
        Symbol.add("*");
        Symbol.add("+");
        Symbol.add("|");
    }

    //if word is then get other word
    private  String GetExceptWords(String word)
    {
        String returnWord = "";
        for(int i=0;i<ar.size();i++)
        {
            if(!ar.get(i).matches(word))
                returnWord += ar.get(i)+" ";
        }
        return returnWord;
    }

    private  String ArrayListToStringStates(String Regextostates)
    {

        String states="";
        String previousChar = Regextostates.charAt(0)+"";
        for(int i=0;i<Regextostates.length();i++)
        {

            previousChar = Regextostates.charAt(i)+"";

            String toDeadState=GetExceptWords(previousChar);

            if(toDeadState.length()>0)
                states+=""+i+":"+""+(i+1)+","+previousChar+",D "+toDeadState+";";
            else
                states+=""+i+":"+""+(i+1)+","+previousChar+";";

        }
        return states;
    }

    private  void SaveToArr(String a)
    {
        ar.clear();
        ar.add("a");
        ar.add("b");
        for(int i=0;i<a.length();i++)
        {
            char character = a.charAt(i);


            String aa = ""+character;

            Boolean getAns=ar.contains(aa);
            if(!getAns && !Symbol.contains(aa))
                ar.add(aa);

        }
    }

    private void GetFinalState(String words)
    {
        int finAllength = words.length();

        finalStates=""+finAllength;
    }

    private static Boolean firstLastarebracNpipe(String regex)
    {
        int fbrack = regex.indexOf('(');
        int lbrack = regex.indexOf(')');
        int kleenstar = regex.indexOf('*');
        int plus = regex.indexOf('+');
        if (fbrack==0 && lbrack==regex.length()-2 && (kleenstar==regex.length()-1||plus==regex.length()-1))
        {
            return true;
        }
        return false;
    }
    private Boolean SecondNumberFBracket(String regex)
    {
        int fbrack = regex.indexOf('(');
        int lbrack = regex.indexOf(')');
        int kleenstar = regex.indexOf('*');
        int plus = regex.indexOf('+');
        int pipeIndex = regex.indexOf("|");
        if (fbrack>=0 && pipeIndex==-1 && lbrack>1 && (((kleenstar<regex.length()-1 || kleenstar==regex.length()-1 ) || (plus<regex.length()-1 || plus==regex.length()-1))))
        {
            return true;
        }
        return false;

    }
    private  String SecondCondOFBrack(String getRegex,int startIndex,int endIndex)
    {



        int starIndex = getRegex.indexOf("*");
        int plusIndex = getRegex.indexOf("+");
        String sign = "*";
        if (plusIndex>-1)
            sign = "+";

        String SubForWords = getRegex.substring(startIndex+1, endIndex);
        System.out.println(SubForWords);
        String StatesAddition = "";

        if(SubForWords.length()==1)
        {
            StatesAddition+=startIndex+":"+(startIndex+1)+","+SubForWords+",D b ;"+";3:3,a,D b ;";
        }
        else
        {

        }


        return StatesAddition;
    }


    public void saveFIle(View view) {
        String getRegex = GetRegexFromUser();
        if(getRegex.length()>0)
        {
            Intent inten = new Intent(DfaTrySelf.this, MainActivity.class);
            inten.putExtra("tutor_name", key+"\n"+"Dfa"+" "+getRegex);
            Toast.makeText(this, "saved Successfully!", Toast.LENGTH_SHORT).show();
            startActivity(inten);
        }
    }
    private int returnLength()
    {
        String temVar = key;

        return 0;
    }
}