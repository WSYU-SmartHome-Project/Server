package com.anranyus.control.entity;

import com.google.gson.Gson;
import lombok.Data;

@Data
public class Rest {
    public int command;
    public String message;
    public boolean success;
    int type;//设备标签

    public static final int DEVICE_TYPE_CLIENT = 0;
    public static final int DEVICE_TYPE_SERVER = 1;
    public static final int DEVICE_TYPE_CONTROL = 2;


    public Rest(int command, String message, boolean success,int type) {
        this.command = command;
        this.message = message;
        this.success = success;
        this.type = type;
    }

    public static Rest success(int status, String message,int type){
        return new Rest(status,message,true,type);
    }

    public static Rest fail(int status,String message,int type){
        return new Rest(status,message,false,type);
    }

}
