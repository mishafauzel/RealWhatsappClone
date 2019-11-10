package com.example.realwhatsappclone.dialogs.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.utills.Resource;

import org.w3c.dom.Text;

import javax.inject.Inject;

public class DialogPagetListAdapter extends PagedListAdapter<Dialog, DialogPagetListAdapter.DialogsViewHolder> {
    RequestManager glide;
    @Inject
    protected DialogPagetListAdapter(@NonNull DiffUtil.ItemCallback<Dialog> diffCallback,RequestManager glide) {
        super(diffCallback);
        this.glide=glide;
    }

    public static DialogPagetListAdapter getAdapter(DifCallbacksForDialogs diffCallback, RequestManager requestManager) {
        DialogPagetListAdapter adapter=new DialogPagetListAdapter(diffCallback,requestManager);
        return adapter;
    }

    @NonNull
    @Override
    public DialogsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DialogsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DialogsViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class DialogsViewHolder extends RecyclerView.ViewHolder
{
    TextView name,lastMessage;
    ImageView photo;
    public DialogsViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.first_line);
        lastMessage=itemView.findViewById(R.id.second_line);
        photo=itemView.findViewById(R.id.dialog_image);
    }

    public void bind(Dialog item) {


        String labelForItem=item.nameOfUser==null?item.phoneOfUser:item.nameOfUser;
        int sizeOfSubstrin=item.lastMessage.length()>=20?20:item.lastMessage.length();
        String message=item.lastMessage.substring(0,sizeOfSubstrin);
        name.setText(labelForItem);
        lastMessage.setText(message);
        glide.load(item.urlForImage).into(photo);
    }
}
}
