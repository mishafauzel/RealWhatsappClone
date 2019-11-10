package com.example.realwhatsappclone.dialogs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.realwhatsappclone.MainActivity;
import com.example.realwhatsappclone.MainApplication;
import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.SessionManager;
import com.example.realwhatsappclone.ViewModel.ViewModelsFactory;
import com.example.realwhatsappclone.databinding.FragmentDialogsBinding;

import com.example.realwhatsappclone.room.UserDatabase;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerFragment;


public class DialogsFragment extends DaggerFragment  {
    private static final String TAG = "DialogsFragment";
    @Inject
     ViewModelsFactory modelsFactory;
    @Inject
     SessionManager sessionManager;
    @Inject
    MainApplication application;

    FragmentDialogsBinding binding;

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }


    private DialogsViewModel viewModel;

    public DialogsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewModel = ViewModelProviders.of(this, modelsFactory).get(DialogsViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialogs, container, false);

        binding.setViewModel(viewModel);
        initRexyclerView();
         subscribeOnObservables();
        binding.toolbar.inflateMenu(R.menu.menu);
        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sessionManager.logout();
                ((MainActivity)getActivity()).moveToBaseFragment();
                return true;
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: 1");
    }

    private void subscribeOnObservables() {
        viewModel.getMoveToUserFrg().observe(this,(onChange)->
        {
            if(onChange!=null)
            if(onChange==true) {
                moveToUserFragment();
                viewModel.getMoveToUserFrg().setValue(null);
            }
        });
    }

    public void moveToUserFragment()
    {
        ((MainActivity)getActivity()).moveToUserFragment();
    }



    private void initRexyclerView()
    {
        binding.recViewOfDialogs.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL,false));
        viewModel.initRecyclerView(binding.recViewOfDialogs);
    }
}
