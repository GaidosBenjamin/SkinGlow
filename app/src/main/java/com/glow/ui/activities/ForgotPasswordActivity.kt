package com.glow.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.glow.R
import com.glow.utils.BaseActivity
import com.glow.utils.SKButton
import com.glow.utils.SKEditText
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setupActionBar(findViewById(R.id.toolbar_forgot_password_activity))

        val submit: SKButton = findViewById(R.id.btn_submit)
        submit.setOnClickListener {
            val email: String = editTextToString(findViewById(R.id.et_email_forgot_password))
            if(email.isEmpty()) {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
            } else {
                showProgressDialog()
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        hideProgressDialog()
                        if(task.isSuccessful) {
                            Toast.makeText(this@ForgotPasswordActivity, resources.getString(R.string.msg_email_sent_success), Toast.LENGTH_LONG).show()

                            finish()
                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }
        }
    }

    private fun editTextToString(input: SKEditText): String {
        return input.text.toString().trim { it <= ' ' }
    }
}