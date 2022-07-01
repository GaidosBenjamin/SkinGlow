package com.glow.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.glow.R
import com.glow.firestore.FirestoreClass
import com.glow.model.firestore.User
import com.glow.utils.BaseActivity
import com.glow.utils.Constants
import com.glow.utils.SKEditText
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tv_register.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        btn_login.setOnClickListener {
            logInUser()
        }

        tv_forgot_password.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }

    }

    private fun validateInput(input: String): Boolean {
        return TextUtils.isEmpty(input.trim { it <= ' ' })
    }

    private fun validateLoginDetails(): Boolean {
        val email: SKEditText = findViewById(R.id.et_email)
        val password: SKEditText = findViewById(R.id.et_password)

        return when {
            validateInput(email.text.toString()) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            validateInput(password.text.toString()) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun editTextToString(input: SKEditText): String {
        return input.text.toString().trim { it <= ' ' }
    }

    private fun logInUser() {

        if(validateLoginDetails()) {
            showProgressDialog()

            val email: String = editTextToString(findViewById(R.id.et_email))
            val password: String = editTextToString(findViewById(R.id.et_password))

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        FirestoreClass().getUserDetails(this@LoginActivity)
                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(resources.getString(R.string.err_msg_login), true)
                    }
                }
        }
    }

    fun userLoggedInSuccess(user: User) {
        hideProgressDialog()

        if(user.profileCompleted == 0) {
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }

        finish()
    }
}