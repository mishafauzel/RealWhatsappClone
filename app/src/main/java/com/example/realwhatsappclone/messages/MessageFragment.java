package com.example.realwhatsappclone.messages;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.example.realwhatsappclone.MainApplication;
import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.SessionManager;
import com.example.realwhatsappclone.ViewModel.ViewModelsFactory;
import com.example.realwhatsappclone.databinding.FragmentMessageBinding;


import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class MessageFragment extends DaggerFragment {
    @Inject
     ViewModelsFactory factory;
    @Inject
     RequestManager glide;
    @Inject
    MainApplication application;
    @Inject
    SessionManager sessionManager;
    MessagesViewModel viewModel;
    FragmentMessageBinding binding;
    String uid;
    String phone;

    private static final String TAG = "MessageFragment";
    public MessageFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.recView.setAdapter(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewModel= ViewModelProviders.of(this,factory).get(MessagesViewModel.class);
        Bundle bundle=getArguments();
        String name=bundle.getString("name");
        phone=bundle.getString("phone");
        uid=bundle.getString("uid");
        String imageRef=bundle.getString("image");
        String dialogUid=bundle.getString("dialogUid");

         binding= DataBindingUtil.inflate(inflater,R.layout.fragment_message,container,false);
        viewModel.setInfoAboutDialog(name,phone,uid,imageRef,dialogUid);
         glide.load(imageRef).into(binding.dialogImage);
        String text=name==null? phone:name;
        binding.name.setText(text);
        binding.setViewModel(viewModel);
        instanciateRecyclerView();
        binding.toolbar.inflateMenu(R.menu.messages_menu);
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId())
            {
                case R.id.logout:

                {
                    sessionManager.logout();

                    break;
                }
                case R.id.call:
                {
                    Intent intent=new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+phone));
                    Log.d(TAG, "onCreateView: ");
                    if(intent.resolveActivity(getActivity().getPackageManager())!=null)
                    startActivity(intent);
                    break;
                }
            }
            return true;
        });
        return binding.getRoot();
    }



    private void instanciateRecyclerView()
    {
        binding.recView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL,false));
        viewModel.setUid(uid,binding.recView);
    }

}
