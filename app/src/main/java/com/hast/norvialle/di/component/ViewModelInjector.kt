package com.hast.norvialle.di.component

import com.hast.norvialle.di.module.ApiModule
import com.hast.norvialle.domain.EventsViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Konstantyn Zakharchenko on 28.12.2019.
 */
@Singleton
@Component(modules = [ApiModule::class])
interface ViewModelInjector {


    fun inject(eventsViewModel: EventsViewModel)


    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun apiModule(apiModule: ApiModule): Builder
    }
}