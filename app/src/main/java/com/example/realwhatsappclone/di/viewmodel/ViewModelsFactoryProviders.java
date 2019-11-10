package com.example.realwhatsappclone.di.viewmodel;

import androidx.lifecycle.ViewModelProvider;

import com.example.realwhatsappclone.ViewModel.ViewModelsFactory;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelsFactoryProviders {
   @Singleton
   @Binds
   public abstract ViewModelProvider.Factory bindsViewModel(ViewModelsFactory modelsFactory);
}
