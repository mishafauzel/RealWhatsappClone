package com.example.realwhatsappclone.users.adapter;

import android.content.ContentResolver;

import com.example.realwhatsappclone.room.UserDatabase;
import com.example.realwhatsappclone.room.Users;
import com.example.realwhatsappclone.room.UsersDao;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import javax.inject.Inject;

public class UsersRepositories {

    UserDatabase database;
    UsersDao dao;
    @Inject
    public  UsersRepositories(UserDatabase database)
    {
       dao = database.usersDao();
    }
    public List<Users> getPagedUsers(int limit,int offset)
    {
        return dao.getusersByPage(limit, offset);
    }

    
}
