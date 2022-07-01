package com.glow.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.glow.R
import com.glow.firestore.FirestoreClass
import com.glow.model.firestore.User
import com.glow.utils.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var userDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setUpActionBar()
        showProgressDialog()
        getUserDetails()

        btn_logout.setOnClickListener(this)
        tv_edit.setOnClickListener(this)
    }

    private fun setUpActionBar() {

        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails() {
        FirestoreClass().getUserDetails(this@SettingsActivity)
    }

    fun userDetailsSuccess(user: User) {
        userDetails = user

        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, findViewById(R.id.iv_user_photo))
        val tvName: SKTextViewBold = findViewById(R.id.tv_name)
        val tvGender: SKTextView = findViewById(R.id.tv_gender)
        val tvEmail: SKTextView = findViewById(R.id.tv_email)
        val tvMobileNumber: SKTextView = findViewById(R.id.tv_mobile_number)
        tvName.text = "${user.firstName } ${user.lastName}"
        tvGender.text = user.gender
        tvEmail.text = user.email
        tvMobileNumber.text = user.mobile

        hideProgressDialog()
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {
        if(v != null) {
            when(v.id) {
                R.id.btn_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.tv_edit -> {
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, userDetails)
                    startActivity(intent)
                }
            }
        }
    }
}