package com.glow.ui.adapters.scroll

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glow.model.product.Products
import com.glow.ui.adapters.ProductsAdapter
import com.glow.utils.Constants
import com.glow.utils.proxy.SephoraProxy

class ProductsScrollListener(private val layoutManager: GridLayoutManager,
                             private val adapter: ProductsAdapter,
                             private val dataList: MutableList<Products>, private val category: String) : RecyclerView.OnScrollListener() {
    var previousTotal = 0
    var loading = true
    val visibleThreshold = 10
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var pageNumber: Int = 1

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            val initialSize = dataList.size
            pageNumber++
            Log.i("PAGE", pageNumber.toString())
            dataList.addAll(SephoraProxy().getProductsFromSephora(category, pageNumber))
            val updatedSize = dataList.size
            recyclerView.post { adapter.notifyItemRangeInserted(initialSize, updatedSize) }
            loading = true
        }
    }
}