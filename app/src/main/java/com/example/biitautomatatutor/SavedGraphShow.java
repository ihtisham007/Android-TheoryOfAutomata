package com.example.biitautomatatutor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SavedGraphShow extends AppCompatActivity {

    TextView txtView;
    ImageView imageView;
    String SAvedRegec;
    String key="";
    String from ="";

    //Mealy
    Map<String, String> valificationFromInput = new HashMap<String, String>();
    Map<String, String> valificationFromStates = new HashMap<String, String>();
    Map<String,String> valificationFromOutput =  new HashMap<String,String>();
    ArrayList<String> uniqueWords = new ArrayList<String>();
    ArrayList<String> FinalSt = new ArrayList<String>();
    Map<String, String> dictionaryWords = new HashMap<String, String>();

    TextView forRegex;

    ArrayList<String>  ar = new ArrayList<String>();

    //Array for storing [ ] * +
    ArrayList<String> Symbol = new ArrayList<String>();

    //
    String finalStates= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_graph_show);

        imageView = (ImageView) findViewById(R.id.UniversalGraph);

        txtView = (TextView) findViewById(R.id.textView177);

        key= getIntent().getExtras().getString("tutor_name");
        SAvedRegec = getIntent().getExtras().getString("savedRegex");
        from = getIntent().getExtras().getString("fromRegex");

        String getTopic = null;

        forRegex = (TextView) findViewById(R.id.textView771);


        txtView.setText(from+" "+SAvedRegec);
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        String getRegex = getIntent().getExtras().getString("savedRegex");
        //String states = GetStatesFromRegexDFa(getRegex);


        if (from.matches("Mealy"))
        {
            AddToDictionaryState(getRegex);
            FinalSt.add("-1");
            getTopic = getRegex;
            View v = new MealyCanvasImage(SavedGraphShow.this, dictionaryWords, FinalSt, getRegex);
            Bitmap bitmap2 = Bitmap.createBitmap(2024/*width*/, 2024/*height*/, Bitmap.Config.ARGB_8888);

            Canvas canvas2 = new Canvas(bitmap2);
            v.draw(canvas2);
            imageView.setImageBitmap(bitmap2);
        }
        else if(from.matches("Nfa"))
        {
            String s = fromToStateFuncDFa(getRegex);
            String  getfinalState = GetFinalStateWordDFa(getRegex);
            View view1 = new CanvasImage(this,s,getfinalState);
            Bitmap bitmap = Bitmap.createBitmap(2024/*width*/, 2024/*height*/, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            view1.draw(canvas);
            imageView.setImageBitmap(bitmap);
        }
        else if (from.matches("Regex"))
        {
            String getRandomWOrds=GetFinalStateWord(getRegex);
            forRegex.setText(getRandomWOrds);
        }
        else if (from.matches("Dfa"))
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
                imageView.setImageBitmap(bitmap);



        }
        else
        {
            AddToDictionaryStateMoore(getRegex);
            getTopic =getRegex;
            FinalSt.add("-1");
            View v = new MooreCanvasImage(this, dictionaryWords, FinalSt, getTopic);
            Bitmap bitmap = Bitmap.createBitmap(2024/*width*/, 2024/*height*/, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            v.draw(canvas);
            imageView.setImageBitmap(bitmap);
        }
    }

    //NFA
    private String fromToStateFunc(String Regex)
    {
        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("NfaTutorial");

        PyObject objreturn=pyObject.callAttr("GetStatesNFA",Regex);

        String states = objreturn.toString();

        return states;
    }

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

    //Mealy
    private void AddToDictionaryState(String selectSpinner) {
        if (selectSpinner.equals("Beep on b (aab)")) {
            dictionaryWords.put("0", "1,a/0;0,b/0");
            dictionaryWords.put("1", "2,a/0;0,b/0");
            dictionaryWords.put("2", "3,b/1;2,a/0");
            dictionaryWords.put("3", "3,b/0;1,a/0");
            AddToinputStatesoutput(0);
        } else if (selectSpinner.equals("1 Add in input")) {
            dictionaryWords.put("0", "1,0/1;0,1/0");
            dictionaryWords.put("1", "1,1/1.0/0");
            AddToinputStatesoutput(2);
        } else if (selectSpinner.equals("Increment 2 in input")) {
            dictionaryWords.put("0", "1,1/1.0/0;");
            dictionaryWords.put("1", "1,1/0;2,0/1;");
            dictionaryWords.put("2", "2,1/1.0/0;");
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
            dictionaryWords.put("1", "1,0/1.1/0;");
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
            valificationFromStates.put("3", "1,3");

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
            valificationFromStates.put("1", "1,1");

            valificationFromOutput.put("0", "1,0");
            valificationFromOutput.put("1", "0,1");
        }
        else if(pos==3)
        {
            valificationFromInput.put("0", "0,1");
            valificationFromStates.put("0", "1,1");

            valificationFromInput.put("1", "0,1");
            valificationFromStates.put("1", "2,1");

            valificationFromInput.put("2", "0,1");
            valificationFromStates.put("2", "2,2");

            valificationFromOutput.put("0", "0,1");
            valificationFromOutput.put("1", "1,0");
            valificationFromOutput.put("2", "0,1");
        }
        else if (pos==4)
        {
            valificationFromInput.put("0", "1,0");
            valificationFromStates.put("0", "1,0");

            valificationFromInput.put("1", "0,1");
            valificationFromStates.put("1", "1,1");

            valificationFromOutput.put("0", "1,0");
            valificationFromOutput.put("1", "1,0");
        }

    }

    /*private  String inputToStates(String inputS)
    {
        if (getTopic.equals("1 Add in input") || getTopic.equals("Increment 2 in input"))
            inputS=GetReverseString(inputS);


        String states="";

        String output = "  ";
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
    }*/
    private String GetReverseString(String Strinput)
    {
        String revStrin ="";
        for(int i =Strinput.length()-1;i>-1;i--)
        {
            revStrin+=Strinput.charAt(i)+"";
        }
        return revStrin;
    }

    //Regex
    private String GetFinalStateWord(String Regex)
    {
        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("RegexToStates");

        PyObject objreturn=pyObject.callAttr("RandomRegex",Regex);

        String Endstates = objreturn.toString();

        return Endstates;
    }

    //DFa
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

    //at the last for Moore
    private void AddToDictionaryStateMoore(String selectSpinner) {
        if (selectSpinner.equals("Beep on b (aab)")) {
            dictionaryWords.put("0", "1,a/0;0,b/0");
            dictionaryWords.put("1", "2,a/0;0,b/0");
            dictionaryWords.put("2", "3,b/1;2,a/0");
            dictionaryWords.put("3", "0,b/0;1,a/0");
            AddToinputStatesoutputMoore(0);
        } else if (selectSpinner.equals("1 Add in input")) {
            dictionaryWords.put("0", "1,0/1;0,1/0");
            dictionaryWords.put("1", "2,0/0;1,1/1");
            dictionaryWords.put("2", "1,1/1;2,0/0");
            AddToinputStatesoutputMoore(2);
        } else if (selectSpinner.equals("Increment 2 in input")) {
            dictionaryWords.put("0", "1,0/0;2,1/1");
            dictionaryWords.put("1", "3,0/1;1,1/0;");
            dictionaryWords.put("2", "3,0/1;1,1/0;");
            dictionaryWords.put("3", "4,0/0;3,1/1;");
            dictionaryWords.put("4", "3,1/1;4,0/0;");
            AddToinputStatesoutputMoore(3);
        }
        else if(selectSpinner.equals("1's Complement"))
        {
            dictionaryWords.put("0", "1,1/0;0,0/1;");
            dictionaryWords.put("1", "0,0/1;1,1/0;");
            AddToinputStatesoutputMoore(1);
        }
        else if(selectSpinner.equals("2's Complement"))
        {
            dictionaryWords.put("0", "1,1/1;0,0/0;");
            dictionaryWords.put("1", "2,1/0;1,0/1;");
            dictionaryWords.put("2", "1,0/1;2,1/0;");
            AddToinputStatesoutputMoore(4);
        }

    }

    private void AddToinputStatesoutputMoore(int pos) {

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

}