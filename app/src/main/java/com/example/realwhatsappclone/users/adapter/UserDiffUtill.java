package com.example.realwhatsappclone.users.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.realwhatsappclone.room.Users;
import com.example.realwhatsappclone.users.UserItem;

public class UserDiffUtill extends DiffUtil.ItemCallback<Users>{


    @Override
    public boolean areItemsTheSame(@NonNull Users oldItem, @NonNull Users newItem) {

        return oldItem.getUid().equals(newItem.getUid());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Users oldItem, @NonNull Users newItem) {
        boolean isSame=oldItem.getUid().equals(newItem.getUid())&&oldItem.getName().equals(newItem.getName())&&oldItem.getPhoneNumber().equals(newItem.getPhoneNumber());

        return isSame;
    }
}
