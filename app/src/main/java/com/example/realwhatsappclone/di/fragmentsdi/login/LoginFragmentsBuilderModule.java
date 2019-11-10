package com.example.realwhatsappclone.di.fragmentsdi.login;




import com.example.realwhatsappclone.userphone.PhoneFragment;
import com.example.realwhatsappclone.login.LoginFragment;
import com.example.realwhatsappclone.userphoto.PhotoFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class LoginFragmentsBuilderModule {

    @ContributesAndroidInjector
    public abstract LoginFragment conributeLoginFragment();
    @ContributesAndroidInjector
    public abstract PhoneFragment contributePhoneFragment();
    @ContributesAndroidInjector
    public abstract PhotoFragment contributePhotoFragment();


}
