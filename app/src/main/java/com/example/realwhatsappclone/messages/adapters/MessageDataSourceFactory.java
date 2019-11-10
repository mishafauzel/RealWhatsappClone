package com.example.realwhatsappclone.messages.adapters;

import javax.inject.Inject;

import androidx.paging.DataSource;

public class MessageDataSourceFactory extends DataSource.Factory<Long,Message> {
    String secondUid;
    MessageRepository repository;
    @Inject
    public MessageDataSourceFactory(MessageRepository repository)
    {
        this.repository=repository;
    }
    @Override
    public DataSource<Long, Message> create() {
        return new MessageDataSource(repository).setUserUid(secondUid);
    }

    public void setSecondUid(String uid) {
        this.secondUid=uid;
    }
}
