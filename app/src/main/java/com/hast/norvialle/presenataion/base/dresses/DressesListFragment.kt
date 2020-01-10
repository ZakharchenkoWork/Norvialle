package com.hast.norvialle.presenataion.base.dresses


import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.hast.norvialle.R
import com.hast.norvialle.data.Dress
import com.hast.norvialle.presenataion.base.BaseFragment
import com.hast.norvialle.domain.MainPresenter
import com.hast.norvialle.presenataion.utils.FullScreenPictureActivity
import kotlinx.android.synthetic.main.activity_search_list.*
import kotlinx.android.synthetic.main.activity_search_list.view.*


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class DressesListFragment : BaseFragment() {
    var pickedDresses : ArrayList<Dress> = ArrayList()
    var onResultListener: ((pickedDresses : ArrayList<Dress>) -> Unit)? = null
    lateinit var adapter : DressesAdapter

    companion object {
        fun newInstance(pickedDresses : ArrayList<Dress> = ArrayList(), onResultListener: ((pickedDresses : ArrayList<Dress>) -> Unit)? = null): BaseFragment {
            val fragment = DressesListFragment()
            fragment.pickedDresses = pickedDresses
            fragment.onResultListener = onResultListener

            return fragment
        }
    }
    override fun getContentId(): Int {
       return R.layout.activity_search_list
    }

    override fun getMenuId(): Int {
        if (onResultListener == null) {
            return R.menu.menu_plus
        } else{
            return R.menu.menu_pick
        }
    }

    override fun getMenuTitleId(): Int {
     return R.string.dresses
    }

    override fun onCreateView(view: View): View {

        context?.let {
            view.list.layoutManager = GridLayoutManager(it, 3)

        }
        return view
    }
    override fun onResume() {
        super.onResume()
        context?.let {
            prepareAdapter(it)
        }
    }

    fun prepareAdapter(context: Context) {
        adapter = DressesAdapter(MainPresenter.dresses, context)
        val isForResult = onResultListener != null
        adapter.isForResult = isForResult
        list.adapter = adapter
        if (isForResult){
            adapter.setPicked(pickedDresses)
        }
        if (!isForResult) {
            adapter.onViewDressListener = DressesAdapter.OnViewDressListener {
                openPictureFullScreen(it)
            }
        }
    }


    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.plus -> {
                openAddDressActivity(Dress("","",0))
            }
            R.id.pickDresses ->{
                onResultListener?.invoke(adapter.pickedDresses)
                fragmentManager?.popBackStack()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun openAddDressActivity(dress: Dress) {
        context?.let {
        var intent = Intent(it, AddDressActivity::class.java)
        intent.putExtra(AddDressActivity.DRESS, dress)
        startActivityForResult(intent, AddDressActivity.EDIT)
        }
    }
    fun openPictureFullScreen(dress : Dress) {
        context?.let {
            val intent = Intent(it, FullScreenPictureActivity::class.java)
            intent.putExtra(FullScreenPictureActivity.DRESS, dress)
            intent.putExtra(FullScreenPictureActivity.EDIT_AND_DELETE, true)
            startActivity(intent)
        }
    }


}