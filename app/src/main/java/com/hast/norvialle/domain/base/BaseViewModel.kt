package com.hast.norvialle.domain.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hast.norvialle.di.component.DaggerViewModelInjector
import com.hast.norvialle.di.component.ViewModelInjector
import com.hast.norvialle.di.module.ApiModule
import com.hast.norvialle.domain.EventsViewModel
import io.reactivex.disposables.Disposable

/**
 * Created by Konstantyn Zakharchenko on 28.12.2019.
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .apiModule(ApiModule)
        .build()

    protected val subscriptions = arrayListOf<Disposable>()
    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is EventsViewModel -> injector.inject(this)
        }
    }
    override fun onCleared() {
        super.onCleared()
        for (subscription in subscriptions) {
            subscription.dispose()
        }

    }

    fun putValueOrNothing(value: Int): String {
        return if (value != 0) {
            "$value"
        } else {
            ""
        }
    }
}