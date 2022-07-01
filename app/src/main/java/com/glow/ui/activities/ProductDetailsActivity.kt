package com.glow.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.glow.R
import com.glow.firestore.FirestoreClass
import com.glow.model.firestore.CartItem
import com.glow.model.product.ProductDetails
import com.glow.model.product.Products
import com.glow.model.product.ProductsResponse
import com.glow.utils.BaseActivity
import com.glow.utils.Constants
import com.glow.utils.GlideLoader
import com.glow.utils.proxy.SephoraProxy
import com.google.gson.GsonBuilder
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import kotlinx.android.synthetic.main.activity_product_details.*
import java.io.IOException

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var productId: String = ""
    private var sku: String = ""
    private var heroImage: String = ""
    private lateinit var mProductDetails: ProductDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        setUpActionBar()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            productId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        if(intent.hasExtra(Constants.EXTRA_HERO_IMAGE)) {
            heroImage = intent.getStringExtra(Constants.EXTRA_HERO_IMAGE)!!
        }
        if(intent.hasExtra(Constants.EXTRA_SKU)) {
            sku = intent.getStringExtra(Constants.EXTRA_SKU)!!
        }

        btn_add_to_cart.setOnClickListener(this)
        btn_go_to_cart.setOnClickListener(this)
        SephoraProxy().getProductDetails(productId, sku, this@ProductDetailsActivity)
    }

    fun productDetailsSuccess(product: ProductDetails) {
        runOnUiThread {
            if(heroImage.isNotEmpty()) {
                GlideLoader(this@ProductDetailsActivity).loadProductPicture(heroImage, iv_product_detail_image)
            }

            tv_product_details_title.text = product.imageAltText
            tv_product_details_price.text = product.currentSku.listPrice
            tv_product_details_description.text = product.quickLookDescription
            tv_product_details_stock_quantity.text = product.currentSku.maxPurchaseQuantity
            tv_product_details_rating.text = "${product.rating}"
            tv_product_details_size.text = product.currentSku.variationValue

            if(product.currentSku.maxPurchaseQuantity.toInt() <= 0) {
                btn_add_to_cart.visibility = View.GONE
                tv_product_details_stock_quantity.text = resources.getString(R.string.lbl_out_of_stock)
                tv_product_details_stock_quantity.setTextColor(ContextCompat.getColor(this@ProductDetailsActivity, R.color.colorSnackBarError))
            }
            FirestoreClass().checkIfItemExistInCart(this@ProductDetailsActivity, productId)
        }
        hideProgressDialog()
        mProductDetails = product

    }

    private fun setUpActionBar() {

        setSupportActionBar(toolbar_product_details_activity)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if(v != null) {
            when(v.id) {
                R.id.btn_add_to_cart -> {
                    addToCart()
                }
                R.id.btn_go_to_cart -> {
                    startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
                }
            }
        }
    }

    private fun addToCart() {
        val cartItem = CartItem(
            FirestoreClass().getCurrentUserId(),
            productId,
            mProductDetails.imageAltText,
            mProductDetails.currentSku.listPrice,
            heroImage,
            Constants.DEFAULT_CART_QUANTITY,
            mProductDetails.currentSku.maxPurchaseQuantity
        )

        showProgressDialog()
        FirestoreClass().addCartItems(this@ProductDetailsActivity, cartItem)
    }

    fun addToCartSuccess() {
        hideProgressDialog()

        Toast.makeText(this@ProductDetailsActivity, resources.getString(R.string.success_message_item_added_to_cart), Toast.LENGTH_SHORT).show()

        btn_add_to_cart.visibility = View.GONE
    }

    fun productExistsInCart() {
        btn_add_to_cart.visibility = View.GONE
    }

    fun productDoesntExistInCart() {
        btn_add_to_cart.visibility = View.VISIBLE
    }

    override fun onResume() {
        FirestoreClass().checkIfItemExistInCart(this@ProductDetailsActivity, productId)
        super.onResume()
    }


}