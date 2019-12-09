package com.hast.norvialle.gui.dresses


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.hast.norvialle.R
import com.hast.norvialle.data.Dress
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.utils.FullScreenPictureActivity
import kotlinx.android.synthetic.main.activity_makeup_list.*


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class DressesListActivity : AppCompatActivity() {
    companion object {
        val IS_FOR_RESULT = "for result"
        val DRESS = "DRESS"
        val PICKED_DRESSES = "PICKED_DRESSES"
    }

    val presenter: MainPresenter =
        MainPresenter
    var isForResult = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_makeup_list)
        setSupportActionBar(toolbar)
        isForResult = intent.getBooleanExtra(IS_FOR_RESULT, false)

        list.layoutManager = GridLayoutManager(this, 3)

        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back);
            getSupportActionBar()?.setTitle(R.string.dresses);
        }
    }

    override fun onResume() {
        super.onResume()
        prepareAdapter()
    }
    lateinit var adapter : DressesAdapter
    fun prepareAdapter() {
        adapter = DressesAdapter(MainPresenter.dresses, this)
        adapter.isForResult = isForResult
        list.adapter = adapter
        if (isForResult){
            val pickedDresses = intent.getSerializableExtra(PICKED_DRESSES) as ArrayList<Dress>
            adapter.setPicked(pickedDresses)

        }
        if (!isForResult) {
            adapter.onViewDressListener = DressesAdapter.OnViewDressListener {
                openPictureFullScreen(it)
            }
        }
    }

    fun openPictureFullScreen(dress : Dress) {

            val intent = Intent(this, FullScreenPictureActivity::class.java)
            intent.putExtra(FullScreenPictureActivity.DRESS, dress)
            intent.putExtra(FullScreenPictureActivity.EDIT_AND_DELETE, true)
            startActivity(intent)

    }
    override
    fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!isForResult) {
            getMenuInflater().inflate(R.menu.menu_plus, menu);
        } else{
            getMenuInflater().inflate(R.menu.menu_pick, menu);
        }
        return true
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
            }
            R.id.plus -> {
                openAddDressActivity(Dress("","",0))
            }
            R.id.pickDresses ->{
                val finishIntent = Intent()
                finishIntent.putExtra(DRESS, adapter.pickedDresses)
                setResult(Activity.RESULT_OK, finishIntent)
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun openAddDressActivity(dress: Dress) {
        var intent = Intent(this, AddDressActivity::class.java)
        intent.putExtra(AddDressActivity.DRESS, dress)
        startActivityForResult(intent, AddDressActivity.EDIT)
    }

}