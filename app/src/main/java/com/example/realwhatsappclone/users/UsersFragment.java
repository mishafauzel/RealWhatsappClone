package com.example.realwhatsappclone.users;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.ViewModel.ViewModelsFactory;
import com.example.realwhatsappclone.databinding.FragmentUsersBinding;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends DaggerFragment {
    @Inject
    ViewModelsFactory modelsFactory;
    @Inject
    FirebaseAuth auth;
    UsersViewModel viewModel;
    FragmentUsersBinding binding;
    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.recView.setAdapter(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel= ViewModelProviders.of(this,modelsFactory).get(UsersViewModel.class);
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_users,container,false);
        binding.toolbar.inflateMenu(R.menu.menu);
        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                auth.signOut();
                return true;
            }

        });
        binding.recView.setLayoutManager(new LinearLayoutManager(this.getActivity(), RecyclerView.VERTICAL,false));
        viewModel.initRecyclerView(binding.recView);



        return binding.getRoot();
    }


}
