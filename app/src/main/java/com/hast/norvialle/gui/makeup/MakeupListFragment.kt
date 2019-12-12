package com.hast.norvialle.gui.makeup


import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hast.norvialle.R
import com.hast.norvialle.data.MakeupArtist
import com.hast.norvialle.gui.BaseFragment
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.makeup.AddMakeupArtistActivity.Companion.MAKEUP_ARTIST
import com.hast.norvialle.utils.getSearchTextWatcher
import com.hast.norvialle.utils.showDeleteDialog
import kotlinx.android.synthetic.main.activity_search_list.view.*


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class MakeupListFragment : BaseFragment() {
    var onResultListener: ((makeupArtist: MakeupArtist) -> Unit)? = null
    var isSearchShown = false

    companion object {
        fun newInstance(onResultListener: ((makeupArtist: MakeupArtist) -> Unit)? = null): BaseFragment {
            val fragment = MakeupListFragment()
            fragment.onResultListener = onResultListener
            return fragment
        }
    }

    override fun getContentId(): Int {
        return R.layout.activity_search_list
    }

    override fun getMenuId(): Int {
        return R.menu.menu_search_plus
    }

    override fun getMenuTitleId(): Int {
        return R.string.makeupArtists
    }

    override fun onCreateView(view: View): View {
        context?.let {
            view.list.layoutManager = LinearLayoutManager(it)

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
        val adapter = MakeupAdapter(MainPresenter.makupArtists, context)
        val isForResult = onResultListener != null
        adapter.isForResult = isForResult
        root.list.adapter = adapter
        if (!isForResult) {
            adapter.onEditistener = {
                openMakeupArtistEditor(it)
            }
            adapter.onDeleteListener = {
                showDeleteDialog(context, R.string.delete_dialog_makeup_artist) {
                    MainPresenter.deleteMakeupArtist(it)
                    prepareAdapter(context)
                }
            }
        } else {
            adapter.onPickListener = { makeupArtist: MakeupArtist ->
                onResultListener?.invoke(makeupArtist)
                fragmentManager?.popBackStack()
            }
        }
        root.searchFilter.addTextChangedListener(getSearchTextWatcher { adapter.filterBy(it) })
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {

            R.id.plus -> {
                openMakeupArtistEditor(MakeupArtist("", 0, ""))
            }
            R.id.search -> {
                isSearchShown = !isSearchShown
                root.searchFilter.visibility = if (isSearchShown) View.VISIBLE else View.GONE
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun openMakeupArtistEditor(makeupArtist: MakeupArtist) {
        context?.let {
            var intent = Intent(context, AddMakeupArtistActivity::class.java)
            intent.putExtra(MAKEUP_ARTIST, makeupArtist)
            startActivityForResult(intent, AddMakeupArtistActivity.EDIT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        context?.let { prepareAdapter(it) }
        super.onActivityResult(requestCode, resultCode, data)
    }

}