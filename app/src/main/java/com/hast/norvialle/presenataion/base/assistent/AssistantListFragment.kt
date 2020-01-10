package com.hast.norvialle.presenataion.base.makeup


import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hast.norvialle.R
import com.hast.norvialle.data.Assistant
import com.hast.norvialle.presenataion.base.BaseFragment
import com.hast.norvialle.domain.MainPresenter
import com.hast.norvialle.presenataion.base.makeup.AddAssistantActivity.Companion.MAKEUP_ARTIST
import com.hast.norvialle.utils.getSearchTextWatcher
import com.hast.norvialle.presenataion.utils.dialogs.showDeleteDialog
import kotlinx.android.synthetic.main.activity_search_list.view.*


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class AssistantListFragment : BaseFragment() {
    var onResultListener: ((assistant: Assistant) -> Unit)? = null
    var isSearchShown = false

    companion object {
        fun newInstance(onResultListener: ((assistant: Assistant) -> Unit)? = null): BaseFragment {
            val fragment = AssistantListFragment()
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
        return R.string.assistants
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
        val adapter = AssistantAdapter(MainPresenter.assistants, context)
        val isForResult = onResultListener != null
        adapter.isForResult = isForResult
        root.list.adapter = adapter
        if (!isForResult) {
            adapter.onEditistener = {
                openAssistentEditor(it)
            }
            adapter.onDeleteListener = {
                showDeleteDialog(
                    context,
                    R.string.delete_dialog_makeup_artist
                ) {
                    MainPresenter.deleteAssistent(it)
                    prepareAdapter(context)
                }
            }
        } else {
            adapter.onPickListener = { assistant: Assistant ->
                onResultListener?.invoke(assistant)
                fragmentManager?.popBackStack()
            }
        }
        root.searchFilter.addTextChangedListener(getSearchTextWatcher { adapter.filterBy(it) })
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {

            R.id.plus -> {
                openAssistentEditor(Assistant("", 0, ""))
            }
            R.id.search -> {
                isSearchShown = !isSearchShown
                root.searchFilter.visibility = if (isSearchShown) View.VISIBLE else View.GONE
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun openAssistentEditor(assistant: Assistant) {
        context?.let {
            var intent = Intent(context, AddAssistantActivity::class.java)
            intent.putExtra(MAKEUP_ARTIST, assistant)
            startActivityForResult(intent, AddAssistantActivity.EDIT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        context?.let { prepareAdapter(it) }
        super.onActivityResult(requestCode, resultCode, data)
    }

}