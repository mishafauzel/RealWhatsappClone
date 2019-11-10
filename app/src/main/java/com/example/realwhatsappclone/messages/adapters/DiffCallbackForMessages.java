package com.example.realwhatsappclone.messages.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class DiffCallbackForMessages extends DiffUtil.ItemCallback<Message> {
    private static final String TAG = "DiffCallbackForMessages";
    @Override
    public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
        Log.d(TAG, "areItemsTheSame: "+oldItem.uid.equals(newItem.uid));
        return oldItem.uid.equals(newItem.uid);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
        boolean areConrentSame=oldItem.date==newItem.date&&oldItem.senderUId.equals(newItem.senderUId);
        if(newItem.text!=null&&oldItem.text!=null&& areConrentSame)
            areConrentSame=areConrentSame&&newItem.text.equals(oldItem.text);
        if(newItem.imagesReferensec!=null&&oldItem.imagesReferensec!=null&&areConrentSame)
        {
            areConrentSame=areConrentSame&&newItem.imagesReferensec.length==oldItem.imagesReferensec.length;
            if (areConrentSame) {
            for(int i=0;i<newItem.imagesReferensec.length;i++)
            {
                areConrentSame=areConrentSame&&newItem.imagesReferensec[i].equals(oldItem.imagesReferensec[i]);
                if(!areConrentSame)
                    break;
                else continue;
            }
            }

                }


        Log.d(TAG, "areContentsTheSame: "+areConrentSame);

        return areConrentSame;
    }

    }

