package com.example.realwhatsappclone.di.applicationdi;

import com.example.realwhatsappclone.MainApplication;
import com.example.realwhatsappclone.di.activitiesdi.BaseActivityModule;
import com.example.realwhatsappclone.di.servicedi.ServiceModule;
import com.example.realwhatsappclone.di.viewmodel.ViewModelsFactoryProviders;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
@Singleton
@Component(modules = {AndroidSupportInjectionModule.class,
        AppModule.class, ViewModelsFactoryProviders.class, BaseActivityModule.class,ServiceModule.class})

public interface ApplicationComponent extends AndroidInjector<MainApplication> {
    @Component.Builder
     interface Builder
    {
        @BindsInstance
        Builder application(MainApplication application);
        ApplicationComponent build();
    }
}
