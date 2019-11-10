package com.example.realwhatsappclone.users.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import com.example.realwhatsappclone.room.Users;

import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserPositionalDataSource extends PositionalDataSource<Users> {
    private UsersRepositories userRepository;
    private static final String TAG = "UserPositionalDataSourc";
    @Inject
    public UserPositionalDataSource(UsersRepositories userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Users> callback) {
        Observable.fromCallable(new Callable<List<Users>>() {
            @Override
            public List<Users> call() throws Exception {
                Log.d(TAG, "call: "+params.requestedStartPosition+", "+params.requestedLoadSize);
                return userRepository.dao.getusersByPage(params.requestedLoadSize,params.requestedStartPosition);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Users>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Users> users) {
                Log.d(TAG, "onNext: "+users.size());
                callback.onResult(users,params.requestedStartPosition);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Users> callback) {
        Observable.fromCallable(new Callable<List<Users>>() {
            @Override
            public List<Users> call() throws Exception {
                return userRepository.dao.getusersByPage(params.startPosition,params.loadSize);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Users>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Users> users) {
                callback.onResult(users);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
}
