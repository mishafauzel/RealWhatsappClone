package com.example.realwhatsappclone.userphoto;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.realwhatsappclone.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import static com.example.realwhatsappclone.utills.NodesNames.*;

public class UserPhotoViewModel extends ViewModel {
    private static final String TAG = "UserPhotoViewModel";
    enum clickedState{SelectPhoto,MoveToNext}
    enum FragState{finished,inProgress,Error}
    StorageReference reference;

    FirebaseAuth auth;
    FirebaseDatabase database;
    private MutableLiveData<clickedState> isPhotoRequired=new MutableLiveData<>();
    private MutableLiveData<FragState> fragState=new MutableLiveData<>();

    public MutableLiveData<FragState> getFragState() {
        return fragState;
    }

    @Inject
    public UserPhotoViewModel(StorageReference reference, FirebaseAuth auth,FirebaseDatabase database) {
       this.reference=reference;
       this.auth=auth;
       this.database=database;
    }


    public LiveData<clickedState> getIsPhotoRequired()
    {
        return isPhotoRequired;
    }
    public void takePhoto(View view)
    {
        Log.d(TAG, "takePhoto: ");
        switch (view.getId())
        {
            case R.id.take_photo:
            {
                isPhotoRequired.setValue(clickedState.SelectPhoto);
                break;
            }
            case R.id.skip:
            {
                isPhotoRequired.setValue(clickedState.MoveToNext);
                break;
            }
        }


    }
    public void savePhoto(Bitmap bitmap)

    {

        fragState.setValue(FragState.inProgress);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        reference.child(AVATARS+"/"+auth.getUid()+"/avatar.jpg").putBytes(data).continueWithTask(task -> {
            Log.d(TAG, "then: "+task.isSuccessful());
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return reference.child(AVATARS+"/"+auth.getUid()+"/avatar.jpg").getDownloadUrl();
        }).addOnSuccessListener(uri -> {
            Log.d(TAG, "onSuccess: uri is"+uri);
            Map<String,Object> map=new HashMap<>();
            map.put("photoref",uri.toString());
            database.getReference().child(USERS).child(auth.getUid()).updateChildren(map).addOnSuccessListener(aVoid -> fragState.setValue(FragState.finished));
        });
    }

}
