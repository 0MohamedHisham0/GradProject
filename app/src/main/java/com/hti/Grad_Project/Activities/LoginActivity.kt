package com.hti.Grad_Project.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hti.Grad_Project.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (mAuth?.currentUser?.uid != null){
            startActivity(Intent(this, TextRecognitionActivity::class.java))
            finishAffinity()
        }

        ll_newAcc_Login.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        bt_login.setOnClickListener {
            CheckDataInput(ed_email = EditText_Email_SignIn, ed_password = EditText_Pass_Login)
        }
        bt_ocr_Login.setOnClickListener {
            startActivity(Intent(this, TextRecognitionActivity::class.java))
        }

    }

    private fun CheckDataInput(ed_email: TextInputEditText, ed_password: TextInputEditText) {
        val email = ed_email.text.toString()
        val password = ed_password.text.toString()
        if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Please Enter Your Data !", Toast.LENGTH_SHORT).show()
            TI_Pass_Login.isErrorEnabled()
            TI_Pass_Login.setError("You did not enter a password")
            TI_Email_Login.isErrorEnabled
            TI_Email_Login.setError("You did not enter a email")
            WatchListenerEditTextDisableError(TI_Email_Login, EditText_Email_SignIn)
            WatchListenerEditTextDisableError(TI_Pass_Login, EditText_Pass_Login)
            return
        } else if (email.isEmpty()) {
            TI_Email_Login.isErrorEnabled
            TI_Email_Login.error = "You did not enter a email"
            WatchListenerEditTextDisableError(TI_Email_Login, EditText_Email_SignIn)
            return
        } else if (password.isEmpty()) {
            TI_Pass_Login.isErrorEnabled
            TI_Pass_Login.error = "You did not enter a password"
            WatchListenerEditTextDisableError(TI_Pass_Login, EditText_Pass_Login)
            return
        }
        //Successfully validation
        loginUser()
    }

    fun WatchListenerEditTextDisableError(
        input: TextInputLayout,
        inputEditText: TextInputEditText
    ) {
        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                input.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable) {
                input.isErrorEnabled = false
            }
        })
    }

    private fun loginUser() {
        var email = EditText_Email_SignIn.text.toString()
        var password = EditText_Pass_Login.text.toString()
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                        spin_kit_QS.visibility = View.GONE
                    // Sign in success, update UI with signed-in user's information
                    Toast.makeText(
                        this, "Login successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(this)
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }

    }

    private fun updateUI(context: Context) {
        mAuth?.currentUser?.let {
            mDatabaseReference?.child("Users")?.child(it.uid)
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val intent = Intent(context, TextRecognitionActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            applicationContext, "Failed to receive data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                })
        }
    }
}