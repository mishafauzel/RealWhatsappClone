package com.example.realwhatsappclone.messages.adapters;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.realwhatsappclone.R;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MessagePagedListAdapter extends PagedListAdapter<Message, MessagePagedListAdapter.MessageViewHolder> {
    RequestManager requestManager;
    private static final String TAG = "MessagePagedListAdapter";
    int i=0;
    Calendar currentDate;
    String uidOfCurrentUser;
    String[] months;
    public void setUidOfCurrentUser(String uidOfCurrentUser) {
        this.uidOfCurrentUser = uidOfCurrentUser;
    }

    @Inject
    public MessagePagedListAdapter(@NonNull DiffUtil.ItemCallback<Message> diffCallback, RequestManager requestManager, Resources resources) {
        super(diffCallback);
        this.requestManager=requestManager;
        currentDate=Calendar.getInstance(TimeZone.getDefault());
        currentDate.setTimeInMillis(new Date().getTime());
        months=resources.getStringArray(R.array.months);
        setHasStableIds(true);

    }

    @Override
    public long getItemId(int position) {
        return getItem(position).date;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: "+i++);

        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
    holder.bindItem(getItem(position));
    if(position==0)
    {holder.checkWithCalendarDate(currentDate);}
    if(position>=1)
    holder.checkWithCalendarDate(getItem(position-1).calendar);
        Log.d(TAG, "onBindViewHolder: "+i++);    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        TextView textofMessage,timeOfMessages,dayOfMessages;
        LinearLayout imageContainer;
        CardView container;
        Message message;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textofMessage =itemView.findViewById(R.id.text_of_message);
            imageContainer=itemView.findViewById(R.id.container_for_images);
            timeOfMessages=itemView.findViewById(R.id.time);
            container=itemView.findViewById(R.id.date_container);
            dayOfMessages=itemView.findViewById(R.id.day_of_messages);
        }

        public void bindItem(Message message) {
            this.message =message;
            if(message.getText()!=null)
                this.textofMessage.setText(message.getText());
                this.timeOfMessages.setText(message.displayingTime);
                if(message.imagesReferensec!=null)
                    { for (String imageRef :
                                message.imagesReferensec) {
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            ImageView imageView = new ImageView(this.textofMessage.getContext());
                            imageView.setLayoutParams(params);
                            imageContainer.addView(imageView);
                            requestManager.load(imageRef).into(imageView);
                        }
                    }
        }


        private void setDateHeader()
        {
            container.setVisibility(View.VISIBLE);
            dayOfMessages.setText(message.calendar.get(Calendar.DAY_OF_MONTH)+" "+ months[message.calendar.get(Calendar.MONTH)]);

        }
        private void hideHeader()
        {
            container.setVisibility(View.GONE);
        }

        public void checkWithCalendarDate(Calendar calendar) {
            if(message.calendar.get(Calendar.DAY_OF_MONTH)!=calendar.get(Calendar.DAY_OF_MONTH))
            {
                setDateHeader();
                  return;
            }
            if(message.calendar.get(Calendar.MONTH)!=calendar.get(Calendar.DAY_OF_MONTH))
            {
                setDateHeader();
                return;
            }
            if(message.calendar.get(Calendar.YEAR)!=calendar.get(Calendar.YEAR))
            {
                setDateHeader();
                return;
            }
            hideHeader();

        }
    }
}

