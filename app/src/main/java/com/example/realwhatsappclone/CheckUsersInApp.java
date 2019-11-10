package com.example.realwhatsappclone;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.realwhatsappclone.room.UserDatabase;
import com.example.realwhatsappclone.room.Users;
import com.example.realwhatsappclone.room.UsersDao;
import com.example.realwhatsappclone.utills.IcoToPhone;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import dagger.android.DaggerIntentService;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import static com.example.realwhatsappclone.utills.NodesNames.*;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class CheckUsersInApp extends DaggerIntentService {
    private static final String TAG = "CheckUsersInApp";
    @Inject
    ContentResolver contentResolver;
    @Inject
    FirebaseDatabase database;
    @Inject
    FirebaseAuth auth;
    @Inject
    UserDatabase userDatabase;
    @Inject
    TelephonyManager telephonyManager;
    int numberOfCheccked = 0;


    public CheckUsersInApp() {
        super("CheckUsersInApp");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: ");
        String ISOprefix = IcoToPhone.getPhone(telephonyManager.getSimCountryIso());
        Cursor cursor = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null
                , null, ContactsContract.CommonDataKinds.Phone.NUMBER);
        List<Users> contacts=new ArrayList<>();
        database.getReference().child(USERS).orderByChild("phone").equalTo("+17782920781").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: checkPhomne"+dataSnapshot.exists());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError.getMessage());
            }
        });
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phone = phone.replace(" ", "");
            phone = phone.replace("-", "");
            phone = phone.replace("(", "");
            phone = phone.replace(")", "");
            if (!String.valueOf(phone.charAt(0)).equals("+")) {
                if (phone.length() == 11)
                    phone = phone.replaceFirst("8", ISOprefix);
                else
                    phone = ISOprefix + phone;
            }
            Log.d(TAG, "onHandleIntent: "+name+" "+phone);
            contacts.add(new Users(null,name,phone,null));
        }
        List<Users> usersInDb=userDatabase.usersDao().getUsersWithNameAndPhones();
        for (Users user :
                usersInDb) {
            Log.d(TAG, "onHandleIntent: userInDb"+user.getName()+" "+user.getPhoneNumber());

        }
        Log.d(TAG, "onHandleIntent: deleted "+contacts.removeAll(usersInDb));
        usersInDb.clear();
        Log.d(TAG, "onHandleIntent: sizeOfContacts"+contacts.size());
        for (Users user :
                contacts) {
            Log.d(TAG, "onHandleIntent: "+user.toString());
            database.getReference().child(USERS).orderByChild("phone").equalTo(user.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshots) {
                    Log.d(TAG, "onDataChange: "+dataSnapshots.exists());
                    if(dataSnapshots.exists()) {
                        for (DataSnapshot dataSnapshot:
                         dataSnapshots.getChildren()    ) {
                            String uid = dataSnapshot.getKey();
                            Log.d(TAG, "onDataChange: "+uid);
                            String phoneNumber = dataSnapshot.child("phone").getValue().toString();
                            String urlForPhoto=null;
                            if(dataSnapshot.child("photoref").getValue()!=null)
                             urlForPhoto=dataSnapshot.child("photoref").getValue().toString();
                            String name = user.getName();
                            Users users = new Users(uid, name, phoneNumber, urlForPhoto);
                            usersInDb.add(users);
                            Log.d(TAG, "onDataChange: user in app " + users.toString());
                        }
                        Completable.create(new CompletableOnSubscribe() {
                            @Override
                            public void subscribe(CompletableEmitter emitter) throws Exception {
                                for (Users user :
                                        usersInDb) {
                                    Log.d(TAG, "subscribe: uderInd"+user);
                                }
                                userDatabase.usersDao().insertAll(usersInDb);
                            }
                        }).subscribeOn(Schedulers.io()).subscribe();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: "+databaseError.getMessage());
                }
            });

        }

    }

}
