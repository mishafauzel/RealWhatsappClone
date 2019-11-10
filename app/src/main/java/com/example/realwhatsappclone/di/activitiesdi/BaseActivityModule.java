package com.example.realwhatsappclone.di.activitiesdi;

import com.example.realwhatsappclone.BaseActivity;
import com.example.realwhatsappclone.MainActivity;
import com.example.realwhatsappclone.di.fragmentsdi.login.LoginFragmentsBuilderModule;
import com.example.realwhatsappclone.di.fragmentsdi.login.LoginViewModels;
import com.example.realwhatsappclone.di.fragmentsdi.main.MainFragmentsBuilderModule;
import com.example.realwhatsappclone.di.fragmentsdi.main.MainViewModelsModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BaseActivityModule {
@AuthScope
@ContributesAndroidInjector(modules = {LoginFragmentsBuilderModule.class, BaseModule.class, LoginViewModels.class,})
   public abstract BaseActivity contributeBaseActivity();
@MainScope
@ContributesAndroidInjector(modules = {MainModule.class, MainFragmentsBuilderModule.class, MainViewModelsModule.class})
   public abstract MainActivity contributrBaseActivity();
}
