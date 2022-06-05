package com.alexanderdudnik.stackexchangedemo

import android.app.Application
import com.alexanderdudnik.stackexchangedemo.network.ApiClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Application class
 */
class App : Application() {

    lateinit var retrofit: ApiClient
        private set

    override fun onCreate() {
        super.onCreate()
        self = this

        retrofit = Retrofit.Builder()
            .baseUrl("https://api.stackexchange.com/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiClient::class.java)

    }


    companion object {
        private var self: App? = null

        fun getSelf() : App = self ?: throw IllegalAccessException()
    }
}