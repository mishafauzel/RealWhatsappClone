package com.example.realwhatsappclone.di.applicationdi;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.telephony.TelephonyManager;

import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.realwhatsappclone.MainApplication;
import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.SessionManager;

import com.example.realwhatsappclone.room.UserDatabase;
import com.example.realwhatsappclone.room.UsersDao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Singleton
    @Provides
    public static RequestOptions provideDegRequestOptions()
    {
        return RequestOptions.placeholderOf(R.drawable.white_background).error(R.drawable.white_background);
    }
    @Singleton
    @Provides
    public static RequestManager provideGlide(MainApplication application,RequestOptions requestOptions)
    {
        return Glide.with(application).setDefaultRequestOptions(requestOptions);
    }
    @Singleton
    @Provides
    public static FirebaseAuth provideFirebase()
    {
        return FirebaseAuth.getInstance();
    }
    @Singleton
    @Provides
    public static FirebaseDatabase provideFirebaseDatabase()
    {
        FirebaseDatabase instance=FirebaseDatabase.getInstance();
        instance.setPersistenceEnabled(true);




        return instance;
    }
    @Singleton
    @Provides
    public static SessionManager provideSessionManager(FirebaseAuth auth,FirebaseDatabase database)
    {
        return new SessionManager(auth,database);
    }
    @Singleton
    @Provides
    public static TelephonyManager providesTelephonyManager(MainApplication application)
    {
        return (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
    }
    @Singleton
    @Provides
    public ContentResolver providesContentResolver(MainApplication application)
    {
        return application.getContentResolver();
    }
    @Singleton
    @Provides
    public UserDatabase provideUserDatabase(MainApplication application)
    {
        return Room.databaseBuilder(application,UserDatabase.class,"users").build();
    }
    @Singleton
    @Provides
    public StorageReference provideStorageReference()
    {
        return FirebaseStorage.getInstance().getReference();
    }

    @Singleton
    @Provides
    public Resources provideResources(MainApplication application)
    {
        return application.getResources();
    }


}
