package com.example.realwhatsappclone.users;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.pdf.PdfDocument;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.PagedList;
import androidx.paging.PositionalDataSource;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realwhatsappclone.room.UserDatabase;
import com.example.realwhatsappclone.room.Users;
import com.example.realwhatsappclone.users.adapter.MainThreadExecutor;
import com.example.realwhatsappclone.users.adapter.UserDiffUtill;
import com.example.realwhatsappclone.users.adapter.UserPagedListAdapter;
import com.example.realwhatsappclone.users.adapter.UserPositionalDataSource;
import com.example.realwhatsappclone.users.adapter.UsersRepositories;
import com.example.realwhatsappclone.utills.IcoToPhone;
import com.google.firebase.database.FirebaseDatabase;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.jar.Attributes;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UsersViewModel extends ViewModel {




    UserPagedListAdapter adapter;

    DataSource dataSource;
    PagedList.Builder pagedList;
    @Inject
    public UsersViewModel(  UserPagedListAdapter adapter,@Named("users")PagedList.Builder pagedList) {
        this.pagedList=pagedList;

        this.adapter = adapter;



    }

    public void initRecyclerView(RecyclerView recView) {
        PagedList list=pagedList.build();
         dataSource=list.getDataSource();

        recView.setAdapter(adapter);
        adapter.submitList(list);
    }
}
