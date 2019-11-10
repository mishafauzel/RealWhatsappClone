package com.example.realwhatsappclone.di.fragmentsdi.login;

import androidx.lifecycle.ViewModel;

import com.example.realwhatsappclone.userphone.PhoneViewModel;
import com.example.realwhatsappclone.di.ViewModelKey;
import com.example.realwhatsappclone.login.LoginViewModel;
import com.example.realwhatsappclone.userphoto.UserPhotoViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class LoginViewModels {



    @Binds
    @IntoMap
    @ViewModelKey(PhoneViewModel.class)
    public abstract ViewModel bindPhoneViewModel(PhoneViewModel phoneViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    public abstract ViewModel bindsLoginViewModel(LoginViewModel loginViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserPhotoViewModel.class)
    public abstract  ViewModel bindsUserPhotoViewModel(UserPhotoViewModel photoViewModel);



}
