package com.example.realwhatsappclone.messages.adapters;

import android.util.Log;

import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.dialogs.adapters.Dialog;
import com.example.realwhatsappclone.utills.Resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MessageDataSource extends ItemKeyedDataSource<Long,Message> {
    MessageRepository repository;
    private String userUid=null;
    private static final String TAG = "MessageDataSource";
    public MessageDataSource(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Message> callback) {
        Log.d(TAG, "loadInitial: "+userUid);

        repository.getMessages(userUid,params.requestedLoadSize,params.requestedInitialKey,false)
                .observeOn(AndroidSchedulers.mainThread()).toList().subscribe(new SingleObserver<List<Message>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onSuccess(List<Message> messages) {
                Collections.reverse(messages);
                Log.d(TAG, "onSuccess: "+messages.get(0)+" "+messages.size());

                callback.onResult(messages);
                Log.d(TAG, "onSuccess: ");

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: "+e.getMessage());
            }
        });}

    @Override
    public void loadBefore(@NonNull LoadParams params, @NonNull LoadCallback callback) {
        Log.d(TAG, "loadBefore: ");
        repository.getMessages(userUid,params.requestedLoadSize,(Long) params.key,true).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).toList().subscribe(new SingleObserver<List<Message>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<Message> messages) {
                Collections.reverse(messages);
                callback.onResult(messages);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: "+e.getMessage());

            }
        });

    }

    @Override
    public void loadAfter(@NonNull LoadParams params, @NonNull LoadCallback callback) {
        repository.getMessages(userUid,params.requestedLoadSize,(Long)params.key,false).toList().subscribe(new SingleObserver<List<Message>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onSuccess(List<Message> messages) {
                Collections.reverse(messages);
                Log.d(TAG, "onSuccess: "+messages);
                callback.onResult(messages);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }
        });}


    public MessageDataSource setUserUid(String userUid) {
        this.userUid=userUid;
        return this;

    }

    @NonNull
    @Override
    public Long getKey(@NonNull Message item) {
        return item.date;
    }
}
