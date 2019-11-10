package com.example.realwhatsappclone.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

public class AuthResource<T> {
    @Nullable
    public String message;
    @Nullable
    public T data;
    @NonNull
    public State state;

    public AuthResource(@Nullable String message,@Nullable T data,@NonNull State state) {
        this.message = message;
        this.data = data;
        this.state = state;
    }

    public static<D> AuthResource<D> notAthentificates()
    {
        return new AuthResource(null,null,State.NotAuthentificated);
    }
    public static<D> AuthResource <D> authenificated(D data)
    {
        return new AuthResource<>(null,data,State.Authentificated);
    }
    public static <D> AuthResource<D> Error(String message,D data)
    {
        return new AuthResource<>(message,data,State.Error);
    }
    public static <D> AuthResource<D> loading()
    {
        return new AuthResource<>(null,null,State.Loading);
    }
    enum State {Authentificated,NotAuthentificated,Error,Loading};
}
