package com.example.realwhatsappclone.dialogs.adapters;

import android.util.Log;

import com.example.realwhatsappclone.utills.Resource;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DialogsDatasource extends ItemKeyedDataSource<String, Dialog> {
  @Inject
    FirebaseAuth auth;
    @Inject
    public DialogsDatasource(DialogsRepositories repositories) {
        this.repositories = repositories;
    }

    DialogsRepositories repositories;
    String secondUid;
    CompositeDisposable disposable =new CompositeDisposable();
    private static final String TAG = "DialogsDatasource";


    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Dialog> callback) {
 repositories.getDialogs(params.requestedLoadSize,0l).toList().subscribe(new SingleObserver<List<Dialog>>() {
      @Override
      public void onSubscribe(Disposable disposable) {

      }

      @Override
      public void onSuccess(List<Dialog> dialogs) {
          Log.d(TAG, "onSuccess: after all"+dialogs.size());
          callback.onResult(dialogs);
      }

      @Override
      public void onError(Throwable throwable) {

      }
    });
    }





    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<Dialog> callback) {


    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<Dialog> callback) {

    }

    @NonNull
    @Override
    public String getKey(@NonNull Dialog item) {
        return String.valueOf(item.date);
    }
}
