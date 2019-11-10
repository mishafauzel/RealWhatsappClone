package com.example.realwhatsappclone;



import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.realwhatsappclone.di.applicationdi.DaggerApplicationComponent;


import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class MainApplication extends DaggerApplication {
    private static final String TAG = "MainApplication";




    @Override
    public void onCreate() {
        super.onCreate();
       // Intent intent =new Intent(this, MessageChecker.class);
       // this.bindService(intent,new CheckerServiceConnection(), Context.BIND_AUTO_CREATE);
    }



    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().application(this).build();
    }



}
