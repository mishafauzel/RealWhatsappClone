package com.example.realwhatsappclone.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Users {
    public String getUidofImage() {
        return uidofImage;
    }

    public void setUidofImage(String uidofImage) {
        this.uidofImage = uidofImage;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Users)
        return this.getPhoneNumber().equals(((Users)obj).getPhoneNumber());
        else
            return false;
    }

    @Override
    public String toString() {
        return "Users{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", uidofImage='" + uidofImage + '\'' +
                '}';
    }

    @PrimaryKey
        @NonNull
    String phoneNumber;
    String name,uid ,uidofImage;




    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    public Users(String uid, String name, String phoneNumber,String uidofImage) {
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
          this.uidofImage=uidofImage;

    }
}
