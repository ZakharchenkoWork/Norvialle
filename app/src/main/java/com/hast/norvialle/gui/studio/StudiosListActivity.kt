package com.hast.norvialle.gui.studio


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hast.norvialle.R
import com.hast.norvialle.data.Studio
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.studio.AddStudioActivity.Companion.STUDIO
import kotlinx.android.synthetic.main.activity_studios_list.*

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class StudiosListActivity : AppCompatActivity() {
    val presenter: MainPresenter =
        MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studios_list)
        setSupportActionBar(toolbar)


        list.layoutManager = LinearLayoutManager(this)
        prepareAdapter()
        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back);
            getSupportActionBar()?.setTitle(R.string.studios);
        }
    }
fun prepareAdapter(){
    val adapter = StudiosAdapter(MainPresenter.studios, this)
    list.adapter = adapter
    adapter.onEditStudioListener= StudiosAdapter.OnEditStudioListener{ openStudioEditor(it) }
    adapter.onDeleteStudioListener= StudiosAdapter.OnEditStudioListener{
        MainPresenter.deleteStudio(it)
        prepareAdapter()
    }
}
    override
    fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.studios_menu, menu);
        return true
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home-> {
                finish()
            }
            R.id.addStudio -> {
                openStudioEditor(Studio())
            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun openStudioEditor(studio: Studio){
        var intent = Intent(this, AddStudioActivity::class.java)
        intent.putExtra(STUDIO, studio)
        startActivityForResult(intent, AddStudioActivity.EDIT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            AddStudioActivity.EDIT ->{
                if (data != null && data.extras != null && data?.extras?.getSerializable(STUDIO) != null) {
                    var studio = data?.extras?.getSerializable(STUDIO) as Studio
                    presenter.addStudio(studio)
                    prepareAdapter()
                    Log.d("result", "is " + resultCode + " name: " + studio.name)
                }
            }
        }
    }
}