package com.example.realwhatsappclone.room;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {com.example.realwhatsappclone.room.Users.class},version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UsersDao usersDao();

}
