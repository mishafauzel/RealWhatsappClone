package com.example.realwhatsappclone.userphone;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.realwhatsappclone.BaseActivity;
import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.ViewModel.ViewModelsFactory;
import com.example.realwhatsappclone.databinding.FragmentPhoneBinding;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhoneFragment extends DaggerFragment {
    @Inject
    ViewModelsFactory viewModelsFactory;
    private PhoneViewModel phoneViewModel;

    public PhoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentPhoneBinding phoneBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_phone,container,false);
        phoneBinding.setViewModel(phoneViewModel= ViewModelProviders.of(this,viewModelsFactory).get(PhoneViewModel.class));
        subscribeOnLiveData();

        return phoneBinding.getRoot();
    }
    public void subscribeOnLiveData()
    {
        phoneViewModel.getUserPhoneSaving().observe(this,(onChange)->
        {
            if(onChange!=null)
            {
                switch (onChange.getStatus())
                {
                    case Error: {
                        ((BaseActivity)getActivity()).showProgress(false);
                        ((BaseActivity) getActivity()).showError(onChange.message);
                        break;
                    }
                    case Loading:
                    {
                        ((BaseActivity)getActivity()).showProgress(true);
                        break;
                    }
                    case Succes:
                    {
                        ((BaseActivity)getActivity()).showProgress(false);
                        ((BaseActivity)getActivity()).moveToPhotoFragment();
                    }
                    case Cancel:
                    {

                    }
                }
            }
        });
    }

}
