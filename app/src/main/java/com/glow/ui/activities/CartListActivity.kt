package com.glow.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.glow.R
import com.glow.firestore.FirestoreClass
import com.glow.model.firestore.CartItem
import com.glow.model.product.Products
import com.glow.ui.adapters.CartListAdapter
import com.glow.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartListActivity : BaseActivity() {

    private lateinit var mCartItems: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        setUpActionBar()
    }

    private fun setUpActionBar() {

        setSupportActionBar(toolbar_cart_list_activity)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_cart_list_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()

        mCartItems = cartList

        if(mCartItems.size > 0) {
            rv_cart_items_list.visibility = View.VISIBLE
            ll_checkout.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_items_list.layoutManager = LinearLayoutManager(this@CartListActivity)
            rv_cart_items_list.setHasFixedSize(true)
            val cartListAdapter = CartListAdapter(this@CartListActivity, cartList)
            rv_cart_items_list.adapter = cartListAdapter

            var subTotal: Double = 0.0
            for(item in mCartItems) {
                val availableQuantity = item.max_order_quantity.toInt()
                if(availableQuantity > 0) {
                    val price = item.price.replace("$", "").toDouble()
                    val quantity = item.cart_quantity.toInt()
                    subTotal += (price * quantity)
                }
            }
            tv_sub_total.text = "$${subTotal}"
            tv_shipping_charge.text = "$10.0"

            if(subTotal > 0) {
                ll_checkout.visibility = View.VISIBLE

                val total = subTotal + 10
                tv_total_amount.text = "$${total}"
            } else {
                ll_checkout.visibility = View.GONE
            }
        }
        else {
            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }
    }

    private fun getCartItemsList() {
        //showProgressDialog()
        FirestoreClass().getCartList(this@CartListActivity)
    }

    override fun onResume() {
        super.onResume()
        getProductList()
    }

//    fun successProductsListFromFireStore(products: ArrayList<Product>) {
//        hideProgressDialog()
//        mProducts = products
//
//        getCartItemsList()
//    }

    private fun getProductList() {
        showProgressDialog()
        //FirestoreClass().getAllProductsList(this@CartListActivity)
        getCartItemsList()
        hideProgressDialog()
    }

    fun itemRemovedSuccess() {
        hideProgressDialog()
        Toast.makeText(this@CartListActivity, resources.getString(R.string.msg_item_removed_successfully), Toast.LENGTH_SHORT).show()

        getCartItemsList()
    }

    fun itemUpdateSuccess() {
        hideProgressDialog()

        getCartItemsList()
    }
}