package com.example.realwhatsappclone.dialogs.adapters;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.dialogs.DialogsResources;
import com.example.realwhatsappclone.dialogs.adapters.Dialog;
import com.example.realwhatsappclone.repository.Repositorie;
import com.example.realwhatsappclone.room.UserDatabase;
import com.example.realwhatsappclone.room.Users;
import com.example.realwhatsappclone.utills.DataBaseErrorToException;
import com.example.realwhatsappclone.utills.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;
import static com.example.realwhatsappclone.utills.NodesNames.*;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DialogsRepositories {
    private static final String TAG = "DialogsRepositories";
    @Inject
    FirebaseAuth auth;
    @Inject
    FirebaseDatabase database;
    @Inject
    UserDatabase localDatabase;

    Resources resources;
    String noDialogs;

    @Inject
    public DialogsRepositories(Resources resources) {
        this.resources = resources;
        noDialogs = resources.getString(R.string.there_is_no_dialogs_anymore);
    }

    public Observable<Dialog> getDialogs(int pageSize,long date)
    {
        return getDatasnaphotOfDialog(pageSize,date).flatMap(new Function<Resource<DataSnapshot>, ObservableSource<Dialog>>() {
            @Override
            public ObservableSource<Dialog> apply(Resource<DataSnapshot> dataSnapshotResource) throws Exception {
                return getDialogsFromDataSnaphot(dataSnapshotResource);
            }
        }).flatMap(new Function<Dialog, ObservableSource<Dialog>>() {
            @Override
            public ObservableSource<Dialog> apply(Dialog dialog) throws Exception {
                return getLastMessageOfDialogs(dialog);
            }
        }).flatMap(new Function<Dialog, ObservableSource<Dialog>>() {
            @Override
            public ObservableSource<Dialog> apply(Dialog dialog) throws Exception {
                return getUsersName(dialog);
            }
        });
    }

    private Observable<Resource<DataSnapshot>> getDatasnaphotOfDialog(int pageSize,long date)
    {
        Query query;
        if(date==0)
        {
            query=database.getReference().child(USERS).child(auth.getUid()).child(DIALOGS).orderByChild(DATE).limitToFirst(pageSize);

        }
        else query=database.getReference().child(USERS).child(auth.getUid()).child(DIALOGS).orderByChild(DATE);

        return Observable.create(new ObservableOnSubscribe<Resource<DataSnapshot>>() {
            @Override
            public void subscribe(ObservableEmitter<Resource<DataSnapshot>> emitter) throws Exception {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: "+dataSnapshot.getChildrenCount());
                        Log.d(TAG, "onDataChange: "+Thread.currentThread().getName());
                        emitter.onNext(Resource.success(dataSnapshot));
                        emitter.onComplete();
                        }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        emitter.onError(new Exception(DataBaseErrorToException.convertToException(resources,databaseError)));
                    }
                });
            }
        });
    }

    public Observable<Dialog> getDialogsFromDataSnaphot(Resource<DataSnapshot> dataSnapshotResource)
    {

           return Observable.create(new ObservableOnSubscribe<Dialog>() {
                @Override
                public void subscribe(ObservableEmitter<Dialog> emitter) throws Exception {
                    if(dataSnapshotResource.status== Resource.Status.Succes)
                    for (DataSnapshot child :
                            dataSnapshotResource.data.getChildren()) {
                        Log.d(TAG, "subscribe: "+dataSnapshotResource.data.getChildrenCount());
                        Log.d(TAG, "subscribe: "+Thread.currentThread().getName());
                        String uidOfDialog=child.getKey();
                        long date=Long.valueOf(child.child(DATE).getValue().toString());
                        Dialog childDialog=new Dialog(uidOfDialog,date);
                        childDialog.userUid=childDialog.Uid.replace(auth.getUid(),"");
                        Log.d(TAG, "subscribe: "+childDialog.toString());
                        emitter.onNext(childDialog);
                    }
                    emitter.onComplete();
                }
            });

    }
    public Observable<Dialog> getLastMessageOfDialogs(Dialog dialog)
    {
        return Observable.create(new ObservableOnSubscribe<Dialog>() {
            @Override
            public void subscribe(ObservableEmitter<Dialog> emitter) throws Exception {
                if(!dialog.Uid.equals("error"))
                    database.getReference().child(DIALOGS).child(dialog.Uid).child(MESSAGES).orderByChild(DATE).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child :
                                dataSnapshot.getChildren()) {
                         dialog.lastMessage=child.child(TEXT).getValue().toString();
                            Log.d(TAG, "onDataChange: "+dialog.toString());
                         emitter.onNext(dialog);
                         emitter.onComplete();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.lastMessage="error";
                        emitter.onNext(dialog);
                    }
                });

            }
        });
    }
    public Observable<Dialog> getUsersName(Dialog dialog)
    {
        return Observable.create(new ObservableOnSubscribe<Dialog>() {
            @Override
            public void subscribe(ObservableEmitter<Dialog> emitter) throws Exception {
                Users user=localDatabase.usersDao().getUserByUid(dialog.userUid);
                if(user!=null) {
                    dialog.phoneOfUser = user.getPhoneNumber();
                    dialog.nameOfUser=user.getName();
                    dialog.urlForImage=user.getUidofImage();
                    Log.d(TAG, "subscribe: "+dialog.toString());
                    emitter.onNext(dialog);
                    emitter.onComplete();
                }
                else
                {
                    database.getReference().child(USERS).child(dialog.userUid).addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String phone=dataSnapshot.child(PHONES).getValue().toString();
                            String photoRef=dataSnapshot.child(PHOTOREF).getValue().toString();
                            dialog.urlForImage=photoRef;
                            dialog.phoneOfUser=phone;
                            Log.d(TAG, "onDataChange: "+dialog.toString());
                            emitter.onNext(dialog);
                            emitter.onComplete();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        emitter.onNext(dialog);
                        }
                    });
                }

            }
        }).subscribeOn(Schedulers.io());
    }
}




