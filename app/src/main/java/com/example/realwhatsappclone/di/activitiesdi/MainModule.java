package com.example.realwhatsappclone.di.activitiesdi;

import android.content.res.Resources;
import android.util.Log;

import androidx.paging.PagedList;

import com.bumptech.glide.RequestManager;
import com.example.realwhatsappclone.dialogs.adapters.DialogsRepositories;
import com.example.realwhatsappclone.dialogs.adapters.DialogPagetListAdapter;
import com.example.realwhatsappclone.dialogs.adapters.DialogsDatasource;
import com.example.realwhatsappclone.dialogs.adapters.DifCallbacksForDialogs;
import com.example.realwhatsappclone.messages.MessageFragment;
import com.example.realwhatsappclone.messages.adapters.DiffCallbackForMessages;
import com.example.realwhatsappclone.messages.adapters.MessageDataSource;
import com.example.realwhatsappclone.messages.adapters.MessageDataSourceFactory;
import com.example.realwhatsappclone.messages.adapters.MessageInserter;
import com.example.realwhatsappclone.messages.adapters.MessagePagedListAdapter;
import com.example.realwhatsappclone.messages.adapters.MessageRepository;
import com.example.realwhatsappclone.users.adapter.MainThreadExecutor;
import com.example.realwhatsappclone.users.adapter.UserDiffUtill;
import com.example.realwhatsappclone.users.adapter.UserPagedListAdapter;
import com.example.realwhatsappclone.users.adapter.UserPositionalDataSource;
import com.example.realwhatsappclone.users.adapter.UsersRepositories;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.Executors;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    private static final String TAG = "MainModule";

    @Provides
    public UserPagedListAdapter provideUserAdapter(UserDiffUtill diffUtill, RequestManager requestManager) {
        Log.d(TAG, "provideUserAdapter: ");
        return UserPagedListAdapter.provideAdapter(diffUtill, requestManager);
    }

    @Provides
    public UserDiffUtill provideUserDiffUtikk() {
        Log.d(TAG, "provideUserDiffUtikk: ");
        return new UserDiffUtill();
    }

    @Named("users")
    @Provides
    public PagedList.Builder provideUserPagedList(UserPositionalDataSource dataSource, PagedList.Config config, MainThreadExecutor mainThreadExecutor) {
        Log.d(TAG, "provideUserPagedList: ");
        return new PagedList.Builder(dataSource, config).setNotifyExecutor(mainThreadExecutor)
                .setFetchExecutor(Executors.newSingleThreadExecutor());

    }

    @Provides
    public PagedList.Config provideConfig() {
        Log.d(TAG, "provideConfig: ");
        return new PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(30).build();
    }

    @Provides
    public UserPositionalDataSource provideUserDatasource(UsersRepositories repositories) {
        Log.d(TAG, "provideUserDatasource: ");
        return new UserPositionalDataSource(repositories);
    }

    @Provides
    public MainThreadExecutor provideMainThreadExecutor() {
        return new MainThreadExecutor();
    }



    @Provides
    public DialogPagetListAdapter provideDialogsAdapter(DifCallbacksForDialogs diffCallback, RequestManager requestManager) {
        return DialogPagetListAdapter.getAdapter(diffCallback, requestManager);
    }

    @Provides
    public DifCallbacksForDialogs provideDiffCalbackForDialogs() {
        return new DifCallbacksForDialogs();
    }

    @Named("dialogs")
    @Provides
    public PagedList.Builder provideDialogsPageList(DialogsDatasource dataSource, PagedList.Config config, MainThreadExecutor mainThreadExecutor)
    {
        return new PagedList.Builder(dataSource, config).setNotifyExecutor(mainThreadExecutor)
                .setFetchExecutor(Executors.newSingleThreadExecutor());

    }
    @Provides
    public MessagePagedListAdapter provideMessageAdapter(DiffCallbackForMessages diffCallback, RequestManager requestManager,Resources resources) {
        return new MessagePagedListAdapter(diffCallback, requestManager,resources);
    }

    @Provides
    public DiffCallbackForMessages provideDiffCalbackForMessages() {
        return new DiffCallbackForMessages();
    }


    @Provides
    public MessageDataSourceFactory provideMessageDataSource(MessageRepository repository)
    {


        return new MessageDataSourceFactory(repository);
    }
    @Provides
    public DialogsDatasource provideDialogsDataSource(DialogsRepositories repository)
    {
        return new DialogsDatasource(repository);
    }
    @Provides
    public MessageInserter provideMessageInserter(FirebaseDatabase database, FirebaseAuth auth, StorageReference storageReference, Resources resources)
    {
        return new MessageInserter(auth,database,storageReference,resources);
    }
}
