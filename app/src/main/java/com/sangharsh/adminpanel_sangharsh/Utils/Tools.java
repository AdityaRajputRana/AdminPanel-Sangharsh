package com.sangharsh.adminpanel_sangharsh.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {

    public String getTimeStamp(){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        return timeStamp;
    }

    public String getTimeStamp(String nameOfFile){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        return timeStamp.substring(0, timeStamp.length()-2) + nameOfFile + timeStamp.substring(timeStamp.length()-2, timeStamp.length()-1);
    }
}
