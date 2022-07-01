package com.glow.utils.proxy

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.glow.model.product.ProductDetails
import com.glow.model.product.Products
import com.glow.model.product.ProductsResponse
import com.glow.ui.activities.MainActivity
import com.glow.ui.activities.ProductDetailsActivity
import com.google.gson.GsonBuilder
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.IOException
import java.util.concurrent.CountDownLatch

class SephoraProxy {

    fun getProductsFromSephora(category: String, pageNumber: Int): MutableList<Products> {
        val list: MutableList<Products> = ArrayList()

        val client = OkHttpClient()
        val countDownLatch = CountDownLatch(1)
        val request = Request.Builder()
            .url("https://sephora.p.rapidapi.com/products/list?categoryId=${category}&pageSize=60&currentPage=${pageNumber}")
            .get()
            .addHeader("X-RapidAPI-Key", "17a04b070dmshad128e93fadd906p1575d6jsn6fd15f08b36d")
            .addHeader("X-RapidAPI-Host", "sephora.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                Log.e("CALL", e.toString())
                countDownLatch.countDown();
            }

            override fun onResponse(response: Response?) {
                val body = response?.body()?.string()
                if (body != null) {
                    val gson = GsonBuilder().create()
                    val response = gson.fromJson(body, ProductsResponse::class.java)
                    list.addAll(response.products)
                    countDownLatch.countDown();
                }
            }
        })
        countDownLatch.await()
        return list;
    }

    fun getProductDetails(productId: String, sku: String, activity: ProductDetailsActivity) {
        activity.showProgressDialog()
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://sephora.p.rapidapi.com/products/detail?productId=${productId}&preferedSku=${sku}")
            .get()
            .addHeader("X-RapidAPI-Key", "17a04b070dmshad128e93fadd906p1575d6jsn6fd15f08b36d")
            .addHeader("X-RapidAPI-Host", "sephora.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                Log.e("CALL FAILED", e.toString())
            }

            override fun onResponse(response: Response?) {
                val body = response?.body()?.string()
                if (body != null) {
                    Log.i("RESPONSE", body)
                    val gson = GsonBuilder().create()
                    val response = gson.fromJson(body, ProductDetails::class.java)
                    activity.productDetailsSuccess(response)
                }
            }
        })
    }

}