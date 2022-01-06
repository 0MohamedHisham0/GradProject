package com.hti.Grad_Project.Utilities;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthAppRepository {
    static MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<FirebaseUser>();

    public static void saveUserData(FirebaseUser firebaseUser) {
        userLiveData.postValue(firebaseUser);
    }

    public static MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

}