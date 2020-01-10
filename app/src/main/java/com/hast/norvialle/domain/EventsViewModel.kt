package com.hast.norvialle.domain

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hast.norvialle.App
import com.hast.norvialle.domain.base.BaseViewModel
import com.hast.norvialle.data.Event
import com.hast.norvialle.model.Api
import com.hast.norvialle.utils.getDateInMillis
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by Konstantyn Zakharchenko on 28.12.2019.
 */
class EventsViewModel(application: Application) : BaseViewModel(application) {
    @Inject
    lateinit var api: Api

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val events: MutableLiveData<List<Event>> = MutableLiveData()


    private fun loadEvents() {
        /*subscription = api.login(AuthData("",""))
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveEventeStart() }
            .doOnTerminate { onRetrieveEventsFinish() }
            .subscribe(
                { onRetrieveEventsSuccess() },
                { onRetrieveEventaError() }
            )*/

        subscriptions.add(App.db.eventDao().getNextList(getDateInMillis(System.currentTimeMillis()))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                this.events.value = it
            })
    }


private fun onRetrieveEventsFinish() {
    loadingVisibility.value = View.GONE
}

private fun onRetrieveEventsSuccess() {

}

private fun onRetrieveEventaError() {

}

private fun onRetrieveEventeStart() {
    loadingVisibility.value = View.VISIBLE
}
}