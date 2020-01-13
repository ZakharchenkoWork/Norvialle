package com.hast.norvialle.presenataion.utils

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.hast.norvialle.R
import com.hast.norvialle.data.Dress
import com.hast.norvialle.databinding.ActivityFullPictureBinding
import com.hast.norvialle.domain.FullScreenPictureViewModel
import com.hast.norvialle.domain.MainPresenter
import com.hast.norvialle.presenataion.base.BaseActivity
import com.hast.norvialle.presenataion.base.dresses.AddDressActivity
import com.hast.norvialle.presenataion.utils.dialogs.showDeleteDialog
import com.hast.norvialle.utils.loadPicture
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_full_picture.*
import java.lang.ref.WeakReference

/**
 * Created by Konstantyn Zakharchenko on 04.12.2019.
 */
class FullScreenPictureActivity : BaseActivity() {
    companion object {
        const val DATA_TYPE = "DRESS"
        const val EDIT_AND_DELETE = "EDIT_AND_DELETE"
    }
    private lateinit var viewModel: FullScreenPictureViewModel
    private lateinit var binding: ActivityFullPictureBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_picture)
        viewModel = ViewModelProviders.of(this).get(FullScreenPictureViewModel::class.java)


        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.setDress(getIntentData(DATA_TYPE))

        back.setOnClickListener { finish() }


        val hasControls = intent?.let { it.extras?.getBoolean(EDIT_AND_DELETE, false)}

        if (hasControls == null || !hasControls){
            editDeleteLayout.visibility = View.GONE
            commentText.setText(comment)
        } else{
            editDeleteLayout.visibility = View.VISIBLE
            val dress : Dress? = intent?.let { return@let it.extras?.getSerializable(
                DRESS
            ) as Dress }
            if (dress != null){
                fileName =dress.fileName
                edit.setOnClickListener{
                    openDressEditor(dress)
                }
                delete.setOnClickListener{
                    showDeleteDialog(
                        this,
                        R.string.delete_dialog_dress
                    ) {
                        MainPresenter.deleteDress(dress)
                        finish()
                    }
                }
            }

        }
        if (fileName != null) {
            progress.visibility = View.VISIBLE
            loadPicture(this, fileName).subscribeBy(
                onNext = {
                    photo.setImageBitmap(it)
                    bitmap = WeakReference(it)
                    progress.visibility = View.GONE
                },
                onError = {
                    progress.visibility = View.GONE
                    finish()
                })
        } else {
            finish()
        }

    }

    private fun openDressEditor(dress: Dress) {
        var intent = Intent(this, AddDressActivity::class.java)
        intent.putExtra(AddDressActivity.DATA_TYPE, dress)
        startActivityForResult(intent, AddDressActivity.EDIT)
        finish()
    }

    override fun getMenuRes(): Int {
        return 0
    }

    override fun getMenuTitleRes(): Int {
        return 0
    }

}