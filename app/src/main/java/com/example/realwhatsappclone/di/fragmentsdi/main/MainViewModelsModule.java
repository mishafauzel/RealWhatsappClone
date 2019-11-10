package com.example.realwhatsappclone.di.fragmentsdi.main;

import androidx.lifecycle.ViewModel;

import com.example.realwhatsappclone.di.ViewModelKey;
import com.example.realwhatsappclone.dialogs.DialogsViewModel;

import com.example.realwhatsappclone.messages.MessagesViewModel;
import com.example.realwhatsappclone.users.UsersViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(DialogsViewModel.class)
    public abstract ViewModel bindsDialogsViewModel(DialogsViewModel dialogsViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(UsersViewModel.class)
    public abstract ViewModel bindsUsersViewModel(UsersViewModel userViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(MessagesViewModel.class)
    public abstract ViewModel bindDialogViewModel(MessagesViewModel dialogVieModel);

}
