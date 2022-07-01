package com.glow.ui.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import com.glow.R
import com.glow.model.product.Products
import com.glow.ui.adapters.ProductsAdapter
import com.glow.ui.adapters.scroll.ProductsScrollListener
import com.glow.utils.BaseActivity
import com.glow.utils.Constants
import com.glow.utils.proxy.SephoraProxy
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showResponse(Constants.CATEGORY_SKINCARE)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                return true
            }
            R.id.action_cart -> {
                startActivity(Intent(this@MainActivity, CartListActivity::class.java))
                return true
            }
            R.id.action_filter -> {
                val popup = PopupMenu(this@MainActivity, findViewById(R.id.action_filter))
                popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)

                popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                    when (menuItem.itemId) {
                        R.id.skincare -> {
                            showResponse(Constants.CATEGORY_SKINCARE)

                            true
                        }
                        R.id.makeup -> {
                            showResponse(Constants.CATEGORY_MAKEUP)
                            true
                        }
                        R.id.clean_at_sephora -> {
                            showResponse(Constants.CATEGORY_CLEAN_AT_SEPHORA)
                            true
                        }
                        R.id.hair -> {
                            showResponse(Constants.CATEGORY_HAIR)
                            true
                        }
                        R.id.tools_and_brushes -> {
                            showResponse(Constants.CATEGORY_TOOLS_AND_BRUSHES)
                            true
                        }
                        R.id.fragrance -> {
                            showResponse(Constants.CATEGORY_FRAGRANCE)
                            true
                        }
                        R.id.bath_and_body -> {
                            showResponse(Constants.CATEGORY_BATH_AND_BODY)
                            true
                        }
                        R.id.gifts -> {
                            showResponse(Constants.CATEGORY_GIFTS)
                            true
                        }
                        R.id.men -> {
                            showResponse(Constants.CATEGORY_MEN)
                            true
                        }
                        R.id.mini_size -> {
                            showResponse(Constants.CATEGORY_MINI_SIZE)
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
                popup.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showResponse(category: String) {

        val products: MutableList<Products> = SephoraProxy().getProductsFromSephora(category, 0)

        if(products.size > 0) {
            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_items_found.visibility = View.GONE

            val layoutManager = GridLayoutManager(this@MainActivity, 2)
            val adapter = ProductsAdapter(this@MainActivity, products)
            rv_dashboard_items.setHasFixedSize(false)
            rv_dashboard_items.layoutManager = layoutManager
            rv_dashboard_items.adapter = adapter
            rv_dashboard_items.addOnScrollListener(ProductsScrollListener(layoutManager, adapter, products, category))

        } else {
            rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_items_found.visibility = View.VISIBLE
        }


    }

}