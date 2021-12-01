package com.hti.Grad_Project.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hti.myapplication.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        ll_newAcc_Register.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        bt_Register.setOnClickListener {
            CheckDataInput(
                ed_email = EditText_Email_Register,
                ed_name = EditText_UserName_Register,
                ed_password = EditText_Pass_Register,
                ed_confirmPss = EditText_PassConfirm_Register
            )
        }

    }


    private fun CheckDataInput(
        ed_email: TextInputEditText,
        ed_name: TextInputEditText,
        ed_password: TextInputEditText,
        ed_confirmPss: TextInputEditText,
    ) {
        val email = ed_email.text.toString()
        val name = ed_name.text.toString()
        val password = ed_password.text.toString()
        val confirm_Pass = ed_confirmPss.text.toString()

        var Layout_email = TI_Email_Register;
        var Layout_name = TI_UserName_Register;
        var Layout_password = TI_Pass_Register;
        var Layout_confirmPss = TI_PassConfirm_Register;

        if (email.isEmpty() && name.isEmpty() && password.isEmpty() && confirm_Pass.isEmpty()) {
            Layout_email.isErrorEnabled()
            Layout_email.setError("You did not enter a email")
            WatchListenerEditTextDisableError(Layout_email, ed_email)

            Layout_name.isErrorEnabled()
            Layout_name.setError("You did not enter a name")
            WatchListenerEditTextDisableError(Layout_name, ed_name)

            Layout_password.isErrorEnabled()
            Layout_password.setError("You did not enter a password")
            WatchListenerEditTextDisableError(Layout_password, ed_password)

            Layout_confirmPss.isErrorEnabled()
            Layout_confirmPss.setError("You did not enter a confirm password")
            WatchListenerEditTextDisableError(Layout_confirmPss, ed_confirmPss)

            return
        } else if (email.isEmpty()) {
            Layout_email.isErrorEnabled()
            Layout_email.setError("You did not enter a email")
            WatchListenerEditTextDisableError(Layout_email, ed_email)
            return
        } else if (name.isEmpty()) {
            Layout_name.isErrorEnabled()
            Layout_name.setError("You did not enter a name")
            WatchListenerEditTextDisableError(Layout_name, ed_name)
            return
        } else if (password.isEmpty()) {
            Layout_password.isErrorEnabled()
            Layout_password.setError("You did not enter a password")
            WatchListenerEditTextDisableError(Layout_password, ed_password)
            return
        } else if (confirm_Pass.isEmpty()) {
            Layout_confirmPss.isErrorEnabled()
            Layout_confirmPss.setError("You did not enter a confirm password")
            WatchListenerEditTextDisableError(Layout_confirmPss, ed_confirmPss)
            return
        }

        if (password != confirm_Pass) {
            Layout_password.isErrorEnabled()
            Layout_confirmPss.isErrorEnabled()
            Layout_confirmPss.setError("Passwords do not match")
            Layout_password.setError("Passwords do not match")
            WatchListenerEditTextDisableError_4Para(
                Layout_password,
                ed_password,
                Layout_confirmPss,
                ed_confirmPss
            )
            return
        }

        //True Code
        //        val userModel = UserModel(name, email, password, phone)
        //        CreateNewUser(userModel)
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

    fun WatchListenerEditTextDisableError_4Para(
        input: TextInputLayout,
        inputEditText: TextInputEditText,
        input2: TextInputLayout,
        inputEditText2: TextInputEditText
    ) {
        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                input.isErrorEnabled = false
                input2.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable) {
                input.isErrorEnabled = false
                input2.isErrorEnabled = false
            }
        })
        inputEditText2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                input.isErrorEnabled = false
                input2.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable) {
                input.isErrorEnabled = false
                input2.isErrorEnabled = false
            }
        })
    }

}