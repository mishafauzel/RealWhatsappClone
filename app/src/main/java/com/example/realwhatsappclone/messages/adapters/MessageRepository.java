package com.example.realwhatsappclone.messages.adapters;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.dialogs.adapters.Dialog;
import com.example.realwhatsappclone.utills.DataBaseErrorToException;
import com.example.realwhatsappclone.utills.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Function;

import static com.example.realwhatsappclone.utills.NodesNames.DATE;
import static com.example.realwhatsappclone.utills.NodesNames.DIALOGS;
import static com.example.realwhatsappclone.utills.NodesNames.MESSAGES;
import static com.example.realwhatsappclone.utills.NodesNames.PHOTOS;
import static com.example.realwhatsappclone.utills.NodesNames.TEXT;
import static com.example.realwhatsappclone.utills.NodesNames.USERS_ID;

public class MessageRepository {
    int pageSize=0;
    boolean lastPortionOfDataCome=false;
    FirebaseAuth auth;
    FirebaseDatabase database;
    StorageReference storage;
    Resources resources;
    private static final String TAG = "MessageRepository";

    @Inject
    public MessageRepository(FirebaseDatabase database, FirebaseAuth auth, StorageReference storage,Resources resources) {
        this.database = database;
        this.auth = auth;
        this.storage = storage;
        this.resources=resources;


    }

    public Observable<Message> getMessages(String udiOfSecondUser,int pageSize,Long date,boolean reverse)
    {

        if(pageSize==0)
            this.pageSize=pageSize;
        this.pageSize=pageSize;
       return getDataSnapshot(udiOfSecondUser,pageSize,date,reverse).flatMap(new Function<Resource<DataSnapshot>, ObservableSource<Message>>() {
            @Override
            public ObservableSource<Message> apply(Resource<DataSnapshot> dataSnapshotResource) throws Exception {
                return getMessagesFromDatasnapshot(dataSnapshotResource);
            }
        });
    }

    private Observable<Resource<DataSnapshot>> getDataSnapshot(String uidOfSecondUser, int pageSize, Long date,boolean reverse) {
        return Observable.create(emitter -> {
            String usersUid = auth.getUid().compareTo(uidOfSecondUser) >= 0 ? auth.getUid() + uidOfSecondUser : uidOfSecondUser + auth.getUid();
            Query query;
            if (date == null)
                query = database.getReference().child(DIALOGS).child(usersUid).child(MESSAGES).orderByChild(DATE).limitToLast(pageSize);
            else if(!reverse)
                query = database.getReference().child(DIALOGS).child(usersUid).child(MESSAGES).orderByChild(DATE)
                        .endAt(date.toString(), DATE).limitToLast(pageSize);
            else query=database.getReference().child(DIALOGS).child(usersUid).child(MESSAGES).orderByChild(DATE).startAt(date.toString(),DATE).limitToFirst(pageSize);
            Log.d(TAG, "subscribe: "+query.getPath().toString());
            Log.d(TAG, "getDataSnapshot: "+query.getSpec().toString());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: "+dataSnapshot.exists());
                    Log.d(TAG, "onDataChange: "+dataSnapshot.getChildrenCount());
                    if (dataSnapshot.exists()) {

                        lastPortionOfDataCome=(int)dataSnapshot.getChildrenCount()<pageSize&&!reverse;
                        emitter.onNext(Resource.success(dataSnapshot));

                        emitter.onComplete();
                    } else {
                        emitter.onError(new Exception("There is no more data"));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    emitter.onError(new Throwable(DataBaseErrorToException.convertToException(resources,databaseError)));

                }
            });
        });
    }
    private Observable<Message> getMessagesFromDatasnapshot(Resource<DataSnapshot> dataSnapshotResource)
    {
        return Observable.create(emitter -> {
            if(!lastPortionOfDataCome)
            {
            if(dataSnapshotResource.status.equals(Resource.Status.Succes))
            {

                for (DataSnapshot child:
                        dataSnapshotResource.data.getChildren())
                {
                    String uid=child.getKey();
                    String senderUid=child.child(USERS_ID).getValue().toString();
                    String text=null;
                    long date;
                    if(child.child(TEXT).exists())
                        text=child.child(TEXT).getValue().toString();
                    ArrayList<String>photos=new ArrayList<>();
                    if(child.child(PHOTOS).exists())
                    {
                        photos=new ArrayList<>();
                        for (DataSnapshot datasnapshot :
                                child.child(PHOTOS).getChildren())
                            photos.add(datasnapshot.getValue().toString());
                        }
                    date=Long.valueOf(child.child(DATE).getValue().toString());
                    Message message=new Message(uid,senderUid,text,date,photos.toArray(new String[photos.size()]));
                    Log.d(TAG, "getMessagesFromDatasnapshot: "+message);
                    emitter.onNext(message);
                    }
                emitter.onComplete();
                }}
            else emitter.onError(new Exception(resources.getString(R.string.there_is_no_data_anymore)));

        });
        }

    }
          





