package com.example.realwhatsappclone.dialogs.adapters;

public class Dialog implements Comparable {
    String Uid;
    String nameOfUser;
    String lastMessage;
    String userUid;
    String urlForImage;
    String phoneOfUser;

    public Dialog(String uid, long date) {
        Uid = uid;
        this.date = date;
    }

    public String getPhoneOfUser() {
        return phoneOfUser;
    }

    public void setPhoneOfUser(String phoneOfUser) {
        this.phoneOfUser = phoneOfUser;
    }

    public String getUrlForImage() {
        return urlForImage;
    }

    public void setUrlForImage(String urlForImage) {
        this.urlForImage = urlForImage;
    }

    long date;

    public Dialog(String uid, String userUid, long date) {
        Uid = uid;
        this.userUid = userUid;
        this.date=date;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public String toString() {
        return "Dialog{" +
                "Uid='" + Uid + '\'' +
                ", nameOfUser='" + nameOfUser + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", userUid='" + userUid + '\'' +
                ", urlForImage='" + urlForImage + '\'' +
                ", phoneOfUser='" + phoneOfUser + '\'' +
                ", date=" + date +
                '}';
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int compareTo(Object o) {
        return (int) (this.date-((Dialog)o).date);
    }
}
