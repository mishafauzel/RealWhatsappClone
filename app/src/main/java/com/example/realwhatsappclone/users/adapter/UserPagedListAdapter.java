package com.example.realwhatsappclone.users.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.realwhatsappclone.MainActivity;
import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.room.Users;
import com.example.realwhatsappclone.users.UserItem;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserPagedListAdapter extends PagedListAdapter<Users, UserPagedListAdapter.UserViewHolder> {
    private static final String TAG = "UserPagedListAdapter";
    RequestManager glide;
    @Inject
    protected UserPagedListAdapter(@NonNull DiffUtil.ItemCallback<Users> diffCallback,RequestManager glide) {
        super(diffCallback);
        this.glide=glide;
    }
    public static UserPagedListAdapter provideAdapter(UserDiffUtill diffUtill,RequestManager glide)
    {
        return new UserPagedListAdapter(diffUtill,glide);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class UserViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        TextView firstLine,secondLine;
        CircleImageView imageView;
        Users item;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            firstLine=view.findViewById(R.id.first_line);
            secondLine=view.findViewById(R.id.second_line);
            imageView=view.findViewById(R.id.dialog_image);
        }

        public void bind(Users item) {
            this.item=item;
            firstLine.setText(item.getName());
            secondLine.setText(item.getPhoneNumber());
            Log.d(TAG, "bind: "+item.getUidofImage());
            glide.load(item.getUidofImage()).into(imageView);
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) view.getContext()).moveToMessageFragment(item.getUid(),item.getName(),item.getPhoneNumber(),item.getUidofImage());

                }
            });

        }
    }
}
