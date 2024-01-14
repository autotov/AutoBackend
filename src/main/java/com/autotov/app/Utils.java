package com.autotov.app;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY");
    public static String dateFormat(Date date){
        if(date == null)
            return "אין נתון";
        return formatter.format(date);
    }
}
