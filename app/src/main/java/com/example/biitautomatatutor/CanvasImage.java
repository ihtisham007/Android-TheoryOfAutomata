package com.example.biitautomatatutor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanvasImage extends View {

    //Regex from user to states
    String GetWordReg=null;
    //End States
    String GetEndStates = null;
    //check from state
    String currentState = null;
    //Array for states
    ArrayList<Integer> fromState = new ArrayList<Integer>();
    ArrayList<Integer> ToStates = new ArrayList<Integer>();
    ArrayList<String>  word     = new ArrayList<String>();
    //Next State Execution
    ArrayList<String> nextStateRun = new ArrayList<String>();
    //End States word
    ArrayList<Integer> endWords = new ArrayList<Integer>();

    Map<String, String> dictionaryforStates = new HashMap<String, String>();
    Map<String, String> stateDimensinDict = new HashMap<String, String>();
    //Constructor Getting Regex
    public CanvasImage(Context context,String UserRegex,String endStates)
    {
        super(context);
        GetWordReg = UserRegex;
        GetEndStates = endStates;
    }
    //onDraw used to draw canvas as image
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        Paint pBackground = new Paint();
        pBackground.setColor(Color.BLACK);

        Paint LoopCircle =  new Paint();
        LoopCircle.setColor(Color.BLUE);

        Paint pText = new Paint();
        pText.setColor(Color.BLACK);
        pText.setTextSize(40);

        Paint line =  new Paint();
        line.setStyle(Paint.Style.STROKE);
        line.setStrokeWidth(5);
        line.setColor(Color.BLACK);

        //Appending words to arrays
        SetStates(GetWordReg);
        //Appending Ending States
        AppendEndStates(GetEndStates);

        Bitmap emptyCircle = BitmapFactory.decodeResource(getResources(), R.drawable.emptycircle);

        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.startcirclearrow);

        Bitmap finalstate = BitmapFactory.decodeResource(getResources(), R.drawable.finalstates);

        Bitmap looparrow = BitmapFactory.decodeResource(getResources(), R.drawable.itselfloop);

        Bitmap trianglearrow = BitmapFactory.decodeResource(getResources(),R.drawable.trinaglearrow );

        //Appending Dictionary from states words
        for (int i=0;i<fromState.size();i++)
        {

            if (dictionaryforStates.containsKey("q"+fromState.get(i))!=true)
            {
                String valueofDict = ValueToDictionary(fromState.get(i),i);
                dictionaryforStates.put("q"+fromState.get(i),valueofDict);
            }

        }


        canvas.drawBitmap(img,0,460,pBackground);
        canvas.drawBitmap(emptyCircle,100,430,pBackground);

        canvas.drawText("q0", 130, 485, pText);

        String getValue = dictionaryforStates.get("q0");
        String[] arr = getValue.split(";");

        stateDimensinDict.put("q0","100,434");
        currentState = "q0";
        for (int i =0;i<arr.length;i++)
        {
            String[] arr2 = arr[i].split(",");
            Float[] Dimension =convertStringtoDimension("q0");
            if (i==0)
            {

                stateDimensinDict.put(arr2[0],Dimension[0]+200+","+Dimension[1]);
            }
            else if (i==1)
            {
                stateDimensinDict.put(arr2[0],"300,234");
            }
            else if(i==2)
            {
                stateDimensinDict.put(arr2[0],"300,634");
            }
            else
            {

            }

            String getDimension = stateDimensinDict.get(arr2[0]);
            String[] getDimensionarr  = getDimension.split(",");

            float left = Float.parseFloat(getDimensionarr[0]);
            float top = Float.parseFloat(getDimensionarr[1]);

            Boolean getans=IsitfinalState(arr2[0]);
            if (getans)
                canvas.drawBitmap(finalstate,left,top,pBackground);
            else
                canvas.drawBitmap(emptyCircle,left,top,pBackground);
            DrawTextinStates(left,top,arr2[0],arr2[1],currentState,canvas,pText);

            if (dictionaryforStates.containsKey(arr2[0]))
                nextStateRun.add(arr2[0]);

            Drawline("q0",arr2[0],canvas,line);
        }

        //Iteration from q1 state to end state
        int itera=0;
        while (itera<nextStateRun.size())
        {
            //Get State name for excution
            String fromState = nextStateRun.get(itera);
            //Get key value mean //"q1" : key will get "q2,b;q2,b"
            if (dictionaryforStates.containsKey(fromState)) {
                String getValue2 = dictionaryforStates.get(nextStateRun.get(itera));
                //
                String[] arr3 = getValue2.split(";");
                createMultipleStatesfromkey(arr3,fromState);
                for (int i = 0; i < arr3.length; i++) {
                    String[] arr4 = arr3[i].split(",");
                    String toStateName = arr4[0];
                    String toStateword = arr4[1];

                    if (fromState.matches(toStateName))
                        drawloop(canvas,pBackground,looparrow,toStateName,toStateword,pText);

                    Boolean ans = stateDimensinDict.containsKey(toStateName);
                    if(ans == false)
                    {

                        String getDix = stateDimensinDict.get(fromState);
                        String[] arrgetDix = getDix.split(",");
                        float x = Float.parseFloat(arrgetDix[0]);
                        float y = Float.parseFloat(arrgetDix[1]);

                        x += 200;

                        Boolean getans = IsitfinalState(toStateName);
                        if (getans)
                            canvas.drawBitmap(finalstate, x, y, pBackground);
                        else
                            canvas.drawBitmap(emptyCircle, x, y, pBackground);

                        DrawTextinStates2(x, y, toStateName, toStateword, fromState, canvas, pText);
                        stateDimensinDict.put(toStateName,x+","+y);
                        if (dictionaryforStates.containsKey(toStateName))
                            nextStateRun.add(toStateName);

                        Drawline(fromState,toStateName,canvas,line);
                    }
                    else
                    {

                        String getDix = stateDimensinDict.get(toStateName);
                        String[] arrgetDix = getDix.split(",");
                        float x = Float.parseFloat(arrgetDix[0]);
                        float y = Float.parseFloat(arrgetDix[1]);



                        Boolean getans = IsitfinalState(toStateName);
                        if (getans)
                            canvas.drawBitmap(finalstate, x, y, pBackground);
                        else
                            canvas.drawBitmap(emptyCircle, x, y, pBackground);
                        DrawTextinStates2(x, y, toStateName, toStateword, fromState, canvas, pText);
                        Drawline(fromState,toStateName,canvas,line);

                    }

                }
                itera += 1;
            }
        }


    }
    private void SetStates(String States)
    {
        States = States.replaceAll(" ","");
        String[] st = States.split(";");
        for (int i=0;i<st.length;i++)
        {
            String[] st2 = st[i].split(",");
            if (i!=0)
            {
                fromState.add(Integer.parseInt(st2[1]));
                ToStates.add(Integer.parseInt(st2[2]));
                word.add(st2[3]);
            }
            else
            {
                fromState.add(Integer.parseInt(st2[0]));
                ToStates.add(Integer.parseInt(st2[1]));
                word.add(st2[2]);
            }
        }
    }
    private  void AppendEndStates(String wordsstr)
    {
        if (wordsstr.length()==1)
            endWords.add(Integer.parseInt(wordsstr));
        else
        {
            String[] arrwords = wordsstr.split(",");
            for (int i =0;i<arrwords.length;i++)
            {
                Integer w = Integer.parseInt(arrwords[i]);
                if (w!=0)
                    endWords.add(w);
            }
        }
    }
    private String ValueToDictionary(int fromWorld,int index)
    {
        String GetValus="";

        for (int j=index;j<fromState.size();j++)
        {
            if (fromState.get(j) ==fromWorld)
            {
                GetValus += "q"+ ToStates.get(j)+","+word.get(j)+";";
            }
        }
        //in case of null
        if (GetValus=="")
        {
            GetValus += "q"+ ToStates.get(index)+","+word.get(index)+";";
        }
        return GetValus;
    }
    //From State to State line
    private void Drawline(String fromState,String toState,Canvas canvas,Paint paint)
    {
        Float[] fromSPoint = convertStringtoDimension(fromState);
        Float[] toSPoint = convertStringtoDimension(toState);
        canvas.drawLine(fromSPoint[0]+90,fromSPoint[1]+50,toSPoint[0],toSPoint[1]+50,paint);
        canvas.drawCircle(toSPoint[0],toSPoint[1]+50,8,paint);

    }
    private void DrawTextinStates(float xaxis,float yaxis,String textWrite,String textWord,String currS,Canvas canvas,Paint t)
    {
        canvas.drawText(textWrite,xaxis+40,yaxis+64,t);

        String getDix = stateDimensinDict.get(currS);
        String[] arrgetDix = getDix.split(",");
        float x = Float.parseFloat(arrgetDix[0]);
        float y = Float.parseFloat(arrgetDix[1]);

        canvas.drawText(textWord,x+xaxis/2,yaxis+50,t);

    }
    private void DrawTextinStates2(float xaxis,float yaxis,String textWrite,String textWord,String currS,Canvas canvas,Paint t)
    {
        canvas.drawText(textWrite,xaxis+40,yaxis+64,t);

        String getDix = stateDimensinDict.get(currS);
        String[] arrgetDix = getDix.split(",");
        float x = Float.parseFloat(arrgetDix[0]);
        float y = Float.parseFloat(arrgetDix[1]);

        canvas.drawText(textWord,x+250/2,yaxis+40,t);

    }
    private void drawloop(Canvas canvas,Paint paint,Bitmap loopdraw,String state,String word,Paint pText)
    {
        Float arr[]= convertStringtoDimension(state);
        canvas.drawBitmap(loopdraw,arr[0]+30,arr[1]-30,paint);
        canvas.drawText(word,arr[0]+5,arr[1]-10,pText);
    }
    //Check state my divide into futher
    private Boolean CheckForStateDivision(String[] arrStates , String key)
    {
        Boolean isReturnAble = true;
        if (arrStates.length>1) {
            for (int i = 0; i < arrStates.length; i++) {
                String[] getState = arrStates[i].split(",");
                if (getState[0].matches(key))
                    return false;
            }
        }
        else
            return false;
        return  true;
    }
    //First check it it true then create states
    private  void createMultipleStatesfromkey(String[] arrStates,String key)
    {
        Boolean isCreateable= CheckForStateDivision(arrStates,key);
        if (isCreateable)
        {
            Float[] arrofcoord = convertStringtoDimension(key);
            for(int i =0;i<arrStates.length;i++)
            {
                String[] getstate = arrStates[i].split(",");
                String ToStateName = getstate[0];
                if (stateDimensinDict.containsKey(ToStateName))
                    continue;
                else
                {
                    if (i==0)
                    {
                        arrofcoord[0]+=200;
                    }
                    else if (i==1)
                    {
                        //arrofcoord[0]+=60;
                        arrofcoord[1]-=200;
                    }
                    else
                    {
                        arrofcoord[1]+=200;
                        if (stateDimensinDict.containsValue(arrofcoord[0]+","+arrofcoord[1]))
                            arrofcoord[1]+=200;
                        //arrofcoord[0]+=60;

                    }
                    stateDimensinDict.put(ToStateName,arrofcoord[0]+","+arrofcoord[1]);
                    if (dictionaryforStates.containsKey(ToStateName))
                        nextStateRun.add(ToStateName);
                }
            }

        }
    }
    //Convert String to Dimension for states x-axis and y-axis
    private  Float[] convertStringtoDimension(String key)
    {
        String getDimension = stateDimensinDict.get(key);
        String[] splitDimension = getDimension.split(",");
        Float x_axis = Float.parseFloat(splitDimension[0]);
        Float y_axis = Float.parseFloat(splitDimension[1]);
        Float[] arrayofCoord = new Float[] {x_axis,y_axis};

        return  arrayofCoord;

    }
    //Check if it is final States
    private  Boolean IsitfinalState(String stN)
    {
        for (int i=0;i<endWords.size();i++)
        {
            String getEndS = "q"+endWords.get(i);
            if (getEndS.matches(stN))
                return true;


        }
        return false;
    }
    //Check States have  a loop
    private Boolean Haveitaloop(String stateName)
    {
        for (int i=0;i<fromState.size();i++)
        {
            String fromS = "q"+fromState.get(i);
            String toS   = "q"+ToStates.get(i);
            if (fromS.matches(toS))
                return true;
        }
        return false;
    }
}
