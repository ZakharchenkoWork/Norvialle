package com.hast.norvialle.gui.studio


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hast.norvialle.R
import com.hast.norvialle.data.PhotoRoom
import com.hast.norvialle.data.Studio
import com.hast.norvialle.gui.BaseFragment
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.studio.AddStudioActivity.Companion.STUDIO
import com.hast.norvialle.utils.getSearchTextWatcher
import com.hast.norvialle.utils.showDeleteDialog
import kotlinx.android.synthetic.main.activity_search_list.view.*


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class StudiosListFragment : BaseFragment() {
    var onResultListener: ((contact: Studio, photoRoom: PhotoRoom) -> Unit)? = null
    var isSearchShown = false

    companion object {
        fun newInstance(onResultListener: ((studio: Studio, photoRoom: PhotoRoom) -> Unit)? = null): BaseFragment {
            val fragment = StudiosListFragment()
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
        return R.string.studios
    }

    override fun onCreateView(view: View): View {
        context?.let {
            view.list.layoutManager = LinearLayoutManager(it)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        context?.let { prepareAdapter(it) }

    }

    fun prepareAdapter(context: Context) {
        val adapter = StudiosAdapter(MainPresenter.studios, context)
        val isForResult = onResultListener != null
        root.list.adapter = adapter
        adapter.isForResult = isForResult
        if (!isForResult) {
            adapter.onEditistener = { openStudioEditor(it) }
            adapter.onDeleteListener = {
                showDeleteDialog(context, R.string.delete_dialog_studio) {
                    MainPresenter.deleteStudio(it)
                    prepareAdapter(context)
                }
            }
        } else {
            adapter.onPickListenerWithOptional = { studio: Studio, photoRoom: Any ->

                if (photoRoom is PhotoRoom) {
                    onResultListener?.invoke(studio, photoRoom)
                    fragmentManager?.popBackStack()
                }

            }
        }
        root.searchFilter.addTextChangedListener(getSearchTextWatcher {
            (root.list.adapter as StudiosAdapter).filterBy(it)
        })
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {

            R.id.plus -> {
                openStudioEditor(Studio())
            }
            R.id.search -> {
                isSearchShown = !isSearchShown
                root.searchFilter.visibility = if (isSearchShown) View.VISIBLE else View.GONE
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun openStudioEditor(studio: Studio) {
        context?.let {
            var intent = Intent(it, AddStudioActivity::class.java)
            intent.putExtra(STUDIO, studio)
            startActivityForResult(intent, AddStudioActivity.EDIT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        context?.let {
            when (requestCode) {
                AddStudioActivity.EDIT -> {
                    if (data != null && data.extras != null && data?.extras?.getSerializable(STUDIO) != null) {
                        val studio = data.extras?.getSerializable(STUDIO) as Studio
                        MainPresenter.addStudio(studio)
                        prepareAdapter(it)
                        Log.d("result", "is " + resultCode + " name: " + studio.name)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}