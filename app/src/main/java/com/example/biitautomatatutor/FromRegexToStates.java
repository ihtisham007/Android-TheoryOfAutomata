package com.example.biitautomatatutor;

import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

public class FromRegexToStates {


    //Get States from Regex
    public   String GetStatesFromRegex(String Regex)
    {

        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("RegexToStates");

        PyObject objreturn=pyObject.callAttr("returnNumber",Regex);

        String states = objreturn.toString();

        return states;
    }
    public String fromToStateFunc(String Regex)
    {
        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("RegexToStates");

        PyObject objreturn=pyObject.callAttr("GetStates",Regex);

        String states = objreturn.toString();

        return states;
    }



}
