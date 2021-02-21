package com.iconfinderdemo.di

import com.iconfinderdemo.network.IconApi
import com.iconfinderdemo.network.IconApiService
import com.iconfinderdemo.util.Constants
import com.iconfinderdemo.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import io.reactivex.android.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideIconApi():IconApi{
        return provideRetrofit().create(IconApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit():Retrofit{
        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(provideHttpLogging())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
    @Singleton
    @Provides
    fun provideIconService():IconApiService
    {
       return provideRetrofit().create(IconApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideHttpLogging(): OkHttpClient {
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

}