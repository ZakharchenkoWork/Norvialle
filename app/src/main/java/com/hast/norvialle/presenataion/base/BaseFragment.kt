package com.hast.norvialle.presenataion.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hast.norvialle.data.Dress
import com.hast.norvialle.presenataion.base.dresses.DressesListFragment

/**
 * Created by Konstantyn Zakharchenko on 11.12.2019.
 */
abstract class BaseFragment : Fragment(){
    lateinit var root : View
    abstract fun getContentId():Int
    abstract fun getMenuId():Int
    abstract fun getMenuTitleId():Int
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);

            container?.removeAllViews()

        root = onCreateView(inflater.inflate(getContentId(), container, false))

        return root
    }
    abstract fun onCreateView(view : View) :  View

     fun openEventsList(dateToScroll: Long = 0) {
        activity?.let { if (it is BaseActivity){
            it.openEventsList(dateToScroll)
        } }

    }
    fun openDressList(dressList: ArrayList<Dress>, onResultListener: ((pickedDresses : ArrayList<Dress>) -> Unit)) {
        activity?.let {
            if (it is BaseActivity){
                it.showFragment(DressesListFragment.newInstance(dressList, onResultListener))
            }
        }
    }
    override fun onResume() {
        super.onResume()
        activity?.let {
            it.invalidateOptionsMenu()
            if (it is BaseActivity){
                it.setToolBarTitle(getMenuTitleId())
            }}

    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        activity?.let {
            menu.clear()
            it.menuInflater.inflate(getMenuId(), menu)
            activity?.let {activity-> if (activity is BaseActivity){
                activity.setToolBarTitle(getMenuTitleId())
            } }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.invalidateOptionsMenu()
    }

}