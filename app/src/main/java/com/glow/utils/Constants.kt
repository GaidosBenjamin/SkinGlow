package com.glow.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val CART_QUANTITY = "cart_quantity"
    const val PRODUCT_ID = "product_id"
    const val CART_ITEMS = "cart_items"
    const val DEFAULT_CART_QUANTITY = "1"
    const val USERS = "users"
    const val SKINGLOW_PREFERENCES: String = "SkinGlowPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 3
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val EXTRA_HERO_IMAGE = "extra_hero_image"
    const val EXTRA_PRODUCT_ID = "extra_product_id"
    const val EXTRA_SKU = "extra_sku"

    const val USER_ID = "user_id"
    const val MALE = "male"
    const val FEMALE = "female"
    const val MOBILE = "mobile"
    const val GENDER = "gender"
    const val IMAGE = "image"
    const val PROFILE_COMPLETE = "profileCompleted"
    const val FIRST_NAME = "firstName"
    const val LAST_NAME = "lastName"
    const val USER_PROFILE_IMAGE = "user_profile_image"

    const val CATEGORY_SKINCARE = "cat150006"
    const val CATEGORY_MAKEUP = "cat140006"
    const val CATEGORY_CLEAN_AT_SEPHORA = "cat3780034"
    const val CATEGORY_HAIR = "cat130038"
    const val CATEGORY_TOOLS_AND_BRUSHES = "cat130042"
    const val CATEGORY_FRAGRANCE = "cat160006"
    const val CATEGORY_BATH_AND_BODY = "cat140014"
    const val CATEGORY_GIFTS = "cat60270"
    const val CATEGORY_MEN = "cat130044"
    const val CATEGORY_MINI_SIZE = "cat1830032"

    fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}