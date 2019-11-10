package com.example.realwhatsappclone.login;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.SessionManager;
import com.example.realwhatsappclone.userphone.UserResourceAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {
    private static final String TAG = "LoginViewModel";
    public enum StateOfLogin {Login,Register,RemindPassword}
    public ObservableField<String> email=new ObservableField<>();
    public ObservableField<String> password=new ObservableField<>();
    public ObservableField<String> passwordConfirmation=new ObservableField<>();
    public ObservableInt emailVisibility=new ObservableInt(View.VISIBLE);
    public ObservableInt passwordVisibility=new ObservableInt(View.VISIBLE);
    public ObservableInt passworConfirmVisibility=new ObservableInt(View.GONE);
    public ObservableInt loginTextVisibility=new ObservableInt(View.GONE);
    public ObservableInt remindPassVisibility=new ObservableInt(View.VISIBLE);
    public ObservableInt registerTextVisibility=new ObservableInt(View.VISIBLE);
    public ObservableField<String> textInButton=new ObservableField<>("Login");
    private StateOfLogin stateOfLogin= StateOfLogin.Login;
    private SessionManager sessionManager;

    public MediatorLiveData<UserResourceAuth> getPhoneIsExists() {
        return phoneIsExists;
    }

    private MediatorLiveData<AuthResource<FirebaseUser>> authentificatedUser =new MediatorLiveData<>();
    private MediatorLiveData<UserResourceAuth> phoneIsExists=new MediatorLiveData<>();
    private final String[] arrayOfTextsInButton;
    @Inject
    public LoginViewModel(SessionManager sessionManager, Resources resources) {

         this.sessionManager=sessionManager;
          this.arrayOfTextsInButton=resources.getStringArray(R.array.text_button);
    }


    public LiveData<AuthResource<FirebaseUser>> getAuthentificatedUser() {
            return authentificatedUser;
        }

        public void buttonClicked(View view)
        {
            Log.d(TAG, "buttonClicked: "+stateOfLogin);
            switch (stateOfLogin)
            {
                case Login:
                {
                    login();
                    break;
                }
                case Register:
                {
                    register();
                    break;
                }
                case RemindPassword:
                {
                    remindMyPassword();
                    break;
                }
            }}
        public void remindMyPassword()
        {}
        private void login()
        {
            Log.d(TAG, "login: "+email.get()+" "+password.get());
            if(!checkEmail(email.get()))
            {
                authentificatedUser.setValue(AuthResource.Error("Please enter correct email",null));
            }
            authentificatedUser.setValue(AuthResource.loading());
            final LiveData<AuthResource<FirebaseUser>> liveData= LiveDataReactiveStreams
                    .fromPublisher(sessionManager.authentificateUsesr(email.get(),password.get()));
            authentificatedUser.addSource(liveData,(onChange)->
            {
                if(onChange!=null) {
                    authentificatedUser.setValue(onChange);
                    authentificatedUser.removeSource(liveData);
                }
            });
        }
        public void checkIsUserSignIn()
        {
            authentificatedUser.setValue(sessionManager.checkIsAthentificated());
        }
        private boolean checkEmail(String email)
        {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";

            Pattern pat = Pattern.compile(emailRegex);
            if (email == null)
                return false;
            return pat.matcher(email).matches();
        }
        private void register()
        {
            Log.d(TAG, "register: "+email.get()+" "+password.get()+" "+passwordConfirmation.get());
            authentificatedUser.setValue(AuthResource.loading());
            if(!checkEmail(email.get()))
            {
                authentificatedUser.setValue(AuthResource.Error("Please enter correct email",null));
                return;
            }
            if(!password.get().equals(passwordConfirmation.get()))
            {
                authentificatedUser.setValue(AuthResource.Error("Password and password confirmation not the same",null));
                return;
            }
            final  LiveData<AuthResource<FirebaseUser>> observable=LiveDataReactiveStreams
                    .fromPublisher(sessionManager.createAccount(email.get(),password.get() ));
            authentificatedUser.addSource(observable,(onChange)->
            {
                Log.d(TAG, "register: succes");
                if(onChange!=null) {
                    authentificatedUser.setValue(onChange);
                    authentificatedUser.removeSource(observable);
                }
            });
        }
        public void switchState(View view)
        {
            switch (view.getId())
            {
                case R.id.login_state: {
                    Log.d(TAG, "switchState: login state");
                    emailVisibility.set(View.VISIBLE);
                    passwordVisibility=new ObservableInt(View.VISIBLE);
                    passworConfirmVisibility.set(View.GONE);
                    loginTextVisibility.set(View.GONE);
                    remindPassVisibility.set(View.VISIBLE);
                    registerTextVisibility.set(View.VISIBLE);
                    textInButton.set(arrayOfTextsInButton[0]);
                    stateOfLogin= StateOfLogin.Login;
                    Log.d(TAG, "switchState: "+stateOfLogin);

                    break;

                }
                case R.id.remind_my_password_state:
                {
                    Log.d(TAG, "switchState: remind my password");
                    emailVisibility.set(View.VISIBLE);
                    passwordVisibility=new ObservableInt(View.GONE);
                    passworConfirmVisibility.set(View.GONE);
                    loginTextVisibility.set(View.VISIBLE);
                    remindPassVisibility.set(View.GONE);
                    registerTextVisibility.set(View.VISIBLE);
                      textInButton.set(arrayOfTextsInButton[2]);
                    stateOfLogin= StateOfLogin.RemindPassword;
                    Log.d(TAG, "switchState: "+stateOfLogin);
                    break;
                }
                case R.id.create_new_user_state:
                {
                    Log.d(TAG, "switchState: crearte new user");
                    emailVisibility.set(View.VISIBLE);
                    passwordVisibility=new ObservableInt(View.VISIBLE);
                    passworConfirmVisibility.set(View.VISIBLE);
                    loginTextVisibility.set(View.VISIBLE);
                    remindPassVisibility.set(View.VISIBLE);
                    registerTextVisibility.set(View.GONE);
                    textInButton.set(arrayOfTextsInButton[1]);
                    stateOfLogin=StateOfLogin.Register;
                    Log.d(TAG, "switchState: "+stateOfLogin);
                    break;
                }

            }
        }
    public void isPhoneExists()
    {
        phoneIsExists.setValue(UserResourceAuth.Loading());
        LiveData<UserResourceAuth>liveData=LiveDataReactiveStreams.fromPublisher(sessionManager.checkUserPhoneExistence());
        phoneIsExists.addSource(liveData,(onChanged)->
        {
           if(onChanged!=null);
            {
                phoneIsExists.setValue(onChanged);
                phoneIsExists.removeSource(liveData);
            }
        });

    }

    }





