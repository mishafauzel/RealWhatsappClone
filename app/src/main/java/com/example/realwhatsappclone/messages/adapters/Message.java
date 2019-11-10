package com.example.realwhatsappclone.messages.adapters;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Message {
    private static final String TAG = "Message";
    String uid;
    String senderUId;
    Calendar calendar;
    String displayingTime;

    String text;
    long date;
    String[] imagesReferensec;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSenderUId() {
        return senderUId;
    }

    public void setSenderUId(String senderUId) {
        this.senderUId = senderUId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String[] getImagesReferensec() {
        return imagesReferensec;
    }

    public void setImagesReferensec(String[] imagesReferensec) {
        this.imagesReferensec = imagesReferensec;
    }
     public Message(String uid, String sender, String text, long date, String[] imagesReferemsec) {
        this.uid = uid;
        this.senderUId = sender;
        this.text = text;
        this.date = date;


         this.calendar=Calendar.getInstance();
         calendar.setTimeZone(TimeZone.getDefault());
         calendar.setTimeInMillis(date);
         Log.d(TAG, "Message: date is "+calendar.get(Calendar.DAY_OF_MONTH)+"."+calendar.get(Calendar.MONTH)+"."+calendar.get(Calendar.YEAR)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND));
        String hours=String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minutes=calendar.get(Calendar.MINUTE)<10?"0"+calendar.get(Calendar.MINUTE):calendar.get(Calendar.MINUTE)+"";
        displayingTime=new StringBuilder(hours).append(":").append(minutes).toString();
        this.imagesReferensec = imagesReferemsec;
    }

    @Override
    public String toString() {
        return "Message{" +
                "uid='" + uid + '\'' +
                ", senderUId='" + senderUId + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", imagesReferensec=" + Arrays.toString(imagesReferensec) +
                '}';
    }
}
