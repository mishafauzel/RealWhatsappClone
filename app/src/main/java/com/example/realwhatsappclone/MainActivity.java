package com.example.realwhatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.realwhatsappclone.ViewModel.ViewModelsFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends DaggerAppCompatActivity {
    private static final String TAG = "MainActivity";
    @Inject
    FirebaseDatabase database;
    @Inject
    FirebaseAuth auth;




    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        navController=Navigation.findNavController(this,R.id.fragment);
        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1)
           if( ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED)
           {startService(new Intent(this.getApplicationContext(),CheckUsersInApp.class));}
        else
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.CALL_PHONE},
                1);
        checkOnComplete();





    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            startService(new Intent(this,CheckUsersInApp.class));
    }

    public void moveToUserFragment() {
        navController.navigate(R.id.action_dialogsFragment_to_usersFragment);
    }

    public void moveToBaseFragment() {
        navController.navigate(R.id.baseActivity);
        this.finish();
    }

    public void moveToMessageFragment(String uid,String name,String phoneNumber,String urlForImage) {
        Bundle bundle=new Bundle();
        bundle.putString("uid",uid);
        bundle.putString("name",name);
        bundle.putString("phone",phoneNumber);
        bundle.putString("image",urlForImage);
        navController.navigate(R.id.action_usersFragment_to_messageFragment,bundle);
    }
    public void checkOnComplete()
    {Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
            emitter.onNext(7);
            emitter.onComplete();
        }
    }).flatMap(new Function<Integer, ObservableSource<Integer>>() {
        @Override
        public ObservableSource<Integer> apply(Integer integer) throws Exception {
            return Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                    for(int i=0;i<integer;i++)
                    {
                        emitter.onNext(i);
                    }
                    emitter.onComplete();
                }
            });
        }
    }).toList().subscribe(new SingleObserver<List<Integer>>() {
        @Override
        public void onSubscribe(Disposable disposable) {

        }

        @Override
        public void onSuccess(List<Integer> integers) {
            for (Integer integer :
                    integers) {
                Log.d(TAG, "onSuccess: integers:"+integer+"-------------------------------------------------------------");
            }
        }

        @Override
        public void onError(Throwable throwable) {
            Log.d(TAG, "onError: ");
        }
    });}
}
