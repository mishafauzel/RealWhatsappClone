package com.example.realwhatsappclone.dialogs;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.schedulers.Schedulers;

import com.example.realwhatsappclone.SessionManager;
import com.example.realwhatsappclone.dialogs.adapters.Dialog;
import com.example.realwhatsappclone.dialogs.adapters.DialogPagetListAdapter;
import com.example.realwhatsappclone.room.UserDatabase;
import com.example.realwhatsappclone.users.adapter.MainThreadExecutor;
import com.example.realwhatsappclone.utills.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Named;

public class DialogsViewModel extends ViewModel {
    private static final String TAG = "DialogsViewModel";

    private PagedList.Builder pagedListBuilder;
    private DialogPagetListAdapter adapter;
    private PagedList<Dialog> pagedList=null;

    private MutableLiveData<Boolean> moveToUserFrg=new MutableLiveData<>(false);
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Inject
    MainThreadExecutor mainThreadExecutor;


    @Inject
    public DialogsViewModel(FirebaseAuth auth, FirebaseDatabase database, UserDatabase userDatabase, @Named("dialogs")PagedList.Builder listBuilder, DialogPagetListAdapter adapter) {
        Log.d(TAG, "DialogsViewModel: ");
        this.database=database;
        this.auth=auth;
        this.adapter=adapter;
        pagedListBuilder=listBuilder;



    }

    public MutableLiveData<Boolean> getMoveToUserFrg() {
        return moveToUserFrg;
    }
    public void moveToUsersFragment()
    {
        moveToUserFrg.setValue(true);
    }

    public void initRecyclerView(RecyclerView recViewOfDialogs) {
        if(pagedList==null)
    adapter.submitList(pagedList=pagedListBuilder.setFetchExecutor(Executors.newSingleThreadExecutor()).setNotifyExecutor(mainThreadExecutor).build());
    recViewOfDialogs.setAdapter(adapter);
    }


}
