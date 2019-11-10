package com.example.realwhatsappclone.users.adapter;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

public class MainThreadExecutor implements Executor {
    Handler mainThreadHandler;

    public MainThreadExecutor() {
        mainThreadHandler=new Handler(Looper.getMainLooper());
    }

    @Override
    public void execute(Runnable runnable) {
        mainThreadHandler.post(runnable);
    }
}
