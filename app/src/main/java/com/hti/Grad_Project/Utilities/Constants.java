package com.hti.Grad_Project.Utilities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Objects;


public class Constants {

    private static FirebaseFirestore db;
    private static FirebaseDatabase dbReal;
    private static DatabaseReference databaseReference;
    private static FirebaseAuth mAut;

    //Text
    private static final String userId = Objects.requireNonNull(Objects.requireNonNull(Constants.GetAuth()).getCurrentUser()).getUid();

    public static FirebaseAuth GetAuth() {
        if (mAut == null)
            mAut = FirebaseAuth.getInstance();

        return mAut;
    }

    public static FirebaseFirestore GetFireStoneDb() {

        if (db == null)
            db = FirebaseFirestore.getInstance();

        return db;
    }

    public static DatabaseReference GetRef() {

        if (databaseReference == null || dbReal == null)
            dbReal = FirebaseDatabase.getInstance();
        databaseReference = dbReal.getReference();


        return databaseReference;
    }


    public static void makeIntent(Context context1, Class<?> context2) {
        context1.startActivity(new Intent(context1, context2));
    }

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



}