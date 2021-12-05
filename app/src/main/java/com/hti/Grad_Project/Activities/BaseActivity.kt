package com.hti.Grad_Project.Activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout.make
import android.widget.Toast

import carbon.widget.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

open class BaseActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    var mDatabaseReference: DatabaseReference? = null
    var mDatabaseFireStore:FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkInternetConnection(this)) {

            if (mAuth == null) {
                mAuth = FirebaseAuth.getInstance()
            }
            if (mDatabaseReference == null) {
                mDatabaseReference = FirebaseDatabase.getInstance().reference
            }
            if (mDatabaseFireStore == null) {
                mDatabaseFireStore = Firebase.firestore
            }

        } else {

            Toast.makeText(this, "No Internet connection !!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun checkInternetConnection(context: Context): Boolean {
        val connectivity = context
            .getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        for (i in info.indices) {
            if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
        return false
    }

}