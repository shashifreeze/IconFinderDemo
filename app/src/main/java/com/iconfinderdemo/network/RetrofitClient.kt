package com.iconfinderdemo.network

import com.iconfinderdemo.util.Constants
import com.iconfinderdemo.util.Constants.BASE_URL
import io.reactivex.android.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    private var retrofit: Retrofit
    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpLogging())
            .build()
    }

    private fun httpLogging(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer ${Constants.API_KEY}"
                        ).build()
                )
            }
            .addInterceptor(logging)
            .build()
    }

    companion object {
        private lateinit var retrofitClient: RetrofitClient

        @Synchronized
        fun getInstance(): RetrofitClient {
            retrofitClient = RetrofitClient()

            return retrofitClient
        }
    }

    public fun getIconApi():IconApi{
        return  retrofit.create(IconApi::class.java)
    }
}