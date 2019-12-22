package com.hast.norvialle.model

import com.hast.norvialle.data.AuthData
import com.hast.norvialle.data.DataUpdates
import com.hast.norvialle.data.server.DataUpdatesResponce
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Konstantyn Zakharchenko on 21.12.2019.
 */
class ApiImpl : Api {
    private val norvialleApi: Api = Api.create()
    @Deprecated("Do not use directly", ReplaceWith("norvialleApi.registration()"))
    override fun register(authData: AuthData): Observable<DataUpdatesResponce> {
        return norvialleApi.register(authData)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    fun registration(authData: AuthData): Observable<DataUpdates> {
        return register(authData).flatMap { responce: DataUpdatesResponce ->
            return@flatMap ObservableSource<DataUpdates>{
                responce.updates
            }
        }
    }

}