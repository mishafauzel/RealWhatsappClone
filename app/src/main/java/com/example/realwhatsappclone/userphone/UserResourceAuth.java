package com.example.realwhatsappclone.userphone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserResourceAuth {
    @Nullable
    String message;
    @NonNull
    Status status;

    @Nullable
    public String getMessage() {
        return message;
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    public UserResourceAuth(@Nullable String message, @NonNull Status status) {
        this.message = message;
        this.status = status;
    }

    public static UserResourceAuth succes()
    {
        return new UserResourceAuth(null,Status.Succes);
    }
    public static UserResourceAuth error(String message)
    {
        return new UserResourceAuth(message,Status.Error);
    }
    public static UserResourceAuth canceled()
    {
        return new UserResourceAuth(null,Status.Cancel);
    }
    public static UserResourceAuth Loading()
    {
        return new UserResourceAuth(null,Status.Loading);
    }
    public enum Status{Succes,Error,Cancel,Loading}
}
