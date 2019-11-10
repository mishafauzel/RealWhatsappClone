package com.example.realwhatsappclone.di.fragmentsdi.main;

import com.example.realwhatsappclone.dialogs.DialogsFragment;

import com.example.realwhatsappclone.messages.MessageFragment;
import com.example.realwhatsappclone.users.UsersFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentsBuilderModule {
    @ContributesAndroidInjector
    public abstract DialogsFragment conributeDialogsFragment();
    @ContributesAndroidInjector
    public abstract MessageFragment contributeDialogFragment();

    @ContributesAndroidInjector
    public abstract UsersFragment contributeUserFragment();
}
