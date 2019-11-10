package com.example.realwhatsappclone.messages.adapters;

import android.content.res.Resources;
import android.util.Log;

import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.dialogs.adapters.Dialog;
import com.example.realwhatsappclone.utills.Resource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

import static com.example.realwhatsappclone.utills.NodesNames.DATE;
import static com.example.realwhatsappclone.utills.NodesNames.DIALOGS;
import static com.example.realwhatsappclone.utills.NodesNames.HAS_NEW_MESSAGES;
import static com.example.realwhatsappclone.utills.NodesNames.LAST_MESSAGE;
import static com.example.realwhatsappclone.utills.NodesNames.MESSAGES;
import static com.example.realwhatsappclone.utills.NodesNames.PHOTOS;
import static com.example.realwhatsappclone.utills.NodesNames.TEXT;
import static com.example.realwhatsappclone.utills.NodesNames.USERS;
import static com.example.realwhatsappclone.utills.NodesNames.USERS_ID;

public class MessageInserter {
    private static final String TAG = "MessageInserter";
    FirebaseAuth auth;
    FirebaseDatabase database;
    StorageReference storage;
    Resources resources;
    @Inject
    public MessageInserter(FirebaseAuth auth, FirebaseDatabase database, StorageReference storage,Resources resources) {
        this.auth = auth;
        this.database = database;
        this.storage = storage;
        this.resources=resources;
    }
    public Single<Resource<Message>> insertMessage(String text,String[]imageReference,String userUid)
    {

    return Single.create(new SingleOnSubscribe<Resource<Message>>() {
        @Override
        public void subscribe(SingleEmitter<Resource<Message>> emitter) throws Exception {
            String usersUid=auth.getUid().compareTo(userUid)>=0?auth.getUid()+userUid:userUid+auth.getUid();
            Map<String,Object> updattingMap=new HashMap<>();

            Date date=new Date();
            //put time to first
            String path=String.format("%s/%s/%s/%s/%s",USERS,auth.getUid(),DIALOGS,usersUid,DATE);
            updattingMap.put(path,String.valueOf(date.getTime()));
            //put time to second
            path=String.format("%s/%s/%s/%s/%s",USERS,userUid,DIALOGS,usersUid,DATE);

            updattingMap.put(path,String.valueOf(date.getTime()));
            //get key from inserting new node to textofMessage
            String key=database.getReference().child(USERS).child(auth.getUid()).child(DIALOGS).child(usersUid)
                    .child(MESSAGES).push().getKey();

            //put date to textofMessage
            path=String.format("%s/%s/%s/%s/%s",DIALOGS,usersUid,MESSAGES,key,DATE);
            updattingMap.put(path,String.valueOf(date.getTime()));
            //put text to textofMessage
            path=String.format("%s/%s/%s/%s/%s",DIALOGS,usersUid,MESSAGES,key,TEXT);
            updattingMap.put(path,text);
            path=String.format("%s/%s/%s/%s/%s",DIALOGS,usersUid,MESSAGES,key,USERS_ID);
            updattingMap.put(path,auth.getUid());
            //put images
            if(imageReference!=null) {
                int i = 0;
                path = String.format("%s/%s/%s/%s", DIALOGS, usersUid,MESSAGES, key, PHOTOS);
                for (String refForImage :
                        imageReference) {

                    path=path+"/image"+i;
                    updattingMap.put(path,refForImage);
                }

            }

            Message insertingMessage=new Message(key,auth.getUid(),text,date.getTime(),imageReference);
            database.getReference().updateChildren(updattingMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (!emitter.isDisposed())
                            emitter.onSuccess(Resource.success(insertingMessage));
                    }
                    emitter.onSuccess(Resource.error(resources.getString(R.string.something_went_wrong),insertingMessage));
                }
            });


        }
    });





    }}
