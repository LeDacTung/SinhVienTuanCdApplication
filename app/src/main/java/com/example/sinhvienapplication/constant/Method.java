package com.example.sinhvienapplication.constant;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Method {
    public static String getTimeCurrent(){
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        return timeStamp;
    }
}
