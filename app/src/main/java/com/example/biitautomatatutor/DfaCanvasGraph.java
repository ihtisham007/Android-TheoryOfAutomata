package com.example.biitautomatatutor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DfaCanvasGraph  extends View {

    Paint StateCirle = new Paint();

    Paint textPaint =  new Paint();

    Paint arrowFillCircle =  new Paint();
    //Get Regex from User
    String RegeDfa=null;

    //
    String FinalState= null;
    // Single circle radius
    float singleCircleRadius = 70;

    float DifferenceBetweenStates = 250;
    //final state for double circle
    float doubleCircleRadius =100;

    //small circle used for arrow
    float circleRadiusArrow = 14;

    //dictionary for states and for dimension
     Map<String, String> dictionaryforStates = new HashMap<String, String>();
    //From states to Dead state
    Map<String,String> toDeadState = new HashMap<String,String>();

    //For states Dimension x,y coordinate
    Map<String,float[]> StateDimension =  new HashMap<String,float[]>();

    //Next State execution
    ArrayList<String> nextStateExe = new ArrayList<String>();
    //More Point join on Dead State
    ArrayList<float[]> DstateMorePoints = new ArrayList<float[]>();

    //it's self loop
    Bitmap looparrow = BitmapFactory.decodeResource(getResources(), R.drawable.loopitself);

    public DfaCanvasGraph(Context context,String StatesRegex,String FinalS) {
        super(context);
        RegeDfa = StatesRegex;
        FinalState = FinalS;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //single Circle
        //Setting up Paint for states Circle

        StateCirle.setColor(Color.BLACK);
        StateCirle.setStrokeWidth(8);
        StateCirle.setStyle(Paint.Style.STROKE);

        //Setting up paint for Text in side Circle
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(80);

        //instead of arrow cicle fill black
        arrowFillCircle.setColor(Color.BLACK);


        //First Step to set Dictionary for States and To end State
        SetBothDicState_Dead(RegeDfa);
        //First State is q0
        nextStateExe.add("0");

        //DrawLineFor Start
        canvas.drawLine(0,400,236,400,StateCirle);
        canvas.drawCircle(226,400,circleRadiusArrow,arrowFillCircle);
        canvas.drawText("Start",30,390,textPaint);

        //Dimension for states default q0 and D (Dead state)
        AddNewDimensionForState("0",300,400);
        AddNewDimensionForState("D", 800, 900);
        if (toDeadState.size()>0) {

            insertTextintoCirlcle("D",canvas);
            canvas.drawCircle(800,900,singleCircleRadius,StateCirle);
        }

        canvas.drawCircle(300,400,singleCircleRadius,StateCirle);
        insertTextintoCirlcle("0",canvas);
        String toDeadS = null;

        int itera_i = 0;
        while (itera_i<nextStateExe.size())
        {
            // for q0 :1,a;
            String getState = dictionaryforStates.get(nextStateExe.get(itera_i));
            if (toDeadState.containsKey(nextStateExe.get(itera_i)))
                toDeadS = toDeadState.get(nextStateExe.get(itera_i));

            GetDrawCircle(nextStateExe.get(itera_i),getState,canvas);

            itera_i+=1;
        }

        int i =0;

        String deadWord=GetDeadStateWord();
        deadWord = deadWord.trim();
        itsSelfLoop("D",deadWord,canvas);

    }

    private  void SetBothDicState_Dead(String fromToWordStates)
    {
        String[] arrOfWords = fromToWordStates.split(";");
        String GEtWithoutD=null;
        String GetForDState = null;

        for(int i=0;i<arrOfWords.length;i++)
        {
            String[] fromState = arrOfWords[i].split(":");
            if (fromState[1].indexOf('D')>-1) {


                int index = fromState[1].indexOf('D');
                if (index > -1) {
                    GEtWithoutD = fromState[1].substring(0, index - 1);
                    GetForDState = fromState[1].substring(index + 1, fromState[1].length());
                    toDeadState.put(fromState[0], GetForDState);
                } else
                    GEtWithoutD = fromState[1];

                String[] valueStr = fromState[1].split(",");


            }
            else
            {
                GEtWithoutD =fromState[1];

            }
            dictionaryforStates.put(fromState[0], GEtWithoutD);
        }

    }

    private void GetDrawCircle(String fromState,String ToandWord,Canvas canvas)
    {
        float[] xandY = StateDimension.get(fromState);



        String[] arrStr =  ToandWord.split(",");
        if(!StateDimension.containsKey(arrStr[0])) {
            canvas.drawCircle(xandY[0] + DifferenceBetweenStates, xandY[1], singleCircleRadius, StateCirle);
            Boolean getAns = CHeckIfStateISFinalOrNot(arrStr[0]);
            if (getAns)
                canvas.drawCircle(xandY[0] + DifferenceBetweenStates, xandY[1], doubleCircleRadius, StateCirle);

            AddNewDimensionForState(arrStr[0], xandY[0] + DifferenceBetweenStates, xandY[1]);

            if (dictionaryforStates.containsKey(arrStr[0]) && !nextStateExe.contains(arrStr[0])) {

                nextStateExe.add(arrStr[0]);
            }
            insertTextintoCirlcle(arrStr[0], canvas);
            DrawLineBtwStates(fromState,arrStr[1],arrStr[0],canvas);
        }
        else
        {
            if (!arrStr[0].matches(fromState))
                backLinefromTop(arrStr[1],fromState,arrStr[0],canvas);
            else
                itsSelfLoop(arrStr[0],arrStr[1],canvas);
        }


        if (toDeadState.containsKey(fromState))
        {
            String DeadStateWords = toDeadState.get(fromState);
            DrawLineBtwStates(fromState,DeadStateWords,"D",canvas);
        }
    }

    private  void backLinefromTop(String word,String fromState,String toState,Canvas canvas)
    {
        float[] fromXandY = StateDimension.get(fromState);
        float[] toXandY = StateDimension.get(toState);

        canvas.drawLine(fromXandY[0],fromXandY[1]-100,fromXandY[0]-150,fromXandY[1]-150,StateCirle);
        canvas.drawLine(fromXandY[0]-150,fromXandY[1]-150,toXandY[0],toXandY[1]-70,StateCirle);

        canvas.drawCircle(toXandY[0],toXandY[1]-70,circleRadiusArrow,arrowFillCircle);

        float midX = ((fromXandY[0]-100)+toXandY[0])/2;
        float ymid = ((fromXandY[1]-150) + (toXandY[1]-70))/2;

        canvas.drawText(word,midX,ymid-30,textPaint);

    }
    private void itsSelfLoop(String fromState,String word,Canvas canvas)
    {
        float[] loopState = StateDimension.get(fromState);
        if (CHeckIfStateISFinalOrNot(fromState))
        {
            canvas.drawBitmap(looparrow,loopState[0]-70,loopState[1]-235,StateCirle);

            canvas.drawText(word,loopState[0],loopState[1]-270,textPaint);
        }
        else
        {
            canvas.drawBitmap(looparrow,loopState[0]-70,loopState[1]-170,StateCirle);

            canvas.drawText(word,loopState[0],loopState[1]-190,textPaint);
        }

    }

    private void AddNewDimensionForState(String key,float x,float y)
    {
        float[] arr =  new  float[]{x,y};
        StateDimension.put(key,arr);
    }
    //Inside Circle Text Write
    private void insertTextintoCirlcle(String key,Canvas canvas)
    {
        float[] xandy = StateDimension.get(key);
        if(!key.matches("D"))
            canvas.drawText("q"+key,xandy[0]-50,xandy[1]+20,textPaint);
        else
            canvas.drawText(key,xandy[0]-20,xandy[1]+17,textPaint);
    }
    //q0 to q1
    private void DrawLineBtwStates(String fromState,String word,String ToState,Canvas canvas)
    {
        float[] fromStateX = StateDimension.get(fromState);
        float[] toStateX  =  StateDimension.get(ToState);

        if (fromStateX[1] == toStateX[1] && Integer.parseInt(fromState)<Integer.parseInt(ToState))
        {
            if(!CHeckIfStateISFinalOrNot(ToState))
            {
                canvas.drawLine(fromStateX[0]+70,fromStateX[1],toStateX[0]-70,toStateX[1],StateCirle);
                canvas.drawCircle(toStateX[0]-80,toStateX[1],circleRadiusArrow,arrowFillCircle);
            }
            else {
                canvas.drawLine(fromStateX[0]+70,fromStateX[1],toStateX[0]-90,toStateX[1],StateCirle);
                canvas.drawCircle(toStateX[0]-100,toStateX[1],circleRadiusArrow,arrowFillCircle);
            }
        }
        else if(ToState.matches("D")) {
            int getIndOfDeadState = DstateMorePoints.size();
            DeadMorePointsformaping(getIndOfDeadState);
            float[] toStateDead = DstateMorePoints.get(getIndOfDeadState);
            canvas.drawLine(fromStateX[0] + 70, fromStateX[1], toStateDead[0], toStateDead[1], StateCirle);
            canvas.drawCircle(toStateDead[0]-10,toStateDead[1],circleRadiusArrow,arrowFillCircle);
        }
        DrawTextBtwStatesLine(ToState,word,fromStateX,toStateX,canvas);
    }

    private void DrawTextBtwStatesLine(String toStateW,String Key,float[] fromState,float[] toState,Canvas canvas)
    {
        double xmid = Math.ceil(((toState[0]) + (fromState[0]))/2);
        float ymid =0;
        int fromkey = 0;
        int toKey =0;

        if (fromState[1] == toState[1]) {
            fromkey = Integer.parseInt(getKey(StateDimension,fromState));
            toKey = Integer.parseInt(getKey(StateDimension,toState));
            if (fromkey < toKey) {
                xmid -= 20;
                ymid = toState[1];
                ymid -= 10;
                if (CHeckIfStateISFinalOrNot(toStateW))
                    xmid -= 17;
                canvas.drawText(Key, (float) xmid, ymid, textPaint);
            }
        }
        else
        {

            ymid = (float) Math.ceil((toState[1] + fromState[1])/2);
            //xmid = deaS[0];
            //ymid = deaS[1];
            xmid +=27;
            if(CHeckIfStateISFinalOrNot(toStateW))
                xmid -=17;
            canvas.drawText(Key,(float) xmid,ymid,textPaint);
        }

    }

    private Boolean CHeckIfStateISFinalOrNot(String State)
    {

        if (FinalState.matches(State))
            return true;
        return  false;
    }
    private  void DeadMorePointsformaping(int index)
    {
        float[] getDeadCoord = StateDimension.get("D");
        float[] arrfloat =  new float[]{0,1};

        if (index == 0) {
            arrfloat[0] = (float) (getDeadCoord[0] - 64.0);
            arrfloat[1] = (float) getDeadCoord[1];
        } else if (index == 1) {
            arrfloat[0] = (float) (getDeadCoord[0] - 40.0);
            arrfloat[1] = (float) (getDeadCoord[1] - 63.0);
        } else if (index == 2) {
            arrfloat[0] = (float) (getDeadCoord[0] - 10.0);
            arrfloat[1] = (float) (getDeadCoord[1] - 63.0);
        } else if (index == 3) {
            arrfloat[0] = (float) (getDeadCoord[0] + 30.0);
            arrfloat[1] = (float) (getDeadCoord[1] - 63.0);
        } else if (index == 4) {
            arrfloat[0] = (float) (getDeadCoord[0] + 50.0);
            arrfloat[1] = (float) (getDeadCoord[1] - 53.0);
        } else if (index == 5) {
            arrfloat[0] = (float) (getDeadCoord[0] + 70.0);
            arrfloat[1] = (float) (getDeadCoord[1] - 13.0);
        }
        else if (index == 6)
        {
            arrfloat[0] = (float) (getDeadCoord[0] + 90.0);
            arrfloat[1] = (float) (getDeadCoord[1] - 0.0);
        }
        if (!DstateMorePoints.contains(arrfloat))
            DstateMorePoints.add(arrfloat);
    }
    public static <K, V> K getKey(Map<K, V> map, V value)
    {
        for (Map.Entry<K, V> entry: map.entrySet())
        {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private String GetDeadStateWord()
    {
        String firsword = toDeadState.get("0");
        firsword = firsword.trim();
        String Addition =firsword;
        ArrayList<String> dead =  new ArrayList<String>();
        dead.add(firsword);
        for (int i=1;i<toDeadState.size();i++)
        {
            String secondChar = toDeadState.get(i+"");
            secondChar=secondChar.trim();
            if (!dead.contains(secondChar))
                Addition += ","+secondChar;dead.add(secondChar);
        }
        return  Addition;
    }




}
