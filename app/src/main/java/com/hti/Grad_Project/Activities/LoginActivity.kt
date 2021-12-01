package com.hti.Grad_Project.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hti.myapplication.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ll_newAcc_Login.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        bt_login.setOnClickListener {
            CheckDataInput(ed_email = EditText_Email_SignIn, ed_password = EditText_Pass_CreateAcc)
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
            WatchListenerEditTextDisableError(TI_Pass_Login, EditText_Pass_CreateAcc)
            return
        } else if (email.isEmpty()) {
            TI_Email_Login.isErrorEnabled
            TI_Email_Login.error = "You did not enter a email"
            WatchListenerEditTextDisableError(TI_Email_Login, EditText_Email_SignIn)
            return
        } else if (password.isEmpty()) {
            TI_Pass_Login.isErrorEnabled
            TI_Pass_Login.error = "You did not enter a password"
            WatchListenerEditTextDisableError(TI_Pass_Login, EditText_Pass_CreateAcc)
            return
        }
        // The password it right
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


}