package com.example.realwhatsappclone.utills;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {
    @NonNull
    public  Status status;
    @Nullable
    public final T data;
    @Nullable public  String messge;

    public Resource(@NonNull Status status, @Nullable T data, @Nullable String messge) {
        this.status = status;
        this.data = data;
        this.messge = messge;
    }
    public static<T> Resource success(@NonNull T data)
    {
        return new Resource(Status.Succes,data,null);
    }
    public static <T> Resource error(String mesage,@Nullable T data)
    {
        return new Resource(Status.Error,data,mesage);
    }
    public static <T> Resource loading(@Nullable T data)
    {
        return new Resource(Status.Loading,data,null);
    }

    public enum Status{Succes,Error,Loading}
}
