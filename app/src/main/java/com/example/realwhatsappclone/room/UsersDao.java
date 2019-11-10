package com.example.realwhatsappclone.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
import java.util.Set;
@Dao
public interface UsersDao {
    @Query("Select *from users order by phoneNumber ")
    List<Users> getAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Users> users);
    @Query("Select *from users order by name limit :limit offset :offset")
    List<Users> getusersByPage(int limit,int offset);
    @Query("Select name,phoneNumber from users order By phoneNumber")
    List<Users> getUsersWithNameAndPhones();
    @Query("Select *from users where uid=:uid")
    Users getUserByUid(String uid);
}
