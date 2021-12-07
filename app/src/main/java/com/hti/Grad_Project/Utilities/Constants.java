package com.hti.Grad_Project.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hti.Grad_Project.R;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
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