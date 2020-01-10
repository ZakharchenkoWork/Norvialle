package com.hast.norvialle.di.module

import com.hast.norvialle.model.Api
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Konstantyn Zakharchenko on 28.12.2019.
 */
@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
object ApiModule {

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(): Retrofit {


        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)


        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            //TODO: Consider replacing with MoshiConverterFactory
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://urbantactics.info:8080/")
            .client(httpClient.build())
            .build()
    }
}