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

public class MealyCanvasImage extends View {

    Paint StateCirle = new Paint();

    Paint textPaint =  new Paint();

    Paint arrowFillCircle =  new Paint();

    float singleCircleRadius = 70;

    float DifferenceBetweenStates = 350;

    float circleRadiusArrow = 14;

    Map<String, String> dictionaryforStates = new HashMap<String, String>();

    Map<String,float[]> StateDimension =  new HashMap<String,float[]>();

    ArrayList<String> nextStateExe = new ArrayList<String>();

    ArrayList<String> FinalState =  new ArrayList<String>();

    Bitmap looparrow = BitmapFactory.decodeResource(getResources(), R.drawable.loopitself2);

    public MealyCanvasImage(Context context,Map<String,String> wordSDict,ArrayList<String> obj,String topic) {
        super(context);
        dictionaryforStates = wordSDict;
        FinalState =obj;
        if (topic.equals("Increment 2 in input"))
            DifferenceBetweenStates =450;
    }
    @Override
    protected void onDraw(Canvas canvas)
    {

        super.onDraw(canvas);

        StateCirle.setColor(Color.BLACK);
        StateCirle.setStrokeWidth(8);
        StateCirle.setStyle(Paint.Style.STROKE);

        //Setting up paint for Text in side Circle
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(80);

        //instead of arrow cicle fill black
        arrowFillCircle.setColor(Color.BLACK);

        //DrawLineFor Start
        canvas.drawLine(0,1000,236,1000,StateCirle);
        canvas.drawCircle(226,1000,circleRadiusArrow,arrowFillCircle);
        canvas.drawText("Start",30,990,textPaint);

        AddNewDimensionForState("0",300,1000);
        canvas.drawCircle(300,1000,singleCircleRadius,StateCirle);
        insertTextintoCirlcle("0",canvas);

        nextStateExe.add("0");
        int i=0;
        while (i<nextStateExe.size())
        {
            String wordsStates = dictionaryforStates.get(i+"");
            String[] strarr = wordsStates.split(";");
            if (strarr.length>1)
            {
                for (int j=0;j<strarr.length;j++)
                {
                    String[] wordTrans = strarr[j].split(",");
                    DrawShapesMain(i+"",wordTrans,canvas);
                }
            }
            else if (strarr.length==1)
            {
                String[] wordTrans = strarr[0].split(",");
                DrawShapesMain(i+"",wordTrans,canvas);
            }

            i+=1;
        }

    }
    private void DrawShapesMain(String fromState,String[] forShapes,Canvas canvas)
    {
        String word = forShapes[1];
        String toState = forShapes[0];

        float[] xandY = StateDimension.get(fromState);

        if (!StateDimension.containsKey(toState))
        {
            canvas.drawCircle(xandY[0] + DifferenceBetweenStates, xandY[1], singleCircleRadius, StateCirle);
            AddNewDimensionForState(toState,xandY[0]+DifferenceBetweenStates,xandY[1]);
            insertTextintoCirlcle(toState,canvas);
            DrawLineBtwStates(fromState,word,toState,canvas);
        }
        else if (fromState.equals(toState))
        {
            itsSelfLoop(fromState,word,canvas);
        }
        else if (Integer.parseInt(fromState)>Integer.parseInt(toState))
        {
            DrawLineBtwStates(fromState,word,toState,canvas);
        }

        if (!nextStateExe.contains(toState))
            nextStateExe.add(toState);

    }

    private void AddNewDimensionForState(String key,float x,float y)
    {
        float[] arr =  new  float[]{x,y};
        StateDimension.put(key,arr);
    }
    private void insertTextintoCirlcle(String key,Canvas canvas)
    {
        float[] xandy = StateDimension.get(key);

        canvas.drawText("q"+key,xandy[0]-50,xandy[1]+20,textPaint);

    }

    private void DrawLineBtwStates(String fromState,String word,String ToState,Canvas canvas)
    {
        float[] fromStateX = StateDimension.get(fromState);
        float[] toStateX  =  StateDimension.get(ToState);

        if (fromStateX[1] == toStateX[1] && Integer.parseInt(fromState)<Integer.parseInt(ToState))
        {

                canvas.drawLine(fromStateX[0]+70,fromStateX[1],toStateX[0]-70,toStateX[1],StateCirle);
                canvas.drawCircle(toStateX[0]-80,toStateX[1],circleRadiusArrow,arrowFillCircle);


        }
        else if (fromStateX[1] == toStateX[1] && Integer.parseInt(fromState)>Integer.parseInt(ToState))
        {
            canvas.drawLine(fromStateX[0],fromStateX[1]+70,fromStateX[0]-70,fromStateX[1]+100,StateCirle);
            canvas.drawLine(fromStateX[0]-70,fromStateX[1]+100,toStateX[0]+70,toStateX[1],StateCirle);
            canvas.drawCircle(toStateX[0]+70,toStateX[1],circleRadiusArrow,arrowFillCircle);

            float xmid = (float) Math.ceil(((toStateX[0]+70) + (fromStateX[0]-70))/2);

           canvas.drawText(word,xmid,toStateX[1]+130,textPaint);

        }
        DrawTextBtwStatesLine(ToState,word,fromStateX,toStateX,canvas);
    }
    private Boolean CHeckIfStateISFinalOrNot(String State)
    {

        if (FinalState.contains(State))
            return true;
        return  false;
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
                xmid -= 37;
                if (DifferenceBetweenStates==450)
                    xmid-=70;
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

    private void itsSelfLoop(String fromState,String word,Canvas canvas)
    {
        float[] loopState = StateDimension.get(fromState);
        canvas.drawBitmap(looparrow,loopState[0]-55,loopState[1]-150,StateCirle);

        canvas.drawText(word,loopState[0]-55,loopState[1]-220,textPaint);
    }

}
