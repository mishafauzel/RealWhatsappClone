package com.example.realwhatsappclone;

import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.realwhatsappclone.login.AuthResource;
import com.example.realwhatsappclone.room.Users;
import com.example.realwhatsappclone.userphone.UserResourceAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.Flowable.create;
import static com.example.realwhatsappclone.utills.NodesNames.*;

@Singleton
public class SessionManager {
    private static final String TAG = "SessionManager";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabse;
    private MutableLiveData<AuthResource<FirebaseUser>> userLiveData = new MutableLiveData<>();

    @Inject
    public SessionManager(FirebaseAuth auth, FirebaseDatabase database) {
        mAuth = auth;
        mDatabse = database;

    }

    public MutableLiveData<AuthResource<FirebaseUser>> getUserLiveData() {
        return userLiveData;
    }

    public AuthResource<FirebaseUser> checkIsAthentificated() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null)
            return AuthResource.notAthentificates();
        else
            return AuthResource.authenificated(user);
    }

    public Flowable<AuthResource<FirebaseUser>> authentificateUsesr(final String email, final String password) {
        Flowable<AuthResource<FirebaseUser>> observable = create((FlowableOnSubscribe<FirebaseUser>) emitter -> {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: ");
                    emitter.onNext(mAuth.getCurrentUser());
                    emitter.onComplete();
                } else emitter.onError(new Throwable("Something went wrong"));
            });
        }, BackpressureStrategy.BUFFER).map(firebaseUser -> {
            Log.d(TAG, "apply: " + Thread.currentThread().getName());
            return AuthResource.authenificated(firebaseUser);
        }).onErrorReturn(throwable -> AuthResource.Error(throwable.getMessage(), null)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public Flowable<AuthResource<FirebaseUser>> createAccount(final String email, final String password) {
        Flowable<AuthResource<FirebaseUser>> flowable = create((FlowableOnSubscribe<FirebaseUser>) emitter ->
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task) ->
        {
            if (task.isSuccessful()) {
                Log.d(TAG, "createAccount: succes");
                emitter.onNext(mAuth.getCurrentUser());
                emitter.onComplete();
            } else
            {
                Log.d(TAG, "createAccount: error");
                emitter.onError(new Throwable("Authentication went wrong"));}
        }), BackpressureStrategy.BUFFER).map(firebaseUser -> AuthResource.authenificated(firebaseUser)).onErrorReturn(throwable -> AuthResource.Error(throwable.getMessage(), null));
        return flowable;
    }

    public Flowable<UserResourceAuth> safeMyPhoneNumber(String phoneNumber, String name) {
        if (mAuth.getCurrentUser() != null) {

            Flowable<UserResourceAuth>flowable=Flowable.create(new FlowableOnSubscribe<UserResourceAuth>() {
                @Override
                public void subscribe(FlowableEmitter<UserResourceAuth> emitter) throws Exception {
                    DatabaseReference reference = mDatabse.getReference().child(USERS).child(mAuth.getCurrentUser().getUid());
                    Query query=reference.orderByChild(PHONES).equalTo(phoneNumber);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                emitter.onError(new Throwable("Sorry,user with such phone already exists"));
                            }
                            else {
                                DatabaseReference referenceForMe = mDatabse.getReference().child("user").child(mAuth.getCurrentUser().getUid());
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            Map<String, Object> userMap = new HashMap<>();
                                            userMap.put("phone", phoneNumber);
                                            userMap.put("name", name);
                                            reference.updateChildren(userMap);
                                            emitter.onNext(UserResourceAuth.succes());
                                            emitter.onComplete();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        emitter.onError(new Throwable("cancel"));
                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            },BackpressureStrategy.BUFFER).onErrorReturn(throwable -> UserResourceAuth.error(throwable.getMessage()));
            return flowable;


        }
        else
            return null;
    }
  public Flowable<UserResourceAuth> checkUserPhoneExistence()
  {
      Flowable<UserResourceAuth> flowable= create((FlowableOnSubscribe<UserResourceAuth>) emitter -> {
          Log.d(TAG, "checkUserPhoneExistence: starting");
          DatabaseReference reference = mDatabse.getReference().child(USERS).child(mAuth.getCurrentUser().getUid()).child(PHONES);
          reference.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                  if(dataSnapshot.exists()) {
                      String value = dataSnapshot.getValue().toString();
                      Log.d(TAG, "onDataChange: check phone"+value);
                      if(value.length()==12)
                      {
                          Log.d(TAG, "onDataChange: sizeOfPhone is "+value.length());
                          emitter.onNext(UserResourceAuth.succes());
                          emitter.onComplete();
                      }}
                      else {
                          Log.d(TAG, "onDataChange: there is no such number");
                          emitter.onError(new Throwable("Please, enter your phone and name"));
                      }

                  }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {
                  Log.d(TAG, "onCancelled: something went wrong");
                    emitter.onError(new Throwable(databaseError.getMessage()));
              }
          });


      },BackpressureStrategy.BUFFER).onErrorReturn(throwable -> UserResourceAuth.error(throwable.getMessage()));
      return flowable;
  }

    public void logout() {
        mAuth.signOut();
    }
}
