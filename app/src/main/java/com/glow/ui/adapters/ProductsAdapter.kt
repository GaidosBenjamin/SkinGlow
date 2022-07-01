package com.glow.ui.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.glow.R
import com.glow.model.product.Products
import com.glow.ui.activities.MainActivity
import com.glow.ui.activities.ProductDetailsActivity
import com.glow.utils.Constants
import com.glow.utils.GlideLoader
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*

open class ProductsAdapter (private val context: Context,
                                      private var list: MutableList<Products>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_layout,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture(model.heroImage, holder.itemView.iv_dashboard_item_image)
            holder.itemView.tv_dashboard_item_title.text = model.currentSku.imageAltText
            holder.itemView.tv_dashboard_item_price.text = model.currentSku.listPrice

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.productId)
                intent.putExtra(Constants.EXTRA_SKU, model.currentSku.skuId)
                intent.putExtra(Constants.EXTRA_HERO_IMAGE, model.heroImage)
                context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}