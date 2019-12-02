package com.hast.norvialle.gui.makeup


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hast.norvialle.R
import com.hast.norvialle.data.MakeupArtist
import com.hast.norvialle.data.PhotoRoom
import com.hast.norvialle.data.Studio
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.makeup.AddMakeupArtistActivity.Companion.MAKEUP_ARTIST
import com.hast.norvialle.gui.studio.AddStudioActivity.Companion.STUDIO
import kotlinx.android.synthetic.main.activity_studios_list.*

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class MakeupListActivity : AppCompatActivity() {
    companion object {
        val IS_FOR_RESULT = "for result"
    }

    val presenter: MainPresenter =
        MainPresenter
    var isForResult = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_makeup_list)
        setSupportActionBar(toolbar)
        isForResult = intent.getBooleanExtra(IS_FOR_RESULT, false)

        list.layoutManager = LinearLayoutManager(this)

        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back);
            getSupportActionBar()?.setTitle(R.string.makeupArtists);
        }
    }

    override fun onResume() {
        super.onResume()
        prepareAdapter()
    }
    fun prepareAdapter() {
        val adapter = MakeupAdapter(MainPresenter.makupArtists, this)
        adapter.isForResult = isForResult
        list.adapter = adapter
        if (!isForResult) {
            adapter.onEditMakeupArtistListener =
                MakeupAdapter.OnEditMakeupArtistListener{
                }
            adapter.onDeleteMakeupArtistListener= MakeupAdapter.OnEditMakeupArtistListener{
                MainPresenter.deleteMakeupArtist(it)
                prepareAdapter()
            }
        } else {
            adapter.onPickMakeupArtistListener= MakeupAdapter.OnPickMakeupArtistListener{
                    makeupArtist: MakeupArtist ->
                val finishIntent = Intent()
                finishIntent.putExtra("MAKEUP_ARTIST", makeupArtist)
                setResult(Activity.RESULT_OK, finishIntent)
                finish()
            }
        }
    }

    override
    fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.makup_artist_menu, menu);
        return true
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
            }
            R.id.addMakeup -> {
             openMakeupArtistEditor(MakeupArtist("",0,""))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun openMakeupArtistEditor(makeupArtist: MakeupArtist) {
        var intent = Intent(this, AddMakeupArtistActivity::class.java)
        intent.putExtra(MAKEUP_ARTIST, makeupArtist)
        startActivityForResult(intent, AddMakeupArtistActivity.EDIT)
    }

}