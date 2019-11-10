package com.example.realwhatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.realwhatsappclone.ViewModel.ViewModelsFactory;
import com.example.realwhatsappclone.databinding.ActivityBaseBinding;
import com.example.realwhatsappclone.userphone.UserResourceAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.invoke.ConstantCallSite;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class BaseActivity extends DaggerAppCompatActivity{

    public ObservableBoolean isVisible=new ObservableBoolean(false);
    protected ViewDataBinding dataBinding;
    private static final String TAG = "BaseActivity";
    ActivityBaseBinding binding;
    NavController navController;
    @Inject
    FirebaseDatabase database;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/**        String[] values=new String[] {"one","three","two"} ;
        for(String string : values) {
             Single.create(new SingleOnSubscribe<String>() {
                @Override
                public void subscribe(SingleEmitter<String> emitter) throws Exception {
                    Log.d(TAG, "subscribe: "+Thread.currentThread().getName());
                    switch(string)
                    {
                        case "one":
                        {

                            Log.d(TAG, "subscribe: one:emitting one");
                            emitter.onSuccess(string);
                            break;
                        }
                        case "two":
                        {
                            Log.d(TAG, "subscribe: two emitting null");
                            emitter.onSuccess(string);
                            break;
                        }
                        case "three":
                        {
                            Log.d(TAG, "subscribe: three trow error");
                            emitter.onError(new Throwable("error"));
                            break;

                        }

                    }
                }
                }).subscribeOn(AndroidSchedulers.mainThread()).onErrorReturnItem("error").flatMap(new Function<String, SingleSource<? extends String>>() {
                @Override
                public SingleSource<? extends String> apply(String s) throws Exception {
                    return Single.create(new SingleOnSubscribe<String>() {
                        @Override
                        public void subscribe(SingleEmitter<String> emitter) throws Exception {
                            Log.d(TAG, "subscribe: "+Thread.currentThread().getName());
                            Log.d(TAG, "subscribe: flatmap called");
                            emitter.onSuccess(s+1);
                        }
                    });
                }
            }).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<String>() {
                 @Override
                 public void onSubscribe(Disposable d) {
                     Log.d(TAG, "onSubscribe: ");
                 }

                 @Override
                 public void onSuccess(String s) {
                     Log.d(TAG, "onSuccess: "+s);
                 }

                 @Override
                 public void onError(Throwable e) {
                     Log.d(TAG, "onError: "+e.getMessage());
                 }
             });
            };**/

        binding=DataBindingUtil.setContentView(this,R.layout.activity_base);

        binding.setIsVisible(this);

        navController = Navigation.findNavController(this, R.id.fragment);








    }




    public void showProgress(boolean visible)
    {
        isVisible.set(visible);
    }
    public void showError(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    public void moveToMainActivity() {
        navController.navigate(R.id.mainActivity);
        this.finish();
    }


    public void moveToPhotoFragment() {
        navController.navigate(R.id.action_phoneFragment_to_photoFragment);
    }
    public void moveToPhoneFragment()
    {
        navController.navigate(R.id.action_loginFragment_to_phoneFragment);
    }

}
