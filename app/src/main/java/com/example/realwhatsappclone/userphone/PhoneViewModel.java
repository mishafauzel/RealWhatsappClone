package com.example.realwhatsappclone.userphone;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.realwhatsappclone.utills.IcoToPhone.getPhone;

import com.example.realwhatsappclone.R;
import com.example.realwhatsappclone.SessionManager;
import com.example.realwhatsappclone.utills.IcoToPhone;

import javax.inject.Inject;

public class PhoneViewModel extends ViewModel {
    TelephonyManager telecomManager;
    private static final String TAG = "PhoneViewModel";
    private SessionManager sessionManager;
    public ObservableField<String> phoneNumber=new ObservableField<>();
    public ObservableField<String> name=new ObservableField<>();
    public ObservableBoolean border=new ObservableBoolean(false);


    private MediatorLiveData<UserResourceAuth> userPhoneSaving=new MediatorLiveData<>();


    public LiveData<UserResourceAuth> getUserPhoneSaving() {
        return userPhoneSaving;
    }


    @Inject
    public PhoneViewModel(SessionManager sessionManager,TelephonyManager telecomManager) {
        this.sessionManager = sessionManager;
        this.telecomManager=telecomManager;


    }

    public void savePhoneNumber()
    {
        border.set(false);

        if(phoneNumber.get().length()<10)
        {
            Log.d(TAG, "savePhoneNumber: ");
            border.set(true);
            return;
        }
        String IcoPrefix=getCountryISO();
        if(phoneNumber.get().length()==11)
        {
            Log.d(TAG, "savePhoneNumber: "+IcoPrefix);
           phoneNumber.set(phoneNumber.get().replaceFirst("8",IcoPrefix));
        }
        else
        {
            phoneNumber.set(IcoPrefix+phoneNumber.get());
        }
        userPhoneSaving.setValue(UserResourceAuth.Loading());
        final LiveData<UserResourceAuth> liveData= LiveDataReactiveStreams.fromPublisher(sessionManager.safeMyPhoneNumber(phoneNumber.get(),name.get()));
        userPhoneSaving.addSource(liveData,(onChanged)->
        {
            userPhoneSaving.setValue(liveData.getValue());
            userPhoneSaving.removeSource(liveData);
        });
    }
    private String getCountryISO()
    {
        String countryISO=null;

        if(telecomManager.getNetworkCountryIso()!=null)
            if(!telecomManager.getNetworkCountryIso().equals(""))
                countryISO=telecomManager.getNetworkCountryIso();
        return IcoToPhone.getPhone(countryISO);

    }


}
