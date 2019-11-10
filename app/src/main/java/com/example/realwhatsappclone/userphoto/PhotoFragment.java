package com.example.realwhatsappclone.userphoto;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.example.realwhatsappclone.BaseActivity;
import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.ViewModel.ViewModelsFactory;
import com.example.realwhatsappclone.databinding.FragmentPhotoBinding;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends DaggerFragment {
    private static final String TAG = "PhotoFragment";
    @Inject
    RequestManager requestManager;
    @Inject
    ViewModelsFactory factory;
    FragmentPhotoBinding binding;
    UserPhotoViewModel viewModel;
    public PhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding=DataBindingUtil.inflate(inflater,R.layout.fragment_photo,container,false);
      viewModel=ViewModelProviders.of(this,factory).get(UserPhotoViewModel.class);
      binding.setViewModel(viewModel);
        subscribeOnObservers();
        return binding.getRoot();
    }

    private void subscribeOnObservers()
    {
        viewModel.getIsPhotoRequired().observe(this,(onChange)->
        {
            if(onChange!=null)
                switch (onChange) {
                    case SelectPhoto: {
                        Log.d(TAG, "subscribeOnObservers: ");
                        Activity activity = this.getActivity();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(activity.getPackageManager()) != null)
                         startActivityForResult(intent, 1);
                        break;
                    }
                    case MoveToNext:
                    {
                        break;
                    }
                }
                });
        viewModel.getFragState().observe(this,(onChange)->
        {
            if(onChange!=null)
            {
                switch (onChange)
                {
                    case Error:
                    {
                        ((BaseActivity)getActivity()).showProgress(false);
                        ((BaseActivity)getActivity()).showError("Cannot save photo");
                        break;
                    }
                    case finished:
                    {
                        ((BaseActivity)getActivity()).showProgress(false);
                        ((BaseActivity)getActivity()).moveToMainActivity();
                        break;
                    }
                    case inProgress:
                    {
                        ((BaseActivity)getActivity()).showProgress(true);
                        break;
                    }
                }
            }
        });
    }
    @Override
   public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
           binding.dialogImage.setImageBitmap(imageBitmap);
            viewModel.savePhoto(imageBitmap);

        }
    }

}
