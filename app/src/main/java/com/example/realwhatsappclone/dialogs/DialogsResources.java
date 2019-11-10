package com.example.realwhatsappclone.dialogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.realwhatsappclone.dialogs.adapters.Dialog;
import com.example.realwhatsappclone.userphone.UserResourceAuth;
import com.google.android.gms.common.internal.DialogRedirect;

import java.util.List;

public class DialogsResources {
    public DialogsResources(@NonNull Status status, @Nullable List<Dialog> data) {
        this.status = status;
        this.data = data;
    }

    Status status;
    List<Dialog> data;
    public static DialogsResources succes(List<Dialog> data)
    {
        return new DialogsResources(Status.Succes,data);
    }
    public static DialogsResources loaading(List<Dialog>data)
    {
        return new DialogsResources(Status.Loading,null);
    }
    public static DialogsResources error()
    {
    return new DialogsResources(Status.Error,null);
    }

    enum Status{Succes,Loading,Error}
}
