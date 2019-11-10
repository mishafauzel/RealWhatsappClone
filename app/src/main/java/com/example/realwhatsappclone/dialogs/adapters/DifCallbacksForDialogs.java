package com.example.realwhatsappclone.dialogs.adapters;

import com.example.realwhatsappclone.utills.Resource;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import javax.inject.Inject;

public class DifCallbacksForDialogs extends DiffUtil.ItemCallback<Dialog> {
    @Inject
    public DifCallbacksForDialogs() {
    }

    @Override
    public boolean areItemsTheSame(@NonNull Dialog oldItem, @NonNull Dialog newItem) {
        return oldItem.Uid.equals(newItem.Uid);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Dialog oldItem, @NonNull Dialog newItem) {
        return (
                oldItem.lastMessage.equals(newItem.lastMessage)
                );
    }
}
