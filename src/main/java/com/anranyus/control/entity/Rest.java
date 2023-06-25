package com.anranyus.control.entity;

import com.google.gson.Gson;
import lombok.Data;

@Data
public class Rest {
    public int command;
    public String message;
    public boolean success;

    public Rest(int command, String message, boolean success) {
        this.command = command;
        this.message = message;
        this.success = success;
    }

    public static Rest success(int status, String message){
        return new Rest(status,message,true);
    }

    public static Rest fail(int status,String message){
        return new Rest(status,message,false);
    }

    public static Rest control(int status){
        return new Rest(status,null,true);
    }
}
