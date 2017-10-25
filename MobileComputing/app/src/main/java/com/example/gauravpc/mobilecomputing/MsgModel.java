package com.example.gauravpc.mobilecomputing;

/**
 * Created by Gaurav Pc on 10/25/2017.
 */

public class MsgModel {
    private String id;
    private String from;
    private String to;
    private String emsg;
    private String date;

    public MsgModel() {

    }


    public MsgModel(String from, String to, String emsg, String date) {
        this.from = from;
        this.to = to;
        this.emsg = emsg;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getEmsg() {
        return emsg;
    }

    public void setEmsg(String emsg) {
        this.emsg = emsg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
