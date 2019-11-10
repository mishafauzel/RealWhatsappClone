package com.example.realwhatsappclone.login;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realwhatsappclone.BaseActivity;
import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.SessionManager;
import com.example.realwhatsappclone.ViewModel.ViewModelsFactory;
import com.example.realwhatsappclone.databinding.FragmentLoginBinding;
import com.example.realwhatsappclone.userphone.UserResourceAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends DaggerFragment {
   FragmentLoginBinding fragmentLoginBinding;
   @Inject
    FirebaseDatabase database;
    @Inject
    ViewModelsFactory modelsFactory;




    private static final String TAG = "LoginFragment";
    LoginViewModel viewModel;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel =ViewModelProviders.of(this,modelsFactory).get(LoginViewModel.class);
        fragmentLoginBinding.setLoginViewModel(viewModel);
        subscribeOnObservers();
        checkIsSignIn();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        return (fragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)).getRoot();
    }

  private void subscribeOnObservers()
    {
        viewModel.getAuthentificatedUser().removeObservers(getViewLifecycleOwner());
        viewModel.getAuthentificatedUser().observe(this,(onChange)->
        {
            Log.d(TAG, "subscribeOnObservers: changed");
            if(onChange!=null)
            switch (onChange.state)
            {
                case Authentificated:
                    Log.d(TAG, "subscribeOnObservers: succes");
                {((BaseActivity)this.getActivity()).showProgress(false);
                    checkPhoneExistence();


                break;}
                case NotAuthentificated:
                {
                    Log.d(TAG, "subscribeOnObservers: notAuthentificated");
                    ((BaseActivity)this.getActivity()).showProgress(false);
                    break;}
                case Loading:
                {
                    Log.d(TAG, "subscribeOnObservers: inProgress");
                    ((BaseActivity)this.getActivity()).showProgress(true);
                    break;
                }
                case Error:
                {
                    Log.d(TAG, "subscribeOnObservers: error");
                    ((BaseActivity)this.getActivity()).showProgress(false);
                    ((BaseActivity)this.getActivity()).showError(onChange.message);
                    break;
                }
            }
        });

    }



   private void checkIsSignIn()
    {
        viewModel.checkIsUserSignIn();
    }
  private void checkPhoneExistence()
  {
      viewModel.getPhoneIsExists().removeObservers(this.getViewLifecycleOwner());
      viewModel.getPhoneIsExists().observe(this,(onChange)->
      {
          Log.d(TAG, "checkPhoneExistence: "+onChange.getStatus());
          switch (onChange.getStatus())
          {
              case Succes:
              {
                  ((BaseActivity)getActivity()).showProgress(false);
                  ((BaseActivity)getActivity()).moveToMainActivity();
                  break;
              }
              case Error:
              {
                  ((BaseActivity)getActivity()).showProgress(false);
                  ((BaseActivity)getActivity()).moveToPhoneFragment();

                  break;
              }
              case Loading:
              {
                  ((BaseActivity)getActivity()).showProgress(true);
                  break;
              }
          }
      });
      viewModel.isPhoneExists();
  }

}
