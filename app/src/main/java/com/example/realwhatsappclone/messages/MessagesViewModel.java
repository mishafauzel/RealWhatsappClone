package com.example.realwhatsappclone.messages;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


import com.example.realwhatsappclone.messages.adapters.Message;

import com.example.realwhatsappclone.messages.adapters.MessageDataSourceFactory;
import com.example.realwhatsappclone.messages.adapters.MessageInserter;
import com.example.realwhatsappclone.messages.adapters.MessagePagedListAdapter;
import com.example.realwhatsappclone.users.adapter.MainThreadExecutor;
import com.example.realwhatsappclone.utills.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import static com.example.realwhatsappclone.utills.NodesNames.*;


import java.util.concurrent.Executors;

import javax.inject.Inject;

public class MessagesViewModel extends ViewModel {
    MessagePagedListAdapter adapter;
    @Inject
    MessageDataSourceFactory messageDataSourceFactory;
    LiveData<PagedList<Message>>dataSourceLiveData;

    MessageInserter messageInserter;
    PagedList.Config config;
    MainThreadExecutor executor;

    @Inject
    FirebaseDatabase database;
    @Inject
    FirebaseAuth auth;
    PagedList<Message> pagedList;
    Observer<PagedList<Message>> observer;
    String name;
    String phone;
    String uid;
    String imageRef;
    String dialogUid;


    ChildEventListener childEventListener= new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    public ObservableField<String> textOfMessage=new ObservableField<>();
    private static final String TAG = "MessagesViewModel";
    @Inject
    public MessagesViewModel(MessagePagedListAdapter adapter, PagedList.Config config, MainThreadExecutor executor,MessageInserter messageInserter) {
    this.adapter=adapter;

    this.config=config;
    this.executor=executor;

    this.messageInserter=messageInserter;

    }


     void setUid(String uid,RecyclerView recyclerView)
    {
        Log.d(TAG, "setUid: "+" "+(config==null));


        adapter.setUidOfCurrentUser(auth.getCurrentUser().getUid());
        recyclerView.setAdapter(adapter);
        messageDataSourceFactory.setSecondUid(uid);
        dataSourceLiveData= new LivePagedListBuilder<>(messageDataSourceFactory, config).setFetchExecutor(Executors.newSingleThreadExecutor()).build();
        dataSourceLiveData.observeForever(observer= messages -> {
            adapter.submitList(messages);


        });

        }






    public void setInfoAboutDialog(String name, String phone, String uid, String imageRef, String dialogUid) {
        this.name=name;
        this.phone=phone;
        this.uid=uid;
        this.imageRef=imageRef;
        this.dialogUid=dialogUid;
        if(dialogUid==null)
            this.dialogUid=auth.getUid().compareTo(uid)>=0?auth.getUid()+uid:uid+auth.getUid();
             Log.d(TAG, "setInfoAboutDialog: "+dialogUid);
             database.getReference().child(DIALOGS).child(this.dialogUid).child(MESSAGES).keepSynced(true);

        database.getReference().child(USERS).child(auth.getUid()).child(HAS_NEW_MESSAGES).addChildEventListener(childEventListener);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: "+adapter.getCurrentList().size());

        database.getReference().child(DIALOGS).child(dialogUid).child(MESSAGES).removeEventListener(childEventListener);
        dataSourceLiveData.removeObserver(observer);
    }

    public void sendMessage()
    {
        Log.d(TAG, "sendMessage: "+(textOfMessage.get()==null));
        if(textOfMessage.get()!=null)
        { messageInserter.insertMessage(textOfMessage.get(),null,uid).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<Resource<Message>>() {
            @Override
            public void onSubscribe(Disposable d) {
                
            }

            @Override
            public void onSuccess(Resource<Message> messageResource) {
                Log.d(TAG, "onSuccess: "+messageResource.data.getText());
              adapter.getCurrentList().getDataSource().invalidate();
            }

            @Override
            public void onError(java.lang.Throwable throwable) {
            }


        });
            textOfMessage.set(null);}
    }

    }
