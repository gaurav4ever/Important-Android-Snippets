package com.example.gauravpc.mobilecomputing;

/**
 * Created by Gaurav Pc on 10/25/2017.
 */

public class MsgModel {
    private String id;
    private String from;
    private String to;
    private String omsg;
    private String ek;
    private String emsg;
    private String date;

    public MsgModel() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MsgModel(String from, String to, String omsg, String ek, String emsg, String date) {
        this.from = from;
        this.to = to;
        this.omsg = omsg;
        this.ek = ek;
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

    public String getOmsg() {
        return omsg;
    }

    public void setOmsg(String omsg) {
        this.omsg = omsg;
    }

    public String getEk() {
        return ek;
    }

    public void setEk(String ek) {
        this.ek = ek;
    }

    public String getEmsg() {
        return emsg;
    }

    public void setEmsg(String emsg) {
        this.emsg = emsg;
    }
}
