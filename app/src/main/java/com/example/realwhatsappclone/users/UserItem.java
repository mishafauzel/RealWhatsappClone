package com.example.realwhatsappclone.users;

public class UserItem {
    public String uid,nameInApp,name,phone,uidOfImage;

    public UserItem(String nameInApp, String name, String phone,String uidOfImage) {
        this.nameInApp = nameInApp;
        this.name = name;
        this.phone = phone;
        this.uidOfImage=uidOfImage;
    }
}
