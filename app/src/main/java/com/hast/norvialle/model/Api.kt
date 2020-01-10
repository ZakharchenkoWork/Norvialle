package com.hast.norvialle.model

import com.hast.norvialle.data.AuthData
import com.hast.norvialle.data.server.DataUpdatesResponce
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


/**
 * Created by Konstantyn Zakharchenko on 20.12.2019.
 */
interface Api {

    @POST("/rest/v1/register")
    fun register(@Body authData: AuthData) : Observable<DataUpdatesResponce>
    @POST("/rest/v1/login")
    fun login(@Body authData: AuthData) : Observable<DataUpdatesResponce>




    companion object Factory {
        fun create(): Api {

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)


            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://urbantactics.info:8080/")
                .client(httpClient.build())
                .build()

            return retrofit.create(Api::class.java)
        }
    }
}