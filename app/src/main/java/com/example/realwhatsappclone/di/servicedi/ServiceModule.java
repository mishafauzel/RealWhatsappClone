package com.example.realwhatsappclone.di.servicedi;

import com.example.realwhatsappclone.CheckUsersInApp;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceModule {
    @ContributesAndroidInjector
    public abstract CheckUsersInApp contributeChecker();

}
